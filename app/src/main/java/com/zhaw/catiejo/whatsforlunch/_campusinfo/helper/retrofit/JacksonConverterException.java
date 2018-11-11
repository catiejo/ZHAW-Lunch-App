package com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit;

/**
 * Exception thrown if something goes wrong during the serialization of Java objects.
 */
public class JacksonConverterException extends RuntimeException {
    public JacksonConverterException() {
    }

    public JacksonConverterException(String detailMessage) {
        super(detailMessage);
    }

    public JacksonConverterException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public JacksonConverterException(Throwable throwable) {
        super(throwable);
    }
}
