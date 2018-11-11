package com.zhaw.catiejo.whatsforlunch._campusinfo.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.joda.time.*;

import java.util.Map;

import static com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider.ServiceTimePeriodInfo.*;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a service time period. Every instance is immutable once created.
 */
public class ServiceTimePeriodDao {

    private final Long id;
    private final Long serviceTimePeriodId;
    private final Long facilityId;
    private final LocalDate startsOn;
    private final LocalDate endsOn;
    private final Optional<ServiceTimePlanDao> openingTimePlan;
    private final Optional<ServiceTimePlanDao> lunchTimePlan;
    private final DateTime lastUpdate;
    private final String version;

    /**
     * Creates new instance using the data supplied by the given cursor.
     *
     * @param cursor Cursor to extract the data from.
     * @return New service time period instance.
     */
    public static ServiceTimePeriodDao fromCursor(Cursor cursor) {
        String[] openingTimeNames = new String[]{
                OpeningTimeStartMonday, OpeningTimeEndMonday,
                OpeningTimeStartTuesday, OpeningTimeEndTuesday,
                OpeningTimeStartWednesday, OpeningTimeEndWednesday,
                OpeningTimeStartThursday, OpeningTimeEndThursday,
                OpeningTimeStartFriday, OpeningTimeEndFriday,
                OpeningTimeStartSaturday, OpeningTimeEndSaturday,
                OpeningTimeStartSunday, OpeningTimeEndSunday
        };
        Map<String, Optional<LocalTime>> openingTimes = Maps.newHashMap();

        for (String name : openingTimeNames) {
            if (cursor.isNull(cursor.getColumnIndex(name))) {
                openingTimes.put(name, Optional.<LocalTime>absent());
            } else {
                openingTimes.put(name, Optional.of(LocalTime.parse(cursor.getString(cursor.getColumnIndex(name)))));
            }
        }

        String[] lunchTimeNames = new String[]{
                LunchTimeStartMonday, LunchTimeEndMonday,
                LunchTimeStartTuesday, LunchTimeEndTuesday,
                LunchTimeStartWednesday, LunchTimeEndWednesday,
                LunchTimeStartThursday, LunchTimeEndThursday,
                LunchTimeStartFriday, LunchTimeEndFriday,
                LunchTimeStartSaturday, LunchTimeEndSaturday,
                LunchTimeStartSunday, LunchTimeEndSunday
        };
        Map<String, Optional<LocalTime>> lunchTimes = Maps.newHashMap();

        for (String name : lunchTimeNames) {
            if (cursor.isNull(cursor.getColumnIndex(name))) {
                lunchTimes.put(name, Optional.<LocalTime>absent());
            } else {
                lunchTimes.put(name, Optional.of(LocalTime.parse(cursor.getString(cursor.getColumnIndex(name)))));
            }
        }

        ServiceTimePlanDao openingTimePlan = new ServiceTimePlanDao(
                new ServiceTimeDao(openingTimes.get(OpeningTimeStartMonday), openingTimes.get(OpeningTimeEndMonday)),
                new ServiceTimeDao(openingTimes.get(OpeningTimeStartTuesday), openingTimes.get(OpeningTimeEndTuesday)),
                new ServiceTimeDao(openingTimes.get(OpeningTimeStartWednesday), openingTimes.get(OpeningTimeEndWednesday)),
                new ServiceTimeDao(openingTimes.get(OpeningTimeStartThursday), openingTimes.get(OpeningTimeEndThursday)),
                new ServiceTimeDao(openingTimes.get(OpeningTimeStartFriday), openingTimes.get(OpeningTimeEndFriday)),
                new ServiceTimeDao(openingTimes.get(OpeningTimeStartSaturday), openingTimes.get(OpeningTimeEndSaturday)),
                new ServiceTimeDao(openingTimes.get(OpeningTimeStartSunday), openingTimes.get(OpeningTimeEndSunday))
        );

        ServiceTimePlanDao lunchTimePlan = new ServiceTimePlanDao(
                new ServiceTimeDao(lunchTimes.get(LunchTimeStartMonday), lunchTimes.get(LunchTimeEndMonday)),
                new ServiceTimeDao(lunchTimes.get(LunchTimeStartTuesday), lunchTimes.get(LunchTimeEndTuesday)),
                new ServiceTimeDao(lunchTimes.get(LunchTimeStartWednesday), lunchTimes.get(LunchTimeEndWednesday)),
                new ServiceTimeDao(lunchTimes.get(LunchTimeStartThursday), lunchTimes.get(LunchTimeEndThursday)),
                new ServiceTimeDao(lunchTimes.get(LunchTimeStartFriday), lunchTimes.get(LunchTimeEndFriday)),
                new ServiceTimeDao(lunchTimes.get(LunchTimeStartSaturday), lunchTimes.get(LunchTimeEndSaturday)),
                new ServiceTimeDao(lunchTimes.get(LunchTimeStartSunday), lunchTimes.get(LunchTimeEndSunday))
        );

        long id = cursor.getLong(cursor.getColumnIndex(Id));
        long serviceTimePeriodId = cursor.getLong(cursor.getColumnIndex(ServiceTimePeriodId));
        long facilityId = cursor.getLong(cursor.getColumnIndex(FacilityId));
        LocalDate startsOn = LocalDate.parse(cursor.getString(cursor.getColumnIndex(StartsOn)));
        LocalDate endsOn = LocalDate.parse(cursor.getString(cursor.getColumnIndex(EndsOn)));
        Optional<ServiceTimePlanDao> optionalOpeningTimePlan =
                (openingTimePlan.isEmpty()) ? Optional.<ServiceTimePlanDao>absent() : Optional.of(openingTimePlan);
        Optional<ServiceTimePlanDao> optionalLunchTimePlan =
                (lunchTimePlan.isEmpty()) ? Optional.<ServiceTimePlanDao>absent() : Optional.of(lunchTimePlan);
        DateTime lastUpdate = DateTime.parse(cursor.getString(cursor.getColumnIndex(LastUpdate)));
        String version = cursor.getString(cursor.getColumnIndex(Version));

        return new ServiceTimePeriodDao(id, serviceTimePeriodId, facilityId, startsOn, endsOn, optionalOpeningTimePlan,
                optionalLunchTimePlan, lastUpdate, version);
    }

