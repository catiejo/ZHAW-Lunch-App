package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import android.database.Cursor;

/**
 * Interface that defines factory methods for the DAO of the type {@code T}.
 *
 * @param <T> Type of the DAO.
 */
public interface IDaoFactory<T> {
    /**
     * Constructs a new DAO of type {@code T} from the supplied cursor. It is up to the caller to make sure that the
     * cursor is properly initialized and points to correct row which the DAO should be constructed from.
     *
     * @param cursor Cursor to construct the DAO from.
     * @return Constructed DAO.
     */
    T constructFromCursor(Cursor cursor);
}
