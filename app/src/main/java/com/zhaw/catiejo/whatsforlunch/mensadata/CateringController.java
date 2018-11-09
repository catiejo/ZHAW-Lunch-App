package com.zhaw.catiejo.whatsforlunch.mensadata;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.squareup.phrase.Phrase;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import com.zhaw.catiejo.whatsforlunch.mensadata.dao.IDaoFactory;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.GastronomicFacilityDao;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.GastronomicFacilityDaoFactory;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.HolidayDao;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.HolidayDaoFactory;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.ServiceTimePeriodDao;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.ServiceTimePeriodDaoFactory;
import com.zhaw.catiejo.whatsforlunch.mensadata.sync.ICateringInformationAvailabilityManager;

import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.ActionUri;
import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.DishInfo;
import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.GastronomicFacilityInfo;
import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.HolidayInfo;
import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.ServiceTimePeriodInfo;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides catering-related operations.
 */
public class CateringController implements ICateringController {

    public static final String[] FACILITY_CURSOR_COLUMNS = new String[]{
            "_id", "FacilityId", "Type", "Name", "OpeningTimeStart", "OpeningTimeEnd", "LunchTimeStart", "LunchTimeEnd"
    };

    private final Context mContext;

    private final ContentResolver mContentResolver;

    private final ICateringInformationAvailabilityManager cateringInformationAvailabilityManager;

    public CateringController(Context mContext, ICateringInformationAvailabilityManager cateringInformationAvailabilityManager) {
        this.mContext = mContext;
        this.mContentResolver = mContext.getContentResolver();
        this.cateringInformationAvailabilityManager = cateringInformationAvailabilityManager;
    }

    @Override
    public Cursor getCanteens(LocalDate day) {
        return getGastronomicFacilities(GastronomicFacilityDao.Type.Canteen.name(), day);
    }

    @Override
    public Cursor getCafeterias(LocalDate day) {
        return getGastronomicFacilities(GastronomicFacilityDao.Type.Cafeteria.name(), day);
    }

    private Cursor getGastronomicFacilities(String type, LocalDate day) {
        checkNotNull(type);
        checkNotNull(day);

        cateringInformationAvailabilityManager.ensureCateringInformationAvailability();
        String facilitySelection = Phrase.from("{type} = ?").put("type", GastronomicFacilityInfo.Type).format().toString();
        String[] facilitySelectionArgs = new String[]{type};
        String facilitySortOrder = Joiner.on(", ")
                .join(Lists.newArrayList(GastronomicFacilityInfo.Location, GastronomicFacilityInfo.Name));

        ImmutableList<GastronomicFacilityDao> canteens = asList(mContentResolver.query(ActionUri.forGastronomicFacilities(),
                null, facilitySelection, facilitySelectionArgs, facilitySortOrder), new GastronomicFacilityDaoFactory());

        String periodSelection = Phrase.from("{starts_on} <= ? AND {ends_on} >= ?")
                .put("starts_on", ServiceTimePeriodInfo.StartsOn)
                .put("ends_on", ServiceTimePeriodInfo.EndsOn)
                .format()
                .toString();
        String[] periodSelectionArgs = new String[]{day.toString(), day.toString()};

        ImmutableList<ServiceTimePeriodDao> serviceTimePeriods = asList(mContentResolver.query(ActionUri.forServiceTimePeriods(),
                null, periodSelection, periodSelectionArgs, null), new ServiceTimePeriodDaoFactory());

        String holidaySelection = Phrase.from("{starts_at} <= ? AND {ends_at} > ?")
                .put("starts_at", HolidayInfo.StartsAt)
                .put("ends_at", HolidayInfo.EndsAt)
                .format()
                .toString();
        String[] holidaySelectionArgs = new String[]{day.toString(), day.toString()};

        ImmutableList<HolidayDao> holidays = asList(mContentResolver.query(ActionUri.forHolidays(),
                null, holidaySelection, holidaySelectionArgs, null), new HolidayDaoFactory());

        MatrixCursor cursor = new MatrixCursor(FACILITY_CURSOR_COLUMNS);

        for (GastronomicFacilityDao facilityDao : canteens) {
            List<Object> columnValues = Lists.newArrayList();
            columnValues.add(facilityDao.getId());
            columnValues.add(facilityDao.getFacilityId());
            columnValues.add(facilityDao.getType().name());
            columnValues.add(facilityDao.getName());

            Optional<LocalTime> openingTimeStart = Optional.absent();
            Optional<LocalTime> openingTimeEnd = Optional.absent();
            Optional<LocalTime> lunchTimeStart = Optional.absent();
            Optional<LocalTime> lunchTimeEnd = Optional.absent();
            for (ServiceTimePeriodDao periodDao : serviceTimePeriods) {
                if (!periodDao.getFacilityId().equals(facilityDao.getFacilityId())) {
                    continue;
                }
                if (!periodDao.isValidOn(day)) {
                    continue;
                }

                Optional<HolidayDao> optionalHolidayDao = Optional.absent();
                for (HolidayDao holidayDao : holidays) {
                    if (!holidayDao.getFacilityId().equals(facilityDao.getFacilityId())) {
                        continue;
                    }
                    if (!holidayDao.isValidOn(day)) {
                        continue;
                    }

                    optionalHolidayDao = Optional.of(holidayDao);
                }

                if (optionalHolidayDao.isPresent()) {
                    openingTimeStart = periodDao.getHolidayAdjustedOpeningTimeStart(day, optionalHolidayDao.get());
                    openingTimeEnd = periodDao.getHolidayAdjustedOpeningTimeEnd(day, optionalHolidayDao.get());
                    lunchTimeStart = periodDao.getHolidayAdjustedLunchTimeStart(day, optionalHolidayDao.get());
                    lunchTimeEnd = periodDao.getHolidayAdjustedLunchTimeEnd(day, optionalHolidayDao.get());
                } else {
                    openingTimeStart = periodDao.getOpeningTimeStartForWeekday(day.getDayOfWeek());
                    openingTimeEnd = periodDao.getOpeningTimeEndForWeekday(day.getDayOfWeek());
                    lunchTimeStart = periodDao.getLunchTimeStartForWeekday(day.getDayOfWeek());
                    lunchTimeEnd = periodDao.getLunchTimeEndForWeekday(day.getDayOfWeek());
                }
            }
            columnValues.add(openingTimeStart.orNull());
            columnValues.add(openingTimeEnd.orNull());
            columnValues.add(lunchTimeStart.orNull());
            columnValues.add(lunchTimeEnd.orNull());

            // Conversion to array currently needed because Robolectric as of version 2.1.1 causes an NPE when
            // supplying an iterable.
            cursor.addRow(columnValues.toArray());
        }

        return cursor;
    }

