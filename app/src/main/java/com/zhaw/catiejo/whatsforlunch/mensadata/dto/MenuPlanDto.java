package com.zhaw.catiejo.whatsforlunch.mensadata.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object to transmit a MenuPlan object over the wire.
 */
public class MenuPlanDto {

    private final CalendarWeekDto calendarWeek;

    @JsonDeserialize(as = ArrayList.class)
    private final ImmutableList<Long> gastronomicFacilityIds;

    private final long id;

    @JsonDeserialize(as = ArrayList.class)
    private final ImmutableList<MenuDto> menus;
    private final String version;

    @JsonCreator
    public MenuPlanDto(@JsonProperty("id") long id,
                       @JsonProperty("calendarWeek") CalendarWeekDto calendarWeek,
                       @JsonProperty("gastronomicFacilityIds") List<Long> gastronomicFacilityIds,
                       @JsonProperty("menus") List<MenuDto> menus,
                       @JsonProperty("version") String version) {
        this.id = id;
        this.calendarWeek = calendarWeek;
        this.gastronomicFacilityIds = ImmutableList.copyOf(gastronomicFacilityIds);
        this.menus = ImmutableList.copyOf(menus);
        this.version = version;
    }

    /**
     * Gets the CalendarWeek this MenuPlan is for.
     *
     * @return The CalendarWeek this MenuPlan is for.
     */
    public CalendarWeekDto getCalendarWeek() {
        return calendarWeek;
    }

    /**
     * Gets the list of IDs of gastronomic facilities where this MenuPlan is offered.
     *
     * @return The list of gastronomic facility IDs.
     */
    public ImmutableList<Long> getGastronomicFacilityIds() {
        return gastronomicFacilityIds;
    }

    /**
     * Gets the ID of this MenuPlan (unique, assigned by the remote database).
     *
     * @return The ID of this MenuPlan.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the list of Menu objects that are part of this MenuPlan.
     *
     * @return The list of Menu objects.
     */
    public ImmutableList<MenuDto> getMenus() {
        return menus;
    }

    /**
     * Gets the version of this MenuPlan.
     *
     * @return The version of this MenuPlan.
     */
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("calendarWeek", calendarWeek).
                add("gastronomicFacilityIds", gastronomicFacilityIds).
                add("id", id).
                add("menus", menus).
                add("version", version).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(calendarWeek, gastronomicFacilityIds, id, menus, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final MenuPlanDto other = (MenuPlanDto) obj;

        return Objects.equal(this.calendarWeek, other.calendarWeek) &&
                Objects.equal(this.gastronomicFacilityIds, other.gastronomicFacilityIds) &&
                Objects.equal(this.id, other.id) &&
                Objects.equal(this.menus, other.menus) &&
                Objects.equal(this.version, other.version);
    }
}