    /**
     * Creates new immutable instance.
     *
     * @param id                  The SQLite rowid of the service time period.
     * @param serviceTimePeriodId The id of the service time period.
     * @param facilityId          The id of the facility the service time period is assigned to.
     * @param startsOn            The date when the service time period starts.
     * @param endsOn              The date when the service time period ends.
     * @param openingTimePlan     The opening time plan of the service time period.
     * @param lunchTimePlan       The lunch time plan of the service time period.
     * @param lastUpdate          Date and time when the service time period was updated the last time.
     * @param version             Version of the service time period.
     */
    public ServiceTimePeriodDao(Long id, Long serviceTimePeriodId, Long facilityId, LocalDate startsOn, LocalDate endsOn,
                                Optional<ServiceTimePlanDao> openingTimePlan,
                                Optional<ServiceTimePlanDao> lunchTimePlan, DateTime lastUpdate, String version) {
        this.id = id;
        this.serviceTimePeriodId = checkNotNull(serviceTimePeriodId);
        this.facilityId = checkNotNull(facilityId);
        this.startsOn = checkNotNull(startsOn);
        this.endsOn = checkNotNull(endsOn);
        this.openingTimePlan = checkNotNull(openingTimePlan);
        this.lunchTimePlan = checkNotNull(lunchTimePlan);
        this.lastUpdate = checkNotNull(lastUpdate);
        this.version = checkNotNull(version);
    }

    /**
     * Gets the SQLite rowid of the service time period.
     *
     * @return The SQLite rowid of the service time period.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the ID of the service time period.
     *
     * @return The ID of the service time period.
     */
    public Long getServiceTimePeriodId() {
        return serviceTimePeriodId;
    }

    /**
     * Gets the ID of the facility this service time period is assigned to.
     *
     * @return The ID of the facility.
     */
    public Long getFacilityId() {
        return facilityId;
    }

    /**
     * Gets the start date of the service time period in the local time zone of the facility. The duration is defined
     * as: {@code start <= duration <= end}.
     *
     * @return The start date of the service time period.
     */
    public LocalDate getStartsOn() {
        return startsOn;
    }

    /**
     * Gets the end date of the service time period in the local time zone of the facility. The duration is defined
     * as: {@code start <= duration <= end}.
     *
     * @return The end date of the service time period.
     */
    public LocalDate getEndsOn() {
        return endsOn;
    }

    /**
     * Gets the opening time plan of the service time period.
     *
     * @return The opening time plan of the service time period.
     */
    public Optional<ServiceTimePlanDao> getOpeningTimePlan() {
        return openingTimePlan;
    }

    /**
     * Gets the lunch time plan of the service time period.
     *
     * @return The lunch time plan of the service time period.
     */
    public Optional<ServiceTimePlanDao> getLunchTimePlan() {
        return lunchTimePlan;
    }

    /**
     * Gets the date and time when the service time period was downloaded from the remote server for the last time.
     *
     * @return Date and time when the service time period was updated for the last time.
     */
    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets the version of the service time period that was received from the remote server (a Base64-encoded byte array).
     *
     * @return The version of the service time period.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Converts this instance to {@link ContentValues} that can be used to store this instance in the database.
     *
     * @return ContentValues containing this instance's data.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (id != null) {
            values.put(Id, id);
        } else {
            values.putNull(Id);
        }
        values.put(ServiceTimePeriodId, serviceTimePeriodId);
        values.put(FacilityId, facilityId);
        values.put(StartsOn, startsOn.toString());
        values.put(EndsOn, endsOn.toString());
        values.put(LastUpdate, lastUpdate.toString());
        values.put(Version, version);

        if (openingTimePlan.isPresent() && openingTimePlan.get().getMonday().getFrom().isPresent()) {
            values.put(OpeningTimeStartMonday, openingTimePlan.get().getMonday().getFrom().get().toString());
        } else {
            values.putNull(OpeningTimeStartMonday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getMonday().getUntil().isPresent()) {
            values.put(OpeningTimeEndMonday, openingTimePlan.get().getMonday().getUntil().get().toString());
        } else {
            values.putNull(OpeningTimeEndMonday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getTuesday().getFrom().isPresent()) {
            values.put(OpeningTimeStartTuesday, openingTimePlan.get().getTuesday().getFrom().get().toString());
        } else {
            values.putNull(OpeningTimeStartTuesday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getTuesday().getUntil().isPresent()) {
            values.put(OpeningTimeEndTuesday, openingTimePlan.get().getTuesday().getUntil().get().toString());
        } else {
            values.putNull(OpeningTimeEndTuesday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getWednesday().getFrom().isPresent()) {
            values.put(OpeningTimeStartWednesday, openingTimePlan.get().getWednesday().getFrom().get().toString());
        } else {
            values.putNull(OpeningTimeStartWednesday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getWednesday().getUntil().isPresent()) {
            values.put(OpeningTimeEndWednesday, openingTimePlan.get().getWednesday().getUntil().get().toString());
        } else {
            values.putNull(OpeningTimeEndWednesday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getThursday().getFrom().isPresent()) {
            values.put(OpeningTimeStartThursday, openingTimePlan.get().getThursday().getFrom().get().toString());
        } else {
            values.putNull(OpeningTimeStartThursday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getThursday().getUntil().isPresent()) {
            values.put(OpeningTimeEndThursday, openingTimePlan.get().getThursday().getUntil().get().toString());
        } else {
            values.putNull(OpeningTimeEndThursday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getFriday().getFrom().isPresent()) {
            values.put(OpeningTimeStartFriday, openingTimePlan.get().getFriday().getFrom().get().toString());
        } else {
            values.putNull(OpeningTimeStartFriday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getFriday().getUntil().isPresent()) {
            values.put(OpeningTimeEndFriday, openingTimePlan.get().getFriday().getUntil().get().toString());
        } else {
            values.putNull(OpeningTimeEndFriday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getSaturday().getFrom().isPresent()) {
            values.put(OpeningTimeStartSaturday, openingTimePlan.get().getSaturday().getFrom().get().toString());
        } else {
            values.putNull(OpeningTimeStartSaturday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getSaturday().getUntil().isPresent()) {
            values.put(OpeningTimeEndSaturday, openingTimePlan.get().getSaturday().getUntil().get().toString());
        } else {
            values.putNull(OpeningTimeEndSaturday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getSunday().getFrom().isPresent()) {
            values.put(OpeningTimeStartSunday, openingTimePlan.get().getSunday().getFrom().get().toString());
        } else {
            values.putNull(OpeningTimeStartSunday);
        }
        if (openingTimePlan.isPresent() && openingTimePlan.get().getSunday().getUntil().isPresent()) {
            values.put(OpeningTimeEndSunday, openingTimePlan.get().getSunday().getUntil().get().toString());
        } else {
            values.putNull(OpeningTimeEndSunday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getMonday().getFrom().isPresent()) {
            values.put(LunchTimeStartMonday, lunchTimePlan.get().getMonday().getFrom().get().toString());
        } else {
            values.putNull(LunchTimeStartMonday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getMonday().getUntil().isPresent()) {
            values.put(LunchTimeEndMonday, lunchTimePlan.get().getMonday().getUntil().get().toString());
        } else {
            values.putNull(LunchTimeEndMonday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getTuesday().getFrom().isPresent()) {
            values.put(LunchTimeStartTuesday, lunchTimePlan.get().getTuesday().getFrom().get().toString());
        } else {
            values.putNull(LunchTimeStartTuesday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getTuesday().getUntil().isPresent()) {
            values.put(LunchTimeEndTuesday, lunchTimePlan.get().getTuesday().getUntil().get().toString());
        } else {
            values.putNull(LunchTimeEndTuesday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getWednesday().getFrom().isPresent()) {
            values.put(LunchTimeStartWednesday, lunchTimePlan.get().getWednesday().getFrom().get().toString());
        } else {
            values.putNull(LunchTimeStartWednesday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getWednesday().getUntil().isPresent()) {
            values.put(LunchTimeEndWednesday, lunchTimePlan.get().getWednesday().getUntil().get().toString());
        } else {
            values.putNull(LunchTimeEndWednesday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getThursday().getFrom().isPresent()) {
            values.put(LunchTimeStartThursday, lunchTimePlan.get().getThursday().getFrom().get().toString());
        } else {
            values.putNull(LunchTimeStartThursday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getThursday().getUntil().isPresent()) {
            values.put(LunchTimeEndThursday, lunchTimePlan.get().getThursday().getUntil().get().toString());
        } else {
            values.putNull(LunchTimeEndThursday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getFriday().getFrom().isPresent()) {
            values.put(LunchTimeStartFriday, lunchTimePlan.get().getFriday().getFrom().get().toString());
        } else {
            values.putNull(LunchTimeStartFriday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getFriday().getUntil().isPresent()) {
            values.put(LunchTimeEndFriday, lunchTimePlan.get().getFriday().getUntil().get().toString());
        } else {
            values.putNull(LunchTimeEndFriday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getSaturday().getFrom().isPresent()) {
            values.put(LunchTimeStartSaturday, lunchTimePlan.get().getSaturday().getFrom().get().toString());
        } else {
            values.putNull(LunchTimeStartSaturday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getSaturday().getUntil().isPresent()) {
            values.put(LunchTimeEndSaturday, lunchTimePlan.get().getSaturday().getUntil().get().toString());
        } else {
            values.putNull(LunchTimeEndSaturday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getSunday().getFrom().isPresent()) {
            values.put(LunchTimeStartSunday, lunchTimePlan.get().getSunday().getFrom().get().toString());
        } else {
            values.putNull(LunchTimeStartSunday);
        }
        if (lunchTimePlan.isPresent() && lunchTimePlan.get().getSunday().getUntil().isPresent()) {
            values.put(LunchTimeEndSunday, lunchTimePlan.get().getSunday().getUntil().get().toString());
        } else {
            values.putNull(LunchTimeEndSunday);
        }

        return values;
    }

    /**
     * Tests whether the service time period is valid on the given date. Returns {@code true} if it is, false otherwise.
     *
     * @param date Date to compare to.
     * @return True if service time period is valid on the given date.
     */
    public boolean isValidOn(ReadablePartial date) {
        return (startsOn.isEqual(date) || startsOn.isBefore(date)) && (endsOn.isAfter(date) || endsOn.isEqual(date));
    }

    /**
     * Returns the opening time start for the given week day. If no opening time is defined,
     * {@link com.google.common.base.Optional#absent()} is returned. The numbering of the weekdays is the same as in
     * {@link DateTimeConstants}.
     *
     * @param dayOfWeek Day of week.
     * @return Opening time start (if any).
     */
    public Optional<LocalTime> getOpeningTimeStartForWeekday(int dayOfWeek) {
        if (!openingTimePlan.isPresent()) {
            return Optional.absent();
        }

        switch (dayOfWeek) {
            case DateTimeConstants.MONDAY:
                return openingTimePlan.get().getMonday().getFrom();
            case DateTimeConstants.TUESDAY:
                return openingTimePlan.get().getTuesday().getFrom();
            case DateTimeConstants.WEDNESDAY:
                return openingTimePlan.get().getWednesday().getFrom();
            case DateTimeConstants.THURSDAY:
                return openingTimePlan.get().getThursday().getFrom();
            case DateTimeConstants.FRIDAY:
                return openingTimePlan.get().getFriday().getFrom();
            case DateTimeConstants.SATURDAY:
                return openingTimePlan.get().getSaturday().getFrom();
            case DateTimeConstants.SUNDAY:
                return openingTimePlan.get().getSunday().getFrom();
        }

        return Optional.absent();
    }

    /**
     * Returns the opening time end for the given week day. If no opening time is defined,
     * {@link com.google.common.base.Optional#absent()} is returned. The numbering of the weekdays is the same as in
     * {@link DateTimeConstants}.
     *
     * @param dayOfWeek Day of week.
     * @return Opening time end (if any).
     */
    public Optional<LocalTime> getOpeningTimeEndForWeekday(int dayOfWeek) {
        if (!openingTimePlan.isPresent()) {
            return Optional.absent();
        }

        switch (dayOfWeek) {
            case DateTimeConstants.MONDAY:
                return openingTimePlan.get().getMonday().getUntil();
            case DateTimeConstants.TUESDAY:
                return openingTimePlan.get().getTuesday().getUntil();
            case DateTimeConstants.WEDNESDAY:
                return openingTimePlan.get().getWednesday().getUntil();
            case DateTimeConstants.THURSDAY:
                return openingTimePlan.get().getThursday().getUntil();
            case DateTimeConstants.FRIDAY:
                return openingTimePlan.get().getFriday().getUntil();
            case DateTimeConstants.SATURDAY:
                return openingTimePlan.get().getSaturday().getUntil();
            case DateTimeConstants.SUNDAY:
                return openingTimePlan.get().getSunday().getUntil();
        }

        return Optional.absent();
    }

    /**
     * Returns the lunch time start for the given week day. If no lunch time is defined,
     * {@link com.google.common.base.Optional#absent()} is returned. The numbering of the weekdays is the same as in
     * {@link DateTimeConstants}.
     *
     * @param dayOfWeek Day of week.
     * @return Lunch time start (if any).
     */
    public Optional<LocalTime> getLunchTimeStartForWeekday(int dayOfWeek) {
        if (!lunchTimePlan.isPresent()) {
            return Optional.absent();
        }

        switch (dayOfWeek) {
            case DateTimeConstants.MONDAY:
                return lunchTimePlan.get().getMonday().getFrom();
            case DateTimeConstants.TUESDAY:
                return lunchTimePlan.get().getTuesday().getFrom();
            case DateTimeConstants.WEDNESDAY:
                return lunchTimePlan.get().getWednesday().getFrom();
            case DateTimeConstants.THURSDAY:
                return lunchTimePlan.get().getThursday().getFrom();
            case DateTimeConstants.FRIDAY:
                return lunchTimePlan.get().getFriday().getFrom();
            case DateTimeConstants.SATURDAY:
                return lunchTimePlan.get().getSaturday().getFrom();
            case DateTimeConstants.SUNDAY:
                return lunchTimePlan.get().getSunday().getFrom();
        }

        return Optional.absent();
    }

    /**
     * Returns the lunch time end for the given week day. If no lunch time is defined,
     * {@link com.google.common.base.Optional#absent()} is returned. The numbering of the weekdays is the same as in
     * {@link DateTimeConstants}.
     *
     * @param dayOfWeek Day of week.
     * @return Lunch time end (if any).
     */
    public Optional<LocalTime> getLunchTimeEndForWeekday(int dayOfWeek) {
        if (!lunchTimePlan.isPresent()) {
            return Optional.absent();
        }

        switch (dayOfWeek) {
            case DateTimeConstants.MONDAY:
                return lunchTimePlan.get().getMonday().getUntil();
            case DateTimeConstants.TUESDAY:
                return lunchTimePlan.get().getTuesday().getUntil();
            case DateTimeConstants.WEDNESDAY:
                return lunchTimePlan.get().getWednesday().getUntil();
            case DateTimeConstants.THURSDAY:
                return lunchTimePlan.get().getThursday().getUntil();
            case DateTimeConstants.FRIDAY:
                return lunchTimePlan.get().getFriday().getUntil();
            case DateTimeConstants.SATURDAY:
                return lunchTimePlan.get().getSaturday().getUntil();
            case DateTimeConstants.SUNDAY:
                return lunchTimePlan.get().getSunday().getUntil();
        }

        return Optional.absent();
    }

    public Optional<LocalTime> getHolidayAdjustedOpeningTimeStart(LocalDate day, HolidayDao holidayDao) {
        Optional<LocalTime> optionalStart = getOpeningTimeStartForWeekday(day.getDayOfWeek());
        Optional<LocalTime> optionalEnd = getOpeningTimeEndForWeekday(day.getDayOfWeek());

        if (!optionalStart.isPresent() || !optionalEnd.isPresent()) {
            return Optional.absent();
        }

        LocalDateTime holidayStart = holidayDao.getStartsAt();
        LocalDateTime holidayEnd = holidayDao.getEndsAt();
        LocalDateTime openingTimeStart = day.toLocalDateTime(optionalStart.get());
        LocalDateTime openingTimeEnd = day.toLocalDateTime(optionalEnd.get());

        // None if holiday spans full opening time
        if ((holidayStart.isBefore(openingTimeStart) || holidayStart.isEqual(openingTimeStart))
                && holidayEnd.isAfter(openingTimeEnd)) {
            return Optional.absent();
        }

        // Later if holiday ends after opening but before closing
        if (day.isEqual(holidayEnd.toLocalDate()) && holidayEnd.isAfter(openingTimeStart)
                && holidayEnd.isBefore(openingTimeEnd)) {
            return Optional.of(holidayEnd.toLocalTime());
        }

        return optionalStart;
    }

    public Optional<LocalTime> getHolidayAdjustedOpeningTimeEnd(LocalDate day, HolidayDao holidayDao) {
        Optional<LocalTime> optionalStart = getOpeningTimeStartForWeekday(day.getDayOfWeek());
        Optional<LocalTime> optionalEnd = getOpeningTimeEndForWeekday(day.getDayOfWeek());

        if (!optionalStart.isPresent() || !optionalEnd.isPresent()) {
            return Optional.absent();
        }

        LocalDateTime holidayStart = holidayDao.getStartsAt();
        LocalDateTime holidayEnd = holidayDao.getEndsAt();
        LocalDateTime openingTimeStart = day.toLocalDateTime(optionalStart.get());
        LocalDateTime openingTimeEnd = day.toLocalDateTime(optionalEnd.get());

        // // None if holiday spans full opening time
        if ((holidayStart.isBefore(openingTimeStart) || holidayStart.isEqual(openingTimeStart))
                && holidayEnd.isAfter(openingTimeEnd)) {
            return Optional.absent();
        }

        // Earlier if holidays starts before closing
        if (day.isEqual(holidayStart.toLocalDate()) && holidayStart.isBefore(openingTimeEnd)) {
            return Optional.of(holidayStart.toLocalTime());
        }

        return optionalEnd;
    }

    public Optional<LocalTime> getHolidayAdjustedLunchTimeStart(LocalDate day, HolidayDao holidayDao) {
        Optional<LocalTime> optionalStart = getLunchTimeStartForWeekday(day.getDayOfWeek());
        Optional<LocalTime> optionalEnd = getLunchTimeEndForWeekday(day.getDayOfWeek());

        if (!optionalStart.isPresent() || !optionalEnd.isPresent()) {
            return Optional.absent();
        }

        LocalDateTime holidayStart = holidayDao.getStartsAt();
        LocalDateTime holidayEnd = holidayDao.getEndsAt();
        LocalDateTime lunchTimeStart = day.toLocalDateTime(optionalStart.get());
        LocalDateTime lunchTimeEnd = day.toLocalDateTime(optionalEnd.get());

        // None if holiday spans full lunch time
        if ((holidayStart.isBefore(lunchTimeStart) || holidayStart.isEqual(lunchTimeStart))
                && holidayEnd.isAfter(lunchTimeEnd)) {
            return Optional.absent();
        }

        // Later if holiday ends after lunch but before closing
        if (day.isEqual(holidayEnd.toLocalDate()) && holidayEnd.isAfter(lunchTimeStart)
                && holidayEnd.isBefore(lunchTimeEnd)) {
            return Optional.of(holidayEnd.toLocalTime());
        }

        return optionalStart;
    }

    public Optional<LocalTime> getHolidayAdjustedLunchTimeEnd(LocalDate day, HolidayDao holidayDao) {
        Optional<LocalTime> optionalStart = getLunchTimeStartForWeekday(day.getDayOfWeek());
        Optional<LocalTime> optionalEnd = getLunchTimeEndForWeekday(day.getDayOfWeek());

        if (!optionalStart.isPresent() || !optionalEnd.isPresent()) {
            return Optional.absent();
        }

        LocalDateTime holidayStart = holidayDao.getStartsAt();
        LocalDateTime holidayEnd = holidayDao.getEndsAt();
        LocalDateTime lunchTimeStart = day.toLocalDateTime(optionalStart.get());
        LocalDateTime lunchTimeEnd = day.toLocalDateTime(optionalEnd.get());

        // // None if holiday spans full lunch time
        if ((holidayStart.isBefore(lunchTimeStart) || holidayStart.isEqual(lunchTimeStart))
                && holidayEnd.isAfter(lunchTimeEnd)) {
            return Optional.absent();
        }

        // Earlier if holidays starts before closing
        if (day.isEqual(holidayStart.toLocalDate()) && holidayStart.isBefore(lunchTimeEnd)) {
            return Optional.of(holidayStart.toLocalTime());
        }

        return optionalEnd;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("id", id).
                add("serviceTimePeriodId", serviceTimePeriodId).
                add("facilityId", facilityId).
                add("startsOn", startsOn).
                add("endsOn", endsOn).
                add("openingTimePlan", openingTimePlan).
                add("lunchTimePlan", lunchTimePlan).
                add("lastUpdate", lastUpdate).
                add("version", version).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, serviceTimePeriodId, facilityId, startsOn, endsOn, openingTimePlan, lunchTimePlan,
                lastUpdate, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ServiceTimePeriodDao other = (ServiceTimePeriodDao) obj;

        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.serviceTimePeriodId, other.serviceTimePeriodId) &&
                Objects.equal(this.facilityId, other.facilityId) &&
                Objects.equal(this.startsOn, other.startsOn) &&
                Objects.equal(this.endsOn, other.endsOn) &&
                Objects.equal(this.openingTimePlan, other.openingTimePlan) &&
                Objects.equal(this.lunchTimePlan, other.lunchTimePlan) &&
                Objects.equal(this.lastUpdate, other.lastUpdate) &&
                Objects.equal(this.version, other.version);
    }
}
