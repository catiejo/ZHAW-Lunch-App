package com.zhaw.catiejo.whatsforlunch.mensadata.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.io.BaseEncoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data Transfer Object to transmit a GastronomicFacility over the wire.
 */
public class GastronomicFacilityDto {

    @JsonDeserialize(as = ArrayList.class)
    private final ImmutableList<HolidayDto> holidays;

    private final long id;

    private final LocationDto location;

    private final String name;

    @JsonDeserialize(as = ArrayList.class)
    private final ImmutableList<ServiceTimePeriodDto> serviceTimePeriods;

    private final String type;

    private final String version;

    @JsonCreator
    public GastronomicFacilityDto(@JsonProperty("id") long id,
                                  @JsonProperty("type") String type,
                                  @JsonProperty("name") String name,
                                  @JsonProperty("location") LocationDto location,
                                  @JsonProperty("holidays") List<HolidayDto> holidays,
                                  @JsonProperty("serviceTimePeriods") List<ServiceTimePeriodDto> serviceTimePeriods,
                                  @JsonProperty("version") String version) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.location = location;
        this.holidays = ImmutableList.copyOf(holidays);
        this.serviceTimePeriods = ImmutableList.copyOf(serviceTimePeriods);
        this.version = version;
    }

    /**
     * Gets the list of holidays of this GastronomicFacility.
     *
     * @return List of Holidays.
     */
    public ImmutableList<HolidayDto> getHolidays() {
        return holidays;
    }

    /**
     * Gets the ID of this GastronomicFacility (unique, issued by the remote database).
     *
     * @return The gastronomic facility's ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the Location of this GastronomicFacility.
     *
     * @return The location.
     */
    public LocationDto getLocation() {
        return location;
    }

    /**
     * Gets the name of this GastronomicFacility (like "Tössfeld").
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of ServiceTimePeriods of this GastronomicFacility.
     *
     * @return The list of ServiceTimePeriods.
     */
    public ImmutableList<ServiceTimePeriodDto> getServiceTimePeriods() {
        return serviceTimePeriods;
    }

    /**
     * Gets the type of the GastronomicFacility (like "Canteen" or "Cafeteria").
     *
     * @return The type.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the version of this GastronomicFacility.
     *
     * @return The version of this GastronomicFacility.
     */
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("holidays", holidays).
                add("id", id).
                add("location", location).
                add("name", name).
                add("serviceTimePeriods", serviceTimePeriods).
                add("type", type).
                add("version", version).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(holidays, id, location, name, serviceTimePeriods, type, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final GastronomicFacilityDto other = (GastronomicFacilityDto) obj;
        return Objects.equal(this.holidays, other.holidays) &&
                Objects.equal(this.id, other.id) &&
                Objects.equal(this.location, other.location) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.serviceTimePeriods, other.serviceTimePeriods) &&
                Objects.equal(this.type, other.type) &&
                Objects.equal(this.version, other.version);
    }
}
