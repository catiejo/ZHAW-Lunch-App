package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.base.Objects;
import org.joda.time.DateTime;

import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.GastronomicFacilityInfo;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a gastronomic facility. Every instance is immutable once created.
 */
public class GastronomicFacilityDao {

    private final Long id;
    private final Long facilityId;
    private final Type type;
    public final String name;
    public final String location;
    public final DateTime lastUpdate;
    private final String version;

    /**
     * Creates new instance using the data supplied by the given cursor.
     *
     * @param cursor Cursor to extract the data from.
     * @return New gastronomic facility instance.
     */
    public static GastronomicFacilityDao fromCursor(Cursor cursor) {
        Long id = cursor.getLong(cursor.getColumnIndex(GastronomicFacilityInfo.Id));
        Long facilityId = cursor.getLong(cursor.getColumnIndex(GastronomicFacilityInfo.FacilityId));
        Type type = Type.valueOf(cursor.getString(cursor.getColumnIndex(GastronomicFacilityInfo.Type)));
        String name = cursor.getString(cursor.getColumnIndex(GastronomicFacilityInfo.Name));
        String location = cursor.getString(cursor.getColumnIndex(GastronomicFacilityInfo.Location));
        DateTime lastUpdate = DateTime.parse(cursor.getString(cursor.getColumnIndex(GastronomicFacilityInfo.LastUpdate)));
        String version = cursor.getString(cursor.getColumnIndex(GastronomicFacilityInfo.Version));

        return new GastronomicFacilityDao(id, facilityId, type, name, location, lastUpdate, version);
    }

    /**
     * Creates new immutable instance.
     *
     * @param id         The SQLite row id of the gastronomic facility.
     * @param facilityId         ID of the gastronomic facility.
     * @param type       Type of the gastronomic facility.
     * @param name       Name of the gastronomic facility.
     * @param location   Location of the gastronomic facility.
     * @param lastUpdate Date when the gastronomic facility was updated for the last time.
     * @param version    Version of the gastronomic facility.
     */
    public GastronomicFacilityDao(Long id, Long facilityId, Type type, String name, String location,
                                  DateTime lastUpdate, String version) {
        this.id = id;
        this.facilityId = checkNotNull(facilityId);
        this.type = checkNotNull(type);
        this.name = checkNotNull(name);
        this.location = checkNotNull(location);
        this.lastUpdate = checkNotNull(lastUpdate);
        this.version = checkNotNull(version);
    }

    /**
     * Gets the SQLite rowid of this gastronomic facility.
     *
     * @return The SQLite rowid of this gastronomic facility.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the ID of this gastronomic facility.
     *
     * @return The ID of this gastronomic facility.
     */
    public Long getFacilityId() {
        return facilityId;
    }

    /**
     * Gets the type of this gastronomic facility.
     *
     * @return The type of this gastronomic facility.
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the name of this gastronomic facility.
     *
     * @return The name of this gastronomic facility.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the name of the location of this gastronomic facility.
     *
     * @return The name of the location of this gastronomic facility.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the date when this gastronomic facility was retrieved from the remote server.
     *
     * @return Date of the last update.
     */
    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets the version of the gastronomic facility that was received from the remote server
     * (a Base64-encoded byte array).
     *
     * @return The version of this gastronomic facility.
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
            values.put(GastronomicFacilityInfo.Id, id);
        } else {
            values.putNull(GastronomicFacilityInfo.Id);
        }
        values.put(GastronomicFacilityInfo.FacilityId, facilityId);
        values.put(GastronomicFacilityInfo.Type, type.name());
        values.put(GastronomicFacilityInfo.Name, name);
        values.put(GastronomicFacilityInfo.Location, location);
        values.put(GastronomicFacilityInfo.LastUpdate, lastUpdate.toString());
        values.put(GastronomicFacilityInfo.Version, version);
        return values;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, facilityId, type, name, location, lastUpdate, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final GastronomicFacilityDao other = (GastronomicFacilityDao) obj;

        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.facilityId, other.facilityId) &&
                Objects.equal(this.type, other.type) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.location, other.location) &&
                Objects.equal(this.lastUpdate, other.lastUpdate) &&
                Objects.equal(this.version, other.version);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("id", id).
                add("facilityId", facilityId).
                add("type", type).
                add("name", name).
                add("location", location).
                add("lastUpdate", lastUpdate).
                add("version", version).
                toString();
    }

    public enum Type {
        /**
         * Denotes the gastronomic facility as a cafeteria.
         */
        Cafeteria,

        /**
         * Denotes the gastronomic facility as a canteen.
         */
        Canteen
    }
}