    @Override
    public Cursor getMenu(long facilityId, LocalDate day) {
        cateringInformationAvailabilityManager.ensureMenuPlanAvailability();

        String selection = Phrase.from("{facility_id} = ? AND {offered_on} = ?")
                .put("facility_id", DishInfo.FacilityId)
                .put("offered_on", DishInfo.OfferedOn)
                .format()
                .toString();

        String[] selectionArgs = new String[]{
                String.valueOf(facilityId),
                day.toString()
        };

        return mContentResolver.query(ActionUri.forDishes(), null, selection, selectionArgs, DishInfo.DishId);
    }

    @Override
    public Optional<ServiceTimePeriodDao> getServiceTimePeriod(long facilityId, LocalDate day) {
        String selection = Phrase.from("{facility_id} = ? AND {starts_on} <= ? AND {ends_on} >= ?")
                .put("facility_id", ServiceTimePeriodInfo.FacilityId)
                .put("starts_on", ServiceTimePeriodInfo.StartsOn)
                .put("ends_on", ServiceTimePeriodInfo.EndsOn)
                .format()
                .toString();

        String[] selectionArgs = new String[]{
                String.valueOf(facilityId),
                day.toString(),
                day.toString()
        };

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(ActionUri.forServiceTimePeriods(), null, selection, selectionArgs, null);

            if (!cursor.moveToFirst()) {
                return Optional.absent();
            }

            return Optional.of(ServiceTimePeriodDao.fromCursor(cursor));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void syncCateringInformation() {
        cateringInformationAvailabilityManager.syncCateringInformation();
    }

    @Override
    public Optional<HolidayDao> getHoliday(long facilityId, LocalDate day) {
        String selection = Phrase.from("{facility_id} = ? AND ({starts_on} <= ? OR {starts_on} LIKE ?) AND ({ends_on} > ? OR {ends_on} LIKE ?)")
                .put("facility_id", HolidayInfo.FacilityId)
                .put("starts_on", HolidayInfo.StartsAt)
                .put("ends_on", HolidayInfo.EndsAt)
                .format()
                .toString();

        String[] selectionArgs = new String[]{
                String.valueOf(facilityId),
                day.toString(),
                day.toString() + '%',
                day.toString(),
                day.toString() + '%'
        };

        Cursor cursor = null;
        try {
            cursor = mContentResolver.query(ActionUri.forHolidays(), null, selection, selectionArgs, null);

            if (!cursor.moveToFirst()) {
                return Optional.absent();
            }

            return Optional.of(HolidayDao.fromCursor(cursor));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private <T> ImmutableList<T> asList(Cursor cursor, IDaoFactory<T> conversion) {
        try {
            ImmutableList.Builder<T> builder = ImmutableList.builder();
            while (cursor != null && cursor.moveToNext()) {
                builder.add(conversion.constructFromCursor(cursor));
            }
            return builder.build();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
