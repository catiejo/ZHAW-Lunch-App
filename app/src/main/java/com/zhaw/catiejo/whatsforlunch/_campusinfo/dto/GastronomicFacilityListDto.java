package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object to transmit a list of GastronomicFacility objects over the wire.
 */
public class GastronomicFacilityListDto {

    @JsonDeserialize(as = ArrayList.class)
    private final ImmutableList<GastronomicFacilityDto> gastronomicFacilities;

    @JsonCreator
    public GastronomicFacilityListDto(@JsonProperty("gastronomicFacilities") List<GastronomicFacilityDto> gastronomicFacilities) {
        this.gastronomicFacilities = ImmutableList.copyOf(gastronomicFacilities);
    }

    /**
     * Gets the list of GastronomicFacility objects.
     *
     * @return The list of gastronomic facilities.
     */
    public ImmutableList<GastronomicFacilityDto> getGastronomicFacilities() {
        return gastronomicFacilities;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("gastronomicFacilities", gastronomicFacilities).
                toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GastronomicFacilityListDto that = (GastronomicFacilityListDto) o;

        return Objects.equal(this.gastronomicFacilities, that.gastronomicFacilities);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.gastronomicFacilities);
    }
}
