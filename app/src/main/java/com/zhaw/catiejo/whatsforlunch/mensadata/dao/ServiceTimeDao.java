package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.joda.time.LocalTime;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a service time. Every instance is immutable once created.
 */
public class ServiceTimeDao {

    private final Optional<LocalTime> from;
    private final Optional<LocalTime> until;

    /**
     * Creates new immutable instance.
     *
     * @param from  Start time of the service time (in the time zone of the gastronomic facility).
     * @param until End time of the service time (in the time zone of the gastronomic facility).
     */
    public ServiceTimeDao(Optional<LocalTime> from, Optional<LocalTime> until) {
        this.from = checkNotNull(from);
        this.until = checkNotNull(until);

        checkArgument((from.isPresent() && until.isPresent()) || (!from.isPresent() && !until.isPresent()));
    }

    /**
     * Gets the start time of the service time (in the time zone of the gastronomic facility). The duration is defined
     * as: {@code from <= duration < until}. If there are no service times for the day,
     * {@link com.google.common.base.Optional#absent()} is returned.
     *
     * @return Start time of the service time.
     */
    public Optional<LocalTime> getFrom() {
        return from;
    }

    /**
     * Gets the end time of the service time (in the time zone of the gastronomic facility). The duration is defined
     * as: {@code from <= duration < until}. If there are no service times for the day,
     * {@link com.google.common.base.Optional#absent()} is returned.
     *
     * @return End time of the service time.
     */
    public Optional<LocalTime> getUntil() {
        return until;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(from, until);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ServiceTimeDao other = (ServiceTimeDao) obj;
        return Objects.equal(this.from, other.from) && Objects.equal(this.until, other.until);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("from", from).
                add("until", until).
                toString();
    }
}
