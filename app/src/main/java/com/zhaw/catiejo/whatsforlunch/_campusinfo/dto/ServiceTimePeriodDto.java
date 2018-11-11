package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Data transfer object to send a ServiceTimePeriod over the wire.
 */
public class ServiceTimePeriodDto {

    private final LocalDate endsOn;

    private final long id;

    private final ServiceTimePlanDto lunchTimePlan;

    private final ServiceTimePlanDto openingTimePlan;

    private final LocalDate startsOn;

    private final String version;

    @JsonCreator
    public ServiceTimePeriodDto(@JsonProperty("id") long id,
                                @JsonProperty("startsOn") DateTime startsOn,
                                @JsonProperty("endsOn") DateTime endsOn,
                                @JsonProperty("openingTimePlan") ServiceTimePlanDto openingTimePlan,
                                @JsonProperty("lunchTimePlan") ServiceTimePlanDto lunchTimePlan,
                                @JsonProperty("version") String version) {
        this.id = id;
        this.startsOn = (startsOn != null) ? startsOn.toDateTime(Constants.LocalTimeZone).toLocalDate() : null;
        this.endsOn = (endsOn != null) ? endsOn.toDateTime(Constants.LocalTimeZone).toLocalDate() : null;
        this.openingTimePlan = openingTimePlan;
        this.lunchTimePlan = lunchTimePlan;
        this.version = version;
    }

    /**
     * Gets the end date of the ServiceTimePeriod. The duration is defined as: {@code startsOn <= duration <= endsOn}.
     *
     * @return The end date.
     */
    public LocalDate getEndsOn() {
        return endsOn;
    }

    /**
     * Gets the ID of the ServiceTimePeriod (unique, issued by the remote database).
     *
     * @return The ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the LunchTimePlan.
     *
     * @return The lunch time plan.
     */
    public ServiceTimePlanDto getLunchTimePlan() {
        return lunchTimePlan;
    }

    /**
     * Gets the OpeningTimePlan.
     *
     * @return The opening time plan.
     */
    public ServiceTimePlanDto getOpeningTimePlan() {
        return openingTimePlan;
    }

    /**
     * Gets the start date of the ServiceTimePeriod. The duration is defined as: {@code startsOn <= duration <= endsOn}.
     *
     * @return The start date.
     */
    public LocalDate getStartsOn() {
        return startsOn;
    }

    /**
     * Gets the version of the ServiceTimePeriod (a Base64-encoded byte array).
     *
     * @return The version of the ServiceTimePeriod.
     */
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("endsOn", endsOn).
                add("id", id).
                add("lunchTimePlan", lunchTimePlan).
                add("openingTimePlan", openingTimePlan).
                add("startsOn", startsOn).
                add("version", version).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(endsOn, id, lunchTimePlan, openingTimePlan, startsOn, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ServiceTimePeriodDto other = (ServiceTimePeriodDto) obj;
        return Objects.equal(this.endsOn, other.endsOn) &&
                Objects.equal(this.id, other.id) &&
                Objects.equal(this.lunchTimePlan, other.lunchTimePlan) &&
                Objects.equal(this.openingTimePlan, other.openingTimePlan) &&
                Objects.equal(this.startsOn, other.startsOn) &&
                Objects.equal(this.version, other.version);
    }
}
