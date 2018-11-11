package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Data Transfer Object to transmit a Dish object over the wire.
 */
public final class DishDto {

    private final BigDecimal externalPrice;

    private final long id;

    private final BigDecimal internalPrice;

    private final String label;

    private final String name;

    private final BigDecimal priceForPartners;

    @JsonDeserialize(as = ArrayList.class)
    private final ImmutableList<SideDishDto> sideDishes;

    private final String version;

    @JsonCreator
    public DishDto(@JsonProperty("id") long id,
                   @JsonProperty("label") String label,
                   @JsonProperty("name") String name,
                   @JsonProperty("internalPrice") BigDecimal internalPrice,
                   @JsonProperty("priceForPartners") BigDecimal priceForPartners,
                   @JsonProperty("externalPrice") BigDecimal externalPrice,
                   @JsonProperty("sideDishes") ArrayList<SideDishDto> sideDishes,
                   @JsonProperty("version") String version) {
        this.id = id;
        this.label = label;
        this.name = name;
        this.internalPrice = internalPrice;
        this.priceForPartners = priceForPartners;
        this.externalPrice = externalPrice;
        this.sideDishes = ImmutableList.copyOf(sideDishes);
        this.version = version;
    }

    /**
     * Gets the external price (price to be paid by people that are not members of the ZHAW).
     *
     * @return External price.
     */
    public BigDecimal getExternalPrice() {
        return externalPrice;
    }

    /**
     * Gets the ID of the Dish (unique, issued by the remote database).
     *
     * @return The Dish ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the internal price (price to be paid by members of the ZHAW).
     *
     * @return The internal price.
     */
    public BigDecimal getInternalPrice() {
        return internalPrice;
    }

    /**
     * Gets the label of the Dish (like "Tagesmenü").
     *
     * @return The label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the name of the Dish (like "Hackbraten").
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the price for partners (price to be paid by partners of the ZHAW).
     *
     * @return The price for partners.
     */
    public BigDecimal getPriceForPartners() {
        return priceForPartners;
    }

    /**
     * Gets the list of SideDishes.
     *
     * @return List of SideDishes.
     */
    public ImmutableList<SideDishDto> getSideDishes() {
        return sideDishes;
    }

    /**
     * Gets the version of this Dish.
     *
     * @return The version of this Dish.
     */
    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("externalPrice", externalPrice).
                add("id", id).
                add("internalPrice", internalPrice).
                add("label", label).
                add("name", name).
                add("priceForPartners", priceForPartners).
                add("sideDishes", sideDishes).
                add("version", version).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(externalPrice, id, internalPrice, label, name, priceForPartners, sideDishes, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final DishDto other = (DishDto) obj;

        return Objects.equal(this.externalPrice, other.externalPrice) &&
                Objects.equal(this.id, other.id) &&
                Objects.equal(this.internalPrice, other.internalPrice) &&
                Objects.equal(this.label, other.label) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.priceForPartners, other.priceForPartners) &&
                Objects.equal(this.sideDishes, other.sideDishes) &&
                Objects.equal(this.version, other.version);
    }
}
