package com.zhaw.catiejo.whatsforlunch._campusinfo.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.ServiceTimeDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.ServiceTimePeriodDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.ServiceTimePlanDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dto.GastronomicFacilityDto;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dto.ServiceTimePeriodDto;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dto.ServiceTimePlanDto;
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
import static com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider.ServiceTimePeriodInfo;

class ServiceTimePeriodUnidirectionalSyncOperation extends UnidirectionalSyncOperation<GastronomicFacilityDto, ServiceTimePeriodDao> {

    /**
     * Creates a new instance.
     *
     * @param context Android context.
     */
    ServiceTimePeriodUnidirectionalSyncOperation(Context context) {
        super(context);
    }

    @Override
    protected ImmutableCollection<ServiceTimePeriodDao> convertToLocalType(ImmutableCollection<GastronomicFacilityDto> remoteObjects) {
        ImmutableSet.Builder<ServiceTimePeriodDao> builder = ImmutableSet.builder();
        for (GastronomicFacilityDto facilityDto : remoteObjects) {
            for (ServiceTimePeriodDto serviceTimePeriodDto : facilityDto.getServiceTimePeriods()) {

                Optional<ServiceTimePlanDao> optionalOpeningTimePlan = Optional.absent();
                if (serviceTimePeriodDto.getOpeningTimePlan() != null) {
                    ServiceTimePlanDto serviceTimePlanDto = serviceTimePeriodDto.getOpeningTimePlan();

                    ServiceTimeDao monday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getMonday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getMonday().getUntil()));
                    ServiceTimeDao tuesday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getTuesday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getTuesday().getUntil()));
                    ServiceTimeDao wednesday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getWednesday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getWednesday().getUntil()));
                    ServiceTimeDao thursday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getThursday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getThursday().getUntil()));
                    ServiceTimeDao friday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getFriday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getFriday().getUntil()));
                    ServiceTimeDao saturday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getSaturday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getSaturday().getUntil()));
                    ServiceTimeDao sunday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getSunday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getSunday().getUntil()));

                    optionalOpeningTimePlan = Optional.of(new ServiceTimePlanDao(monday, tuesday, wednesday, thursday,
                            friday, saturday, sunday));
                }
                Optional<ServiceTimePlanDao> optionalLunchTimePlan = Optional.absent();
                if (serviceTimePeriodDto.getLunchTimePlan() != null) {
                    ServiceTimePlanDto serviceTimePlanDto = serviceTimePeriodDto.getLunchTimePlan();

                    ServiceTimeDao monday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getMonday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getMonday().getUntil()));
                    ServiceTimeDao tuesday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getTuesday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getTuesday().getUntil()));
                    ServiceTimeDao wednesday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getWednesday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getWednesday().getUntil()));
                    ServiceTimeDao thursday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getThursday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getThursday().getUntil()));
                    ServiceTimeDao friday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getFriday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getFriday().getUntil()));
                    ServiceTimeDao saturday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getSaturday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getSaturday().getUntil()));
                    ServiceTimeDao sunday = new ServiceTimeDao(Optional.fromNullable(serviceTimePlanDto.getSunday().getFrom()),
                            Optional.fromNullable(serviceTimePlanDto.getSunday().getUntil()));

                    optionalLunchTimePlan = Optional.of(new ServiceTimePlanDao(monday, tuesday, wednesday, thursday,
                            friday, saturday, sunday));
                }

                builder.add(new ServiceTimePeriodDao(null, serviceTimePeriodDto.getId(), facilityDto.getId(),
                        serviceTimePeriodDto.getStartsOn(), serviceTimePeriodDto.getEndsOn(), optionalOpeningTimePlan,
                        optionalLunchTimePlan, DateTime.now(Constants.LocalTimeZone), serviceTimePeriodDto.getVersion()));
            }
        }
        return builder.build();
    }

    @Override
    protected Collection<? extends ContentProviderOperation> removeVanished(ImmutableCollection<ServiceTimePeriodDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forServiceTimePeriods(), null, null, null, null);

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                ServiceTimePeriodDao localServiceTimePeriodDao = ServiceTimePeriodDao.fromCursor(cursor);

                Optional<ServiceTimePeriodDao> remoteServiceTimePeriodDao = this.findServiceTimePeriodDao(convertedRemoteObjects,
                        localServiceTimePeriodDao.getServiceTimePeriodId(),
                        localServiceTimePeriodDao.getFacilityId());

                if (remoteServiceTimePeriodDao.isPresent()) {
                    continue;
                }

                operations.add(ContentProviderOperation.newDelete(ActionUri.forServiceTimePeriod(localServiceTimePeriodDao.getId()))
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
    protected Collection<? extends ContentProviderOperation> updateOutdated(ImmutableCollection<ServiceTimePeriodDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forServiceTimePeriods(), null, null, null, null);

            String selection = Phrase.from("{service_time_period_id} = ? AND {facility_id} = ? AND {version} = ?")
                    .put("service_time_period_id", ServiceTimePeriodInfo.ServiceTimePeriodId)
                    .put("facility_id", ServiceTimePeriodInfo.FacilityId)
                    .put("version", ServiceTimePeriodInfo.Version)
                    .format()
                    .toString();

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                ServiceTimePeriodDao localServiceTimePeriodDao = ServiceTimePeriodDao.fromCursor(cursor);

                Optional<ServiceTimePeriodDao> remoteServiceTimePeriodDao = findServiceTimePeriodDao(convertedRemoteObjects, localServiceTimePeriodDao.getServiceTimePeriodId(),
                        localServiceTimePeriodDao.getFacilityId());

                if (!remoteServiceTimePeriodDao.isPresent()) {
                    continue;
                }
                if (localServiceTimePeriodDao.getVersion().equals(remoteServiceTimePeriodDao.get().getVersion())) {
                    String[] selectionArgs = new String[]{
                            localServiceTimePeriodDao.getServiceTimePeriodId().toString(),
                            localServiceTimePeriodDao.getFacilityId().toString(),
                            localServiceTimePeriodDao.getVersion()
                    };

                    operations.add(ContentProviderOperation.newUpdate(ActionUri.forServiceTimePeriod(localServiceTimePeriodDao.getId()))
                            .withValue(ServiceTimePeriodInfo.LastUpdate, DateTime.now(Constants.LocalTimeZone).toString())
                            .withSelection(selection, selectionArgs)
                            .build());

                    continue;
                }

                String[] selectionArgs = new String[]{
                        localServiceTimePeriodDao.getServiceTimePeriodId().toString(),
                        localServiceTimePeriodDao.getFacilityId().toString(),
                        localServiceTimePeriodDao.getVersion()
                };

                ServiceTimePeriodDao updateDao = new ServiceTimePeriodDao(localServiceTimePeriodDao.getId(),
                        remoteServiceTimePeriodDao.get().getServiceTimePeriodId(),
                        remoteServiceTimePeriodDao.get().getFacilityId(),
                        remoteServiceTimePeriodDao.get().getStartsOn(),
                        remoteServiceTimePeriodDao.get().getEndsOn(),
                        remoteServiceTimePeriodDao.get().getOpeningTimePlan(),
                        remoteServiceTimePeriodDao.get().getLunchTimePlan(),
                        DateTime.now(Constants.LocalTimeZone), remoteServiceTimePeriodDao.get().getVersion());

                operations.add(ContentProviderOperation.newUpdate(ActionUri.forServiceTimePeriod(localServiceTimePeriodDao.getId()))
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
    protected Collection<? extends ContentProviderOperation> addNew(ImmutableCollection<ServiceTimePeriodDao> convertedRemoteObjects) {
        ImmutableSet.Builder<ServiceTimePeriodDao> builder = ImmutableSet.builder();
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forServiceTimePeriods(), null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                builder.add(ServiceTimePeriodDao.fromCursor(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ImmutableSet<ServiceTimePeriodDao> localServiceTimePeriodDaos = builder.build();
        ArrayList<ContentProviderOperation> operations = Lists.newArrayList();

        for (ServiceTimePeriodDao remoteServiceTimePeriodDao : convertedRemoteObjects) {
            Optional<ServiceTimePeriodDao> localServiceTimePeriodDao = findServiceTimePeriodDao(localServiceTimePeriodDaos,
                    remoteServiceTimePeriodDao.getServiceTimePeriodId(), remoteServiceTimePeriodDao.getFacilityId());

            if (localServiceTimePeriodDao.isPresent()) {
                continue;
            }

            operations.add(ContentProviderOperation.newInsert(ActionUri.forServiceTimePeriods())
                    .withValues(remoteServiceTimePeriodDao.toContentValues())
                    .build());
        }

        return operations;
    }

    private Optional<ServiceTimePeriodDao> findServiceTimePeriodDao(ImmutableCollection<ServiceTimePeriodDao> listToSearch,
                                                                    long serviceTimePeriodIdWanted, long facilityIdWanted) {
        for (ServiceTimePeriodDao serviceTimePeriodDao : listToSearch) {
            if (serviceTimePeriodDao.getServiceTimePeriodId().equals(serviceTimePeriodIdWanted) &&
                    serviceTimePeriodDao.getFacilityId().equals(facilityIdWanted)) {
                return Optional.of(serviceTimePeriodDao);
            }
        }
        return Optional.absent();
    }
}
