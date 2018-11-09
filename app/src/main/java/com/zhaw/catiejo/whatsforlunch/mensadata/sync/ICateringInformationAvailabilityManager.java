package com.zhaw.catiejo.whatsforlunch.mensadata.sync;

import com.squareup.otto.Produce;

/**
 * Manages the download of the catering information.
 */
public interface ICateringInformationAvailabilityManager {

    /**
     * Asynchronously tests whether the catering information is available and downloads it if it is not. Updates about
     * the progress of the processing are posted to the message bus as {@link CateringInformationAvailabilityNotification}.
     */
    void ensureCateringInformationAvailability();

    /**
     * Asynchronously tests whether the menu plans are up to date and downloads them if they are not. Updates about
     * the progress of the processing are posted to the message bus as {@link CateringInformationAvailabilityNotification}.
     */
    void ensureMenuPlanAvailability();

    /**
     * Synchronously synchronizes the catering information with the remote service. Updates about the progress of the
     * synchronization are posted to the message bus as {@link CateringInformationAvailabilityNotification}.
     */
    void syncCateringInformation();

    /**
     * Provides the complete state of the {@link ICateringInformationAvailabilityManager} to new subscribers of the
     * message bus.
     *
     * @return Notification containing the current state.
     */
    @Produce
    CateringInformationAvailabilityNotification produceState();
}
