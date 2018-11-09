package com.zhaw.catiejo.whatsforlunch.mensadata;

/**
 * Wraps a database operation that can either be executed individually within a separate transaction or alongside of
 * other operations in a batch operation within a single transaction.
 *
 * @param <T> Type of the return value (e.g. an Uri or the number of affected rows).
 */
interface DbOperation<T> {

    /**
     * Executes the operation.
     *
     * @return The result of the operation (e.g. an Uri or the number of affected rows).
     */
    T execute();
}
