package com.zhaw.catiejo.whatsforlunch._campusinfo.sync;

/**
 * Exception thrown in case an error occurs when synchronizing catering information.
 */
public class CateringInformationSynchronizationException extends RuntimeException {
    public CateringInformationSynchronizationException() {
    }

    public CateringInformationSynchronizationException(String detailMessage) {
        super(detailMessage);
    }

    public CateringInformationSynchronizationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CateringInformationSynchronizationException(Throwable throwable) {
        super(throwable);
    }
}
