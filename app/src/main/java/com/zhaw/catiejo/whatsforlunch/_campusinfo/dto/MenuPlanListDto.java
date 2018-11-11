package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object to transmit a MenuPlanListDto object over the wire.
 */
public class MenuPlanListDto {

    @JsonDeserialize(as = ArrayList.class)
    private final ImmutableList<MenuPlanDto> menuPlans;

    @JsonCreator
    public MenuPlanListDto(@JsonProperty("menuPlans") List<MenuPlanDto> menuPlans) {
        this.menuPlans = ImmutableList.copyOf(menuPlans);
    }

    /**
     * Gets the list of MenuPlan objects.
     *
     * @return List of MenuPlan objects.
     */
    public ImmutableList<MenuPlanDto> getMenuPlans() {
        return menuPlans;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(menuPlans);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MenuPlanListDto other = (MenuPlanListDto) obj;
        return Objects.equal(this.menuPlans, other.menuPlans);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("menuPlans", menuPlans).
                toString();
    }
}
