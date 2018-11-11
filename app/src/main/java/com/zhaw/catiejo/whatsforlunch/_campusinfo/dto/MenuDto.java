package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * Data Transfer Object to transmit a Menu over the wire.
 */
public class MenuDto {
    private final ImmutableList<DishDto> dishes;
    private final long id;
    private final LocalDate offeredOn;
    private final String version;

    public MenuDto(@JsonProperty("dishes") List<DishDto> dishes,
                   @JsonProperty("id") long id,
                   @JsonProperty("offeredOn") DateTime offeredOn,
                   @JsonProperty("version") String version) {
        this.dishes = ImmutableList.copyOf(dishes);
        this.id = id;
        this.offeredOn = offeredOn.toDateTime(Constants.LocalTimeZone).toLocalDate();
        this.version = version;
    }

    /**
     * Gets the list of Dish objects.
     *
     * @return The list of Dish objects.
     */
    public ImmutableList<DishDto> getDishes() {
        return dishes;
    }

    /**
     * Gets the ID of this Menu (unique, issued by the remote database).
     *
     * @return The ID of this Menu.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the date this Menu is offered on.
     *
     * @return The date this Menu is offered on.
     */
    public LocalDate getOfferedOn() {
        return offeredOn;
    }

    /**
     * Gets the version of this Menu.
     *
     * @return The version of this Menu.
     */
    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(dishes, id, offeredOn, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final MenuDto other = (MenuDto) obj;

        return Objects.equal(this.dishes, other.dishes) &&
                Objects.equal(this.id, other.id) &&
                Objects.equal(this.offeredOn, other.offeredOn) &&
                Objects.equal(this.version, other.version);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("dishes", dishes).
                add("id", id).
                add("offeredOn", offeredOn).
                add("version", version).
                toString();
    }
}
