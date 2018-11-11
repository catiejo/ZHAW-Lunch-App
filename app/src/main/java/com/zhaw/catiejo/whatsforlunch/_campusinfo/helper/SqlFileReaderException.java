package com.zhaw.catiejo.whatsforlunch._campusinfo.helper;

/**
 * Exception that is thrown in case an error happens when reading SQL files.
 */
public class SqlFileReaderException extends RuntimeException {
    public SqlFileReaderException() {
        super();
    }

    public SqlFileReaderException(String detailMessage) {
        super(detailMessage);
    }

    public SqlFileReaderException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SqlFileReaderException(Throwable throwable) {
        super(throwable);
    }
}
