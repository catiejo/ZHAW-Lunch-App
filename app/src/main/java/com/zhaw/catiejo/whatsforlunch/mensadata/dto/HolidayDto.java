package com.zhaw.catiejo.whatsforlunch.mensadata.dto;

import com.zhaw.catiejo.whatsforlunch.mensadata.helper.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

/**
 * Data Transfer Object to transmit a Holiday over the wire.
 */
public class HolidayDto {

    private final LocalDateTime endsAt;

    private final long id;

    private final String name;

    private final LocalDateTime startsAt;

    private final String version;

    @JsonCreator
    public HolidayDto(@JsonProperty("id") long id,
                      @JsonProperty("name") String name,
                      @JsonProperty("startsAt") DateTime startsAt,
                      @JsonProperty("endsAt") DateTime endsAt,
                      @JsonProperty("version") String version) {
        this.id = id;
        this.name = name;
        this.startsAt = startsAt.toDateTime(Constants.LocalTimeZone).toLocalDateTime();
        this.endsAt = endsAt.toDateTime(Constants.LocalTimeZone).toLocalDateTime();
        this.version = version;
    }

    /**
     * Gets the end date of the Holiday. The duration of the Holiday is defined as:
     * {@code startsAt <= holiday < EndsAt }.
     *
     * @return The end date of the Holiday.
     */
    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    /**
     * Gets the ID of the Holiday (unique, issued by the remote database).
     *
     * @return The Dish ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the Holiday (like "Christmas").
     *
     * @return The name of the Holiday.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the start date of the Holiday. The duration of the Holiday is defined as:
     * {@code startsAt <= holiday < EndsAt }.
     *
     * @return The start date of the Holiday.
     */
    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    /**
     * Gets the current version of this Holiday.
     *
     * @return The version of this Holiday.
     */
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("endsAt", endsAt).
                add("id", id).
                add("name", name).
                add("startsAt", startsAt).
                add("version", version).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(endsAt, id, name, startsAt, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final HolidayDto other = (HolidayDto) obj;

        return Objects.equal(this.endsAt, other.endsAt) &&
                Objects.equal(this.id, other.id) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.startsAt, other.startsAt) &&
                Objects.equal(this.version, other.version);
    }
}
