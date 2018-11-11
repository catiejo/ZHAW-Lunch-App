package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Data Transfer Object to transmit a ServiceTimePlan over the wire.
 */
public class ServiceTimePlanDto {

    private final ServiceTimeDto friday;

    private final ServiceTimeDto monday;

    private final ServiceTimeDto saturday;

    private final ServiceTimeDto sunday;

    private final ServiceTimeDto thursday;

    private final ServiceTimeDto tuesday;

    private final String type;

    private final ServiceTimeDto wednesday;

    @JsonCreator
    public ServiceTimePlanDto(@JsonProperty("type") String type,
                              @JsonProperty("monday") ServiceTimeDto monday,
                              @JsonProperty("tuesday") ServiceTimeDto tuesday,
                              @JsonProperty("wednesday") ServiceTimeDto wednesday,
                              @JsonProperty("thursday") ServiceTimeDto thursday,
                              @JsonProperty("friday") ServiceTimeDto friday,
                              @JsonProperty("saturday") ServiceTimeDto saturday,
                              @JsonProperty("sunday") ServiceTimeDto sunday) {
        this.type = type;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    /**
     * Gets the ServiceTime for Friday.
     *
     * @return The ServiceTime for Friday.
     */
    public ServiceTimeDto getFriday() {
        return friday;
    }

    /**
     * Gets the ServiceTime for Monday.
     *
     * @return The ServiceTime for Monday.
     */
    public ServiceTimeDto getMonday() {
        return monday;
    }

    /**
     * Gets the ServiceTime for Saturday.
     *
     * @return The ServiceTime for Saturday.
     */
    public ServiceTimeDto getSaturday() {
        return saturday;
    }

    /**
     * Gets the ServiceTime for Sunday.
     *
     * @return The ServiceTime for Sunday.
     */
    public ServiceTimeDto getSunday() {
        return sunday;
    }

    /**
     * Gets the ServiceTime for Thursday.
     *
     * @return The ServiceTime for Thursday.
     */
    public ServiceTimeDto getThursday() {
        return thursday;
    }

    /**
     * Gets the ServiceTime for Tuesday.
     *
     * @return The ServiceTime for Tuesday.
     */
    public ServiceTimeDto getTuesday() {
        return tuesday;
    }

    /**
     * Returns the type of this ServiceTimePlan (like "OpeningTimePlan" or "LunchTimePlan").
     *
     * @return The type of this ServiceTimePlan.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the ServiceTime for Wednesday.
     *
     * @return The ServiceTime for Wednesday.
     */
    public ServiceTimeDto getWednesday() {
        return wednesday;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(friday, monday, saturday, sunday, thursday, tuesday, type, wednesday);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ServiceTimePlanDto other = (ServiceTimePlanDto) obj;

        return Objects.equal(this.friday, other.friday) &&
                Objects.equal(this.monday, other.monday) &&
                Objects.equal(this.saturday, other.saturday) &&
                Objects.equal(this.sunday, other.sunday) &&
                Objects.equal(this.thursday, other.thursday) &&
                Objects.equal(this.tuesday, other.tuesday) &&
                Objects.equal(this.type, other.type) &&
                Objects.equal(this.wednesday, other.wednesday);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("friday", friday).
                add("monday", monday).
                add("saturday", saturday).
                add("sunday", sunday).
                add("thursday", thursday).
                add("tuesday", tuesday).
                add("type", type).
                add("wednesday", wednesday).
                toString();
    }
}
