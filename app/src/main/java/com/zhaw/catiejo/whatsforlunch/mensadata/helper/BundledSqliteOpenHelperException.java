package com.zhaw.catiejo.whatsforlunch.mensadata.helper;

/**
 * Indicates that an error occurred when accessing a bundled SQLite database.
 */
public class BundledSqliteOpenHelperException extends RuntimeException {

    public BundledSqliteOpenHelperException() {
    }

    public BundledSqliteOpenHelperException(String detailMessage) {
        super(detailMessage);
    }

    public BundledSqliteOpenHelperException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BundledSqliteOpenHelperException(Throwable throwable) {
        super(throwable);
    }
}
