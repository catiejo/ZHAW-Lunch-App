package com.zhaw.catiejo.whatsforlunch._campusinfo.dao;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a service time plan. Every instance is immutable once created.
 */
public class ServiceTimePlanDao {
    private final ServiceTimeDao monday;
    private final ServiceTimeDao tuesday;
    private final ServiceTimeDao wednesday;
    private final ServiceTimeDao thursday;
    private final ServiceTimeDao friday;
    private final ServiceTimeDao saturday;
    private final ServiceTimeDao sunday;

    /**
     * Creates new immutable instance.
     *
     * @param monday    Service time for monday.
     * @param tuesday   Service time for tuesday.
     * @param wednesday Service time for wednesday.
     * @param thursday  Service time for thursday.
     * @param friday    Service time for friday.
     * @param saturday  Service time for saturday.
     * @param sunday    Service time for sunday.
     */
    public ServiceTimePlanDao(ServiceTimeDao monday, ServiceTimeDao tuesday, ServiceTimeDao wednesday, ServiceTimeDao thursday,
                              ServiceTimeDao friday, ServiceTimeDao saturday, ServiceTimeDao sunday) {
        this.monday = checkNotNull(monday);
        this.tuesday = checkNotNull(tuesday);
        this.wednesday = checkNotNull(wednesday);
        this.thursday = checkNotNull(thursday);
        this.friday = checkNotNull(friday);
        this.saturday = checkNotNull(saturday);
        this.sunday = checkNotNull(sunday);
    }

    /**
     * Gets the service time for Monday.
     *
     * @return The service time for Monday.
     */
    public ServiceTimeDao getMonday() {
        return monday;
    }

    /**
     * Gets the service time for Tuesday.
     *
     * @return The service time for Tuesday.
     */
    public ServiceTimeDao getTuesday() {
        return tuesday;
    }

    /**
     * Gets the service time for Wednesday.
     *
     * @return The service time for Wednesday.
     */
    public ServiceTimeDao getWednesday() {
        return wednesday;
    }

    /**
     * Gets the service time for Thursday.
     *
     * @return The service time for Thursday.
     */
    public ServiceTimeDao getThursday() {
        return thursday;
    }

    /**
     * Gets the service time for Friday.
     *
     * @return The service time for Friday.
     */
    public ServiceTimeDao getFriday() {
        return friday;
    }

    /**
     * Gets the service time for Saturday.
     *
     * @return The service time for Saturday.
     */
    public ServiceTimeDao getSaturday() {
        return saturday;
    }

    /**
     * Gets the service time for Sunday.
     *
     * @return The service time for Sunday.
     */
    public ServiceTimeDao getSunday() {
        return sunday;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("monday", monday).
                add("tuesday", tuesday).
                add("wednesday", wednesday).
                add("thursday", thursday).
                add("friday", friday).
                add("saturday", saturday).
                add("sunday", sunday).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ServiceTimePlanDao other = (ServiceTimePlanDao) obj;

        return Objects.equal(this.monday, other.monday) &&
                Objects.equal(this.tuesday, other.tuesday) &&
                Objects.equal(this.wednesday, other.wednesday) &&
                Objects.equal(this.thursday, other.thursday) &&
                Objects.equal(this.friday, other.friday) &&
                Objects.equal(this.saturday, other.saturday) &&
                Objects.equal(this.sunday, other.sunday);
    }

    /**
     * Returns true if all the service times are empty, false otherwise.
     *
     * @return True if all the service time are empty, false otherwise.
     */
    public boolean isEmpty() {
        // it is sufficient to look just at the start time, because there is a precondition in the constructor
        // of the ServiceTimeDao that either both must be present or neither.
        return !monday.getFrom().isPresent() &&
                !tuesday.getFrom().isPresent() &&
                !wednesday.getFrom().isPresent() &&
                !thursday.getFrom().isPresent() &&
                !friday.getFrom().isPresent() &&
                !saturday.getFrom().isPresent() &&
                !sunday.getFrom().isPresent();
    }
}
