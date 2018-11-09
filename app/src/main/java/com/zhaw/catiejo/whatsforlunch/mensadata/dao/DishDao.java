package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.DishInfo;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a dish. Every instance is immutable once created.
 */
public class DishDao {
    private final Long id;
    private final Long dishId;
    private final Long facilityId;
    private final String label;
    private final String name;
    private final Optional<String> firstSideDish;
    private final Optional<String> secondSideDish;
    private final Optional<String> thirdSideDish;
    private final LocalDate offeredOn;
    private final BigDecimal internalPrice;
    private final BigDecimal priceForPartners;
    private final BigDecimal externalPrice;
    private final DateTime lastUpdate;
    private final String version;

    /**
     * Creates new instance using the data supplied by the given cursor.
     *
     * @param cursor Cursor to extract the data from.
     * @return New dish instance.
     */
    public static DishDao fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(DishInfo.Id));
        long dishId = cursor.getLong(cursor.getColumnIndex(DishInfo.DishId));
        long facilityId = cursor.getLong(cursor.getColumnIndex(DishInfo.FacilityId));
        String label = cursor.getString(cursor.getColumnIndex(DishInfo.Label));
        String name = cursor.getString(cursor.getColumnIndex(DishInfo.Name));
        String firstSideDish = cursor.getString(cursor.getColumnIndex(DishInfo.FirstSideDish));
        String secondSideDish = cursor.getString(cursor.getColumnIndex(DishInfo.SecondSideDish));
        String thirdSideDish = cursor.getString(cursor.getColumnIndex(DishInfo.ThirdSideDish));
        LocalDate offeredOn = LocalDate.parse(cursor.getString(cursor.getColumnIndex(DishInfo.OfferedOn)));
        BigDecimal internalPrice = new BigDecimal(cursor.getString(cursor.getColumnIndex(DishInfo.InternalPrice)));
        BigDecimal priceForPartners = new BigDecimal(cursor.getString(cursor.getColumnIndex(DishInfo.PriceForPartners)));
        BigDecimal externalPrice = new BigDecimal(cursor.getString(cursor.getColumnIndex(DishInfo.ExternalPrice)));
        DateTime lastUpdate = DateTime.parse(cursor.getString(cursor.getColumnIndex(DishInfo.LastUpdate)));
        String version = cursor.getString(cursor.getColumnIndex(DishInfo.Version));

        Optional<String> optionalFirstSideDish = (Strings.isNullOrEmpty(firstSideDish)) ? Optional.<String>absent() : Optional.of(firstSideDish);
        Optional<String> optionalSecondSideDish = (Strings.isNullOrEmpty(secondSideDish)) ? Optional.<String>absent() : Optional.of(secondSideDish);
        Optional<String> optionalThirdSideDish = (Strings.isNullOrEmpty(thirdSideDish)) ? Optional.<String>absent() : Optional.of(thirdSideDish);

        return new DishDao(id, dishId, facilityId, label, name, optionalFirstSideDish, optionalSecondSideDish,
                optionalThirdSideDish, offeredOn, internalPrice, priceForPartners, externalPrice, lastUpdate, version);
    }

    /**
     * Creates new immutable instance.
     *
     * @param id               The SQLite rowid that uniquely identifies this dish.
     * @param dishId           The ID of the dish.
     * @param facilityId       The ID of the facility that offers this dish.
     * @param label            Label of the dish (like "Tagesmenü").
     * @param name             Name of the dish (like "Hackbraten").
     * @param firstSideDish    The name of the first side dish (like "Kartoffelstock").
     * @param secondSideDish   The name of the second side dish (like "Karotten").
     * @param thirdSideDish    The name of the third side dish (like "Rahmsauce").
     * @param offeredOn        The date that this dish is offered on (in the local time zone of the facility).
     * @param internalPrice    The price for members of the ZHAW (in the local currency of the facility).
     * @param priceForPartners The price for partners of the ZHAW (in the local currency of the facility).
     * @param externalPrice    The price for people that are not members of the ZHAW (in the local currency of the facility).
     * @param lastUpdate       Date and time when the dish was updated the last time.
     * @param version          Version of the dish.
     */
    public DishDao(Long id, Long dishId, Long facilityId, String label, String name, Optional<String> firstSideDish,
                   Optional<String> secondSideDish, Optional<String> thirdSideDish, LocalDate offeredOn,
                   BigDecimal internalPrice, BigDecimal priceForPartners, BigDecimal externalPrice, DateTime lastUpdate,
                   String version) {
        this.id = id;
        this.dishId = checkNotNull(dishId);
        this.facilityId = checkNotNull(facilityId);
        this.label = checkNotNull(label);
        this.name = checkNotNull(name);
        this.firstSideDish = checkNotNull(firstSideDish);
        this.secondSideDish = checkNotNull(secondSideDish);
        this.thirdSideDish = checkNotNull(thirdSideDish);
        this.offeredOn = checkNotNull(offeredOn);
        this.internalPrice = checkNotNull(internalPrice);
        this.priceForPartners = checkNotNull(priceForPartners);
        this.externalPrice = checkNotNull(externalPrice);
        this.lastUpdate = checkNotNull(lastUpdate);
        this.version = checkNotNull(version);
    }

    /**
     * Gets the SQLite rowid that uniquely identifies this dish.
     *
     * @return The SQLite rowid.
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the ID of the dish.
     *
     * @return The ID of the dish.
     */
    public Long getDishId() {
        return dishId;
    }

    /**
     * Gets the ID of the facility that offers this dish.
     *
     * @return The ID of the facility.
     */
    public Long getFacilityId() {
        return facilityId;
    }

    /**
     * Gets the label of the dish (like "Budget-Menü").
     *
     * @return The label of the dish.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the name of the dish (like "Paniertes Schnitzel").
     *
     * @return The name of the dish.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the name of the first side dish (like "Pommes frites", if present).
     *
     * @return The name of the first side dish.
     */
    public Optional<String> getFirstSideDish() {
        return firstSideDish;
    }

    /**
     * Gets the name of the second side dish (like "Erbsen", if present).
     *
     * @return The name of the second side dish.
     */
    public Optional<String> getSecondSideDish() {
        return secondSideDish;
    }

    /**
     * Gets the name of the third side dish (like "Zitronenschnitz", if present).
     *
     * @return The name of the third side dish.
     */
    public Optional<String> getThirdSideDish() {
        return thirdSideDish;
    }

    /**
     * Gets the date when this dish is offered.
     *
     * @return The date when this dish is offered.
     */
    public LocalDate getOfferedOn() {
        return offeredOn;
    }

    /**
     * Gets the price for members of the ZHAW (in the local currency of the facility).
     *
     * @return The internal price.
     */
    public BigDecimal getInternalPrice() {
        return internalPrice;
    }

    /**
     * Gets the price for partners of the ZHAW (in the local currency of the facility).
     *
     * @return The price for partners.
     */
    public BigDecimal getPriceForPartners() {
        return priceForPartners;
    }

    /**
     * Gets the price for people that are not members of the ZHAW (in the local currency of the facility).
     *
     * @return The external price.
     */
    public BigDecimal getExternalPrice() {
        return externalPrice;
    }

    /**
     * Gets the date and time when the dish was downloaded from the remote server for the last time.
     *
     * @return Date and time when the dish was updated for the last time.
     */
    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Gets the version of the dish that was received from the remote server (a Base64-encoded byte array).
     *
     * @return The version of the dish.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Converts this instance to {@link ContentValues} that can be used to store this instance in the database.
     *
     * @return ContentValues containing this instance's data.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        if (id != null) {
            values.put(DishInfo.Id, id);
        } else {
            values.putNull(DishInfo.Id);
        }
        values.put(DishInfo.DishId, dishId);
        values.put(DishInfo.FacilityId, facilityId);
        values.put(DishInfo.Label, label);
        values.put(DishInfo.Name, name);

        if (firstSideDish.isPresent()) {
            values.put(DishInfo.FirstSideDish, firstSideDish.get());
        } else {
            values.putNull(DishInfo.FirstSideDish);
        }

        if (secondSideDish.isPresent()) {
            values.put(DishInfo.SecondSideDish, secondSideDish.get());
        } else {
            values.putNull(DishInfo.SecondSideDish);
        }

        if (thirdSideDish.isPresent()) {
            values.put(DishInfo.ThirdSideDish, thirdSideDish.get());
        } else {
            values.putNull(DishInfo.ThirdSideDish);
        }

        values.put(DishInfo.OfferedOn, offeredOn.toString());
        values.put(DishInfo.InternalPrice, internalPrice.toString());
        values.put(DishInfo.PriceForPartners, priceForPartners.toString());
        values.put(DishInfo.ExternalPrice, externalPrice.toString());
        values.put(DishInfo.LastUpdate, lastUpdate.toString());
        values.put(DishInfo.Version, version);
        return values;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("id", id).
                add("dishId", dishId).
                add("facilityId", facilityId).
                add("label", label).
                add("name", name).
                add("firstSideDish", firstSideDish).
                add("secondSideDish", secondSideDish).
                add("thirdSideDish", thirdSideDish).
                add("offeredOn", offeredOn).
                add("internalPrice", internalPrice).
                add("priceForPartners", priceForPartners).
                add("externalPrice", externalPrice).
                add("lastUpdate", lastUpdate).
                add("version", version).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, dishId, facilityId, label, name, firstSideDish, secondSideDish, thirdSideDish,
                offeredOn, internalPrice, priceForPartners, externalPrice, lastUpdate, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final DishDao other = (DishDao) obj;

        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.dishId, other.dishId) &&
                Objects.equal(this.facilityId, other.facilityId) &&
                Objects.equal(this.label, other.label) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.firstSideDish, other.firstSideDish) &&
                Objects.equal(this.secondSideDish, other.secondSideDish) &&
                Objects.equal(this.thirdSideDish, other.thirdSideDish) &&
                Objects.equal(this.offeredOn, other.offeredOn) &&
                Objects.equal(this.internalPrice, other.internalPrice) &&
                Objects.equal(this.priceForPartners, other.priceForPartners) &&
                Objects.equal(this.externalPrice, other.externalPrice) &&
                Objects.equal(this.lastUpdate, other.lastUpdate) &&
                Objects.equal(this.version, other.version);
    }
}
