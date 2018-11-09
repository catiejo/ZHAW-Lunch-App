package com.zhaw.catiejo.whatsforlunch.mensadata.sync;

/**
 * RuntimeException thrown in case of an error during the execution of the {@link CateringInformationAvailabilityManager}.
 */
public class CateringInformationAvailabilityManagerException extends RuntimeException {
    public CateringInformationAvailabilityManagerException() {
    }

    public CateringInformationAvailabilityManagerException(String detailMessage) {
        super(detailMessage);
    }

    public CateringInformationAvailabilityManagerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CateringInformationAvailabilityManagerException(Throwable throwable) {
        super(throwable);
    }
}
