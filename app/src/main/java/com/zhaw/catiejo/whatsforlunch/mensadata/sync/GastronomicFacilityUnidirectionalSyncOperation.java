package com.zhaw.catiejo.whatsforlunch.mensadata.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.GastronomicFacilityDao;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.GastronomicFacilityDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.helper.Constants;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.squareup.phrase.Phrase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;

import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.ActionUri;
import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.GastronomicFacilityInfo;

class GastronomicFacilityUnidirectionalSyncOperation extends UnidirectionalSyncOperation<GastronomicFacilityDto, GastronomicFacilityDao> {

    /**
     * Creates a new instance.
     *
     * @param context Android context.
     */
    GastronomicFacilityUnidirectionalSyncOperation(Context context) {
        super(context);
    }

    @Override
    protected ImmutableCollection<GastronomicFacilityDao> convertToLocalType(ImmutableCollection<GastronomicFacilityDto> remoteObjects) {
        ImmutableSet.Builder<GastronomicFacilityDao> builder = ImmutableSet.builder();
        for (GastronomicFacilityDto facilityDto : remoteObjects) {
            builder.add(new GastronomicFacilityDao(null, facilityDto.getId(),
                    GastronomicFacilityDao.Type.valueOf(facilityDto.getType()), facilityDto.getName(),
                    facilityDto.getLocation().getName(), DateTime.now(Constants.LocalTimeZone),
                    facilityDto.getVersion()));
        }
        return builder.build();
    }

    @Override
    protected Collection<? extends ContentProviderOperation> removeVanished(ImmutableCollection<GastronomicFacilityDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forGastronomicFacilities(), null, null, null, null);

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                GastronomicFacilityDao localFacilityDao = GastronomicFacilityDao.fromCursor(cursor);

                Optional<GastronomicFacilityDao> remoteFacilityDao = this.findFacilityDao(convertedRemoteObjects,
                        localFacilityDao.getFacilityId());

                if (remoteFacilityDao.isPresent()) {
                    continue;
                }

                operations.add(ContentProviderOperation.newDelete(ActionUri.forGastronomicFacility(localFacilityDao.getId()))
                        .build());
            }

            return operations;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected Collection<? extends ContentProviderOperation> updateOutdated(ImmutableCollection<GastronomicFacilityDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forGastronomicFacilities(), null, null, null, null);

            String selection = Phrase.from("{facility_id} = ? AND {version} = ?")
                    .put("facility_id", GastronomicFacilityInfo.FacilityId)
                    .put("version", GastronomicFacilityInfo.Version)
                    .format()
                    .toString();

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                GastronomicFacilityDao localGastronomicFacilityDao = GastronomicFacilityDao.fromCursor(cursor);

                Optional<GastronomicFacilityDao> remoteFacilityDao = findFacilityDao(convertedRemoteObjects,
                        localGastronomicFacilityDao.getFacilityId());

                if (!remoteFacilityDao.isPresent()) {
                    continue;
                }

                /*
                 * The locations are separately versioned entities, so they have to be compared separately.
                 */
                if (localGastronomicFacilityDao.getVersion().equals(remoteFacilityDao.get().getVersion()) &&
                        localGastronomicFacilityDao.getLocation().equals(remoteFacilityDao.get().getLocation())) {

                    String[] selectionArgs = new String[]{
                            localGastronomicFacilityDao.getFacilityId().toString(),
                            localGastronomicFacilityDao.getVersion()
                    };

                    operations.add(ContentProviderOperation.newUpdate(ActionUri.forGastronomicFacility(localGastronomicFacilityDao.getId()))
                            .withValue(GastronomicFacilityInfo.LastUpdate, DateTime.now(Constants.LocalTimeZone).toString())
                            .withSelection(selection, selectionArgs)
                            .build());

                    continue;
                }

                String[] selectionArgs = new String[]{
                        localGastronomicFacilityDao.getFacilityId().toString(),
                        localGastronomicFacilityDao.getVersion()
                };

                GastronomicFacilityDao updateDao = new GastronomicFacilityDao(localGastronomicFacilityDao.getId(),
                        remoteFacilityDao.get().getFacilityId(), remoteFacilityDao.get().getType(),
                        remoteFacilityDao.get().getName(), remoteFacilityDao.get().getLocation(),
                        DateTime.now(Constants.LocalTimeZone), remoteFacilityDao.get().getVersion());

                operations.add(ContentProviderOperation.newUpdate(ActionUri.forGastronomicFacility(localGastronomicFacilityDao.getId()))
                        .withValues(updateDao.toContentValues())
                        .withSelection(selection, selectionArgs)
                        .build());
            }

            return operations;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected Collection<? extends ContentProviderOperation> addNew(ImmutableCollection<GastronomicFacilityDao> convertedRemoteObjects) {
        ImmutableSet.Builder<GastronomicFacilityDao> builder = ImmutableSet.builder();
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forGastronomicFacilities(), null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                builder.add(GastronomicFacilityDao.fromCursor(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ImmutableSet<GastronomicFacilityDao> localFacilityDaos = builder.build();
        ArrayList<ContentProviderOperation> operations = Lists.newArrayList();

        for (GastronomicFacilityDao remoteFacilityDao : convertedRemoteObjects) {
            Optional<GastronomicFacilityDao> localGastronomicFacilityDao = findFacilityDao(localFacilityDaos,
                    remoteFacilityDao.getFacilityId());

            if (localGastronomicFacilityDao.isPresent()) {
                continue;
            }

            operations.add(ContentProviderOperation.newInsert(ActionUri.forGastronomicFacilities())
                    .withValues(remoteFacilityDao.toContentValues())
                    .build());
        }

        return operations;
    }

    private Optional<GastronomicFacilityDao> findFacilityDao(ImmutableCollection<GastronomicFacilityDao> listToSearch, long facilityIdWanted) {
        for (GastronomicFacilityDao facilityDao : listToSearch) {
            if (facilityDao.getFacilityId().equals(facilityIdWanted)) {
                return Optional.of(facilityDao);
            }
        }
        return Optional.absent();
    }
}
