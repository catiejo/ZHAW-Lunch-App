package com.zhaw.catiejo.whatsforlunch.mensadata.sync;

import com.google.common.base.Objects;

/**
 * Notification about the availability of the catering information.
 */
public class CateringInformationAvailabilityNotification {

    private final CateringInformationState state;

    public CateringInformationAvailabilityNotification(CateringInformationState state) {
        this.state = state;
    }

    /**
     * Gets the current state of the catering information.
     *
     * @return The current state of the catering information.
     */
    public CateringInformationState getState() {
        return state;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("state", state).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(state);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final CateringInformationAvailabilityNotification other = (CateringInformationAvailabilityNotification) obj;

        return Objects.equal(this.state, other.state);
    }
}
