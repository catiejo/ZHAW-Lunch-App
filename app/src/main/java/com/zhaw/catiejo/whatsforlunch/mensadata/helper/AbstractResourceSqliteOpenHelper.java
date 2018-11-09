package com.zhaw.catiejo.whatsforlunch.mensadata.helper;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import java.io.InputStream;

/**
 * A helper class that allows access to SQLite databases shipped as raw resource.
 * <p/>
 * <p>To access a SQLite database that is shipped as raw resource, implement this class. Overriding the lifecycle
 * callbacks is optional.</p>
 */
public abstract class AbstractResourceSqliteOpenHelper extends AbstractBundledSqliteOpenHelper {

    private final int rawResource;

    /**
     * Creates a new helper instance that allows to access a bundled SQLite database stored in the application's assets
     * directory. The database is not actually
     * opened until {@link AbstractBundledSqliteOpenHelper#getDatabase()} is called.
     *
     * @param context      Context to use to access the database.
     * @param rawResource  ID of the raw resource (the bundled database).
     * @param databaseName Name of the database in the application directory to access.
     * @param version      Version number of the database (>= 1).
     */
    protected AbstractResourceSqliteOpenHelper(Context context, int rawResource, String databaseName, int version) {
        super(context, databaseName, version);
        this.rawResource = rawResource;
    }

    /**
     * Creates a new helper instance that allows to access a bundled SQLite database stored in the application's assets
     * directory. The database is not actually
     * opened until {@link AbstractBundledSqliteOpenHelper#getDatabase()} is called.
     *
     * @param context      Context to use to access the database.
     * @param rawResource  ID of the raw resource (the bundled database).
     * @param databaseName Name of the database in the application directory to access.
     * @param version      Version number of the database (>= 1).
     * @param factory      Factory to use for creating cursor objects.
     */
    public AbstractResourceSqliteOpenHelper(Context context, int rawResource, String databaseName,
                                            int version, SQLiteDatabase.CursorFactory factory) {
        super(context, databaseName, version, factory);

        this.rawResource = rawResource;
    }

    @Override
    protected final InputStream getBundledDatabase() {
        try {
            return getContext().getResources().openRawResource(rawResource);
        } catch (Resources.NotFoundException e) {
            throw new BundledSqliteOpenHelperException("Failed to open given raw resource.", e);
        }
    }
}