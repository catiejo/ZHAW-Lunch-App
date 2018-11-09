package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.base.Objects;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.ReadablePartial;

import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.HolidayInfo;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a holiday. Every instance is immutable once created.
 */
public class HolidayDao {

    private final Long id;
    private final Long holidayId;
    private final Long facilityId;
    private final String name;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final DateTime lastUpdate;
    private final String version;

    /**
     * Creates new instance using the data supplied by the given cursor.
     *
     * @param cursor Cursor to extract the data from.
     * @return New holiday instance.
     */
    public static HolidayDao fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(HolidayInfo.Id));
        long holidayId = cursor.getLong(cursor.getColumnIndex(HolidayInfo.HolidayId));
        long facility = cursor.getLong(cursor.getColumnIndex(HolidayInfo.FacilityId));
        String name = cursor.getString(cursor.getColumnIndex(HolidayInfo.Name));
        LocalDateTime startsAt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(HolidayInfo.StartsAt)));
        LocalDateTime endsAt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(HolidayInfo.EndsAt)));
        DateTime lastUpdate = DateTime.parse(cursor.getString(cursor.getColumnIndex(HolidayInfo.LastUpdate)));
        String version = cursor.getString(cursor.getColumnIndex(HolidayInfo.Version));

        return new HolidayDao(id, holidayId, facility, name, startsAt, endsAt, lastUpdate, version);
    }

    /**
     * Creates new immutable instance.
     *
     * @param id         The SQLite rowid of the holiday.
     * @param holidayId  ID of the holiday.
     * @param facilityId ID of the gastronomic facility this holiday is assigned to.
     * @param name       Name of the holiday.
     * @param startsAt   Date and time when the holiday starts.
     * @param endsAt     Date and time when the holiday ends.
     * @param lastUpdate Date and time when the holiday was updated the last time.
     * @param version    Version of the holiday.
     */
    public HolidayDao(Long id, Long holidayId, Long facilityId, String name, LocalDateTime startsAt, LocalDateTime endsAt,
                      DateTime lastUpdate, String version) {
        this.id = id;
        this.holidayId = checkNotNull(holidayId);
        this.facilityId = checkNotNull(facilityId);
        this.name = checkNotNull(name);
        this.startsAt = checkNotNull(startsAt);
        this.endsAt = checkNotNull(endsAt);
        this.lastUpdate = checkNotNull(lastUpdate);
        this.version = checkNotNull(version);
    }

    /**
     * Gets the SQLite rowid of the holiday.
     *
     * @return The SQLite rowid of the holiday.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the ID of the holiday.
     *
     * @return The ID of the holiday.
     */
    public Long getHolidayId() {
        return holidayId;
    }

    /**
     * Gets the ID of the gastronomic facility this holiday is assigned to.
     *
     * @return The ID of the gastronomic facility.
     */
    public Long getFacilityId() {
        return facilityId;
    }

    /**
     * Gets the name of the holiday.
     *
     * @return The name of the holiday.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the date and time when the holiday starts. The duration is defined as:
     * {@code startsAt <= holiday < EndsAt }.
     *
     * @return The date and time when the holiday starts.
     */
    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    /**
     * Gets the date and time when the holiday ends. The duration is defined as:
     * {@code startsAt <= holiday < EndsAt }.
     *
     * @return The date and time when the holiday starts.
     */
    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    /**
     * Gets the date and time when the holiday was downloaded from the remote server for the last time.
     *
     * @return Date and time when the holiday was updated for the last time.
     */
    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets the version of the holiday that was received from the remote server (a Base64-encoded byte array).
     *
     * @return The version of the holiday.
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
            values.put(HolidayInfo.Id, id);
        } else {
            values.putNull(HolidayInfo.Id);
        }
        values.put(HolidayInfo.HolidayId, holidayId);
        values.put(HolidayInfo.FacilityId, facilityId);
        values.put(HolidayInfo.Name, name);
        values.put(HolidayInfo.StartsAt, startsAt.toString());
        values.put(HolidayInfo.EndsAt, endsAt.toString());
        values.put(HolidayInfo.LastUpdate, lastUpdate.toString());
        values.put(HolidayInfo.Version, version);
        return values;
    }

    /**
     * Tests whether the holiday falls on the day with the given date. Returns true if it does, false otherwise.
     *
     * @param date Reference date.
     * @return True if the holiday falls on the day with the given date, false otherwise.
     */
    public boolean isValidOn(ReadablePartial date) {
        return (getStartsAt().toLocalDate().isEqual(date) || getStartsAt().toLocalDate().isBefore(date)) &&
                getEndsAt().toLocalDate().isAfter(date);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, holidayId, facilityId, name, startsAt, endsAt, lastUpdate, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final HolidayDao other = (HolidayDao) obj;
        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.holidayId, other.holidayId) &&
                Objects.equal(this.facilityId, other.facilityId) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.startsAt, other.startsAt) &&
                Objects.equal(this.endsAt, other.endsAt) &&
                Objects.equal(this.lastUpdate, other.lastUpdate) &&
                Objects.equal(this.version, other.version);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("id", id).
                add("holidayId", holidayId).
                add("facilityId", facilityId).
                add("name", name).
                add("startsAt", startsAt).
                add("endsAt", endsAt).
                add("lastUpdate", lastUpdate).
                add("version", version).
                toString();
    }
}
