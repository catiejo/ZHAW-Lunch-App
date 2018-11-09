package com.zhaw.catiejo.whatsforlunch.mensadata.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A helper class that allows access to SQLite databases shipped with an Android application package.
 * <p/>
 * <p>The helper automatically copies the bundled SQLite database to the application directory and updates it on
 * demand. Users are expected to implement one of the direct subclasses.</p>
 */
public abstract class AbstractBundledSqliteOpenHelper {

    private static final String TAG = AbstractBundledSqliteOpenHelper.class.getSimpleName();

    private final Context context;

    private final String databaseName;

    private final SQLiteDatabase.CursorFactory factory;

    private final int version;

    // To be guarded by this. Don't touch if you're not in a synchronized method.
    private SQLiteDatabase database;

    /**
     * Creates a new helper instance that allows to access a bundled SQLite database. The database is not actually
     * opened until {@link AbstractBundledSqliteOpenHelper#getDatabase()} is called.
     *
     * @param context      Context to use to access the database.
     * @param databaseName Name of the database in the application directory to access.
     * @param version      Version number of the database (>= 1).
     */
    protected AbstractBundledSqliteOpenHelper(Context context, String databaseName, int version) {
        this(context, databaseName, version, null);
    }

    /**
     * Creates a new helper instance that allows to access a bundled SQLite database. The database is not actually
     * opened until {@link AbstractBundledSqliteOpenHelper#getDatabase()} is called.
     *
     * @param context      Context to use to access the database.
     * @param databaseName Name of the database in the application directory to access.
     * @param version      Version number of the database (>= 1).
     * @param factory      Factory to use for creating cursor objects.
     */
    protected AbstractBundledSqliteOpenHelper(Context context, String databaseName, int version,
                                              SQLiteDatabase.CursorFactory factory) {
        if (context == null) {
            throw new IllegalArgumentException("The context may not be null.");
        }
        if (databaseName == null || "".equals(databaseName)) {
            throw new IllegalArgumentException("Empty database name supplied.");
        }

        if (version < 1) {
            throw new IllegalArgumentException("Version must be >= 1.");
        }

        this.context = context;
        this.databaseName = databaseName;
        this.factory = factory;
        this.version = version;
    }

    /**
     * Gets the current context used to access the database.
     *
     * @return Application context.
     */
    protected Context getContext() {
        return context;
    }

    /**
     * Gets the name of the database being opened.
     *
     * @return The name of the database being opened.
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Closes the database object if it is open.
     */
    public synchronized void close() {
        if (database != null && database.isOpen()) {
            database.close();
            database = null;
        }
    }

    /**
     * Opens the database and returns it.
     * <p/>
     * <p>If the database does not exist or is outdated, it will be automatically populated using the bundled SQLite
     * database. Problems like a full disk may prevent the database from being populated or opened. Successive calls
     * return the same database object if it has not been closed in the meantime.</p>
     * <p/>
     * <p class="caution">This method may take a long time to return.</p>
     *
     * @return The opened database.
     * @throws BundledSqliteOpenHelperException In case of an error.
     */
    public synchronized SQLiteDatabase getDatabase() {
        if (database != null && database.isOpen()) {
            return database;
        }

        SQLiteDatabase db = null;
        try {
            // Make sure that the database file exists, otherwise the bundled one can't be copied over it.
            try {
                db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, factory);
            } catch (SQLiteException e) {
                Log.e(TAG, "Could not open database " + databaseName);
                throw new BundledSqliteOpenHelperException("Could not open database " + databaseName + ".", e);
            }

            final int oldVersion = db.getVersion();
            if (oldVersion != this.version) {
                Log.i(TAG, String.format("Database on device (version %s) is outdated, replacing it with bundled "
                        + "one (version %s).", oldVersion, version));

                // Close database, copy the bundled one to /data/data/<app-pkg>/databases/ and reopen it.
                db.close();

                try {
                    replaceCurrentDatabaseWithBundledDatabase();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to overwrite database " + databaseName + " in app directory with bundled one.");

                    throw new BundledSqliteOpenHelperException("Failed to overwrite database " + databaseName
                            + " in app directory with bundled one.", e);
                }

                try {
                    db = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, factory);
                } catch (SQLiteException e) {
                    Log.e(TAG, "Could not open database " + databaseName);
                    throw new BundledSqliteOpenHelperException("Could not open database " + databaseName + ".", e);
                }

                db.setVersion(this.version);
            }

            onConfigure(db);

            if (oldVersion != this.version) {
                db.beginTransaction();
                try {
                    onPostCopy(db, oldVersion, version);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            Log.i(TAG, "Successfully opened database " + databaseName);

            this.database = db;
            return this.database;
        } catch (Exception e) {
            throw new BundledSqliteOpenHelperException(e);
        } finally {
            if (db != null && this.database != db) {
                db.close();
            }
        }
    }

    /**
     * Template method that returns an InputStream which points to the bundled SQLite database.
     *
     * @return InputStream pointing to bundled SQLite database.
     * @throws BundledSqliteOpenHelperException If InputStream could not be opened.
     */
    protected abstract InputStream getBundledDatabase();

    /**
     * Called when the database connection is being configured. Implement this method if you like to enable features
     * like foreign key support.
     * <p/>
     * <p>This method is called before
     * {@link AbstractBundledSqliteOpenHelper#onPostCopy(android.database.sqlite.SQLiteDatabase, int, int)} and should
     * not modify the database. Use
     * {@link AbstractBundledSqliteOpenHelper#onPostCopy(android.database.sqlite.SQLiteDatabase, int, int)} for this
     * purpose instead.</p>
     *
     * @param db The database.
     */
    protected abstract void onConfigure(SQLiteDatabase db);

    /**
     * Called after the bundled database has been copied to the application directory. Allows you to apply changes to
     * the database before it is being returned to the calling code.
     * <p/>
     * <p>This method is called after
     * {@link AbstractBundledSqliteOpenHelper#onConfigure(android.database.sqlite.SQLiteDatabase)} and wrapped in a
     * transaction.</p>
     *
     * @param db         The database.
     * @param oldVersion The old version of the database (the one that was replaced).
     * @param newVersion The new version of the database.
     */
    protected abstract void onPostCopy(SQLiteDatabase db, int oldVersion, int newVersion);

    private void replaceCurrentDatabaseWithBundledDatabase() throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = getBundledDatabase();
            os = new FileOutputStream(context.getDatabasePath(databaseName));

            Log.i(TAG, "Overwriting database " + databaseName + " in app directory with bundled one.");

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            os.flush();

            Log.i(TAG, "Finished overwriting database " + databaseName + " in app directory with bundled one.");
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }
}
