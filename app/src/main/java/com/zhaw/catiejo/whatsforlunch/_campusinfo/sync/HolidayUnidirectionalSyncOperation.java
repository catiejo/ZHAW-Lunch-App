package com.zhaw.catiejo.whatsforlunch._campusinfo.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.HolidayDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dto.GastronomicFacilityDto;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dto.HolidayDto;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.squareup.phrase.Phrase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;

import static com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider.ActionUri;
import static com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider.HolidayInfo;

class HolidayUnidirectionalSyncOperation extends UnidirectionalSyncOperation<GastronomicFacilityDto, HolidayDao> {

    /**
     * Creates a new instance.
     *
     * @param context Android context.
     */
    HolidayUnidirectionalSyncOperation(Context context) {
        super(context);
    }

    @Override
    protected ImmutableCollection<HolidayDao> convertToLocalType(ImmutableCollection<GastronomicFacilityDto> remoteObjects) {
        ImmutableSet.Builder<HolidayDao> builder = ImmutableSet.builder();
        for (GastronomicFacilityDto facilityDto : remoteObjects) {
            for (HolidayDto holidayDto : facilityDto.getHolidays()) {
                builder.add(new HolidayDao(null, holidayDto.getId(), facilityDto.getId(), holidayDto.getName(),
                        holidayDto.getStartsAt(), holidayDto.getEndsAt(), DateTime.now(Constants.LocalTimeZone),
                        holidayDto.getVersion()));
            }
        }
        return builder.build();
    }

    @Override
    protected Collection<? extends ContentProviderOperation> removeVanished(ImmutableCollection<HolidayDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forHolidays(), null, null, null, null);

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                HolidayDao localHolidayDao = HolidayDao.fromCursor(cursor);

                Optional<HolidayDao> remoteHolidayDao = this.findHolidayDao(convertedRemoteObjects, localHolidayDao.getHolidayId(),
                        localHolidayDao.getFacilityId());

                if (remoteHolidayDao.isPresent()) {
                    continue;
                }

                operations.add(ContentProviderOperation.newDelete(ActionUri.forHoliday(localHolidayDao.getId()))
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
    protected Collection<? extends ContentProviderOperation> updateOutdated(ImmutableCollection<HolidayDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forHolidays(), null, null, null, null);

            String selection = Phrase.from("{holiday_id} = ? AND {facility_id} = ? AND {version} = ?")
                    .put("holiday_id", HolidayInfo.HolidayId)
                    .put("facility_id", HolidayInfo.FacilityId)
                    .put("version", HolidayInfo.Version)
                    .format()
                    .toString();

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                HolidayDao localHolidayDao = HolidayDao.fromCursor(cursor);

                Optional<HolidayDao> remoteHolidayDao = findHolidayDao(convertedRemoteObjects, localHolidayDao.getHolidayId(),
                        localHolidayDao.getFacilityId());

                if (!remoteHolidayDao.isPresent()) {
                    continue;
                }
                if (localHolidayDao.getVersion().equals(remoteHolidayDao.get().getVersion())) {
                    String[] selectionArgs = new String[]{
                            localHolidayDao.getHolidayId().toString(),
                            localHolidayDao.getFacilityId().toString(),
                            localHolidayDao.getVersion()
                    };

                    operations.add(ContentProviderOperation.newUpdate(ActionUri.forHoliday(localHolidayDao.getId()))
                            .withValue(HolidayInfo.LastUpdate, DateTime.now(Constants.LocalTimeZone).toString())
                            .withSelection(selection, selectionArgs)
                            .build());

                    continue;
                }

                String[] selectionArgs = new String[]{
                        localHolidayDao.getHolidayId().toString(),
                        localHolidayDao.getFacilityId().toString(),
                        localHolidayDao.getVersion()
                };

                HolidayDao updateDao = new HolidayDao(localHolidayDao.getId(), remoteHolidayDao.get().getHolidayId(),
                        remoteHolidayDao.get().getFacilityId(), remoteHolidayDao.get().getName(),
                        remoteHolidayDao.get().getStartsAt(), remoteHolidayDao.get().getEndsAt(),
                        DateTime.now(Constants.LocalTimeZone), remoteHolidayDao.get().getVersion());

                operations.add(ContentProviderOperation.newUpdate(ActionUri.forHoliday(localHolidayDao.getId()))
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
    protected Collection<? extends ContentProviderOperation> addNew(ImmutableCollection<HolidayDao> convertedRemoteObjects) {
        ImmutableSet.Builder<HolidayDao> builder = ImmutableSet.builder();
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forHolidays(), null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                builder.add(HolidayDao.fromCursor(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ImmutableSet<HolidayDao> localHolidayDaos = builder.build();
        ArrayList<ContentProviderOperation> operations = Lists.newArrayList();

        for (HolidayDao remoteHolidayDao : convertedRemoteObjects) {
            Optional<HolidayDao> localHolidayDao = findHolidayDao(localHolidayDaos, remoteHolidayDao.getHolidayId(),
                    remoteHolidayDao.getFacilityId());

            if (localHolidayDao.isPresent()) {
                continue;
            }

            operations.add(ContentProviderOperation.newInsert(ActionUri.forHolidays())
                    .withValues(remoteHolidayDao.toContentValues())
                    .build());
        }

        return operations;
    }

    private Optional<HolidayDao> findHolidayDao(ImmutableCollection<HolidayDao> listToSearch, long holidayIdWanted,
                                                long facilityIdWanted) {
        for (HolidayDao dishDao : listToSearch) {
            if (dishDao.getHolidayId().equals(holidayIdWanted) && dishDao.getFacilityId().equals(facilityIdWanted)) {
                return Optional.of(dishDao);
            }
        }
        return Optional.absent();
    }
}
