package com.zhaw.catiejo.whatsforlunch.mensadata.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.joda.time.LocalTime;

/**
 * Data Transfer Object to transmit a ServiceTime over the wire.
 */
public class ServiceTimeDto {

    private final String type;

    private final LocalTime from;

    private final LocalTime until;

    @JsonCreator
    public ServiceTimeDto(@JsonProperty("type") String type,
                          @JsonProperty("from") LocalTime from,
                          @JsonProperty("until") LocalTime until) {
        this.type = type;
        this.from = from;
        this.until = until;
    }

    /**
     * Gets the type of this ServiceTime (like "OpeningTime" or "LunchTime").
     *
     * @return The type of this ServiceTime.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the start time of this ServiceTime. The duration is defined as: {@code from <= duration < until}.
     *
     * @return The start time of this ServiceTime.
     */
    public LocalTime getFrom() {
        return from;
    }

    /**
     * Gets the end time of this ServiceTime.  The duration is defined as: {@code from <= duration < until}.
     *
     * @return The end time of this ServiceTime.
     */
    public LocalTime getUntil() {
        return until;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, from, until);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ServiceTimeDto other = (ServiceTimeDto) obj;

        return Objects.equal(this.type, other.type) &&
                Objects.equal(this.from, other.from) &&
                Objects.equal(this.until, other.until);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("type", type).
                add("from", from).
                add("until", until).
                toString();
    }
}
