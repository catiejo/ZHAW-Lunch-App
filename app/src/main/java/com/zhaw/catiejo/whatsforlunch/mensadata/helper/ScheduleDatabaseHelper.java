package com.zhaw.catiejo.whatsforlunch.mensadata.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ch.zhaw.init.android.campusinfo.R;
import ch.zhaw.init.android.campusinfo.util.AndroidUtils;

/**
 * Manages the SQLite database storing all schedule information and the access to it.
 */
public final class ScheduleDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = ScheduleDatabaseHelper.class.getCanonicalName();

    private static final String DATABASE_NAME = "schedules.db";

    static final int DATABASE_VERSION = 2;

    private final Context mContext;

    public ScheduleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            Log.i(TAG, "Enabling foreign key support for database " + DATABASE_NAME);
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Iterable<String> stmts = AndroidUtils.readSqlFile(mContext, R.raw.schedules);
        for (String stmt : stmts) {
            db.execSQL(stmt);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion > 1) {
            Log.i(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ".");

            db.execSQL("DROP TABLE IF EXISTS download");
            db.execSQL("DROP TABLE IF EXISTS owner");
            db.execSQL("DROP TABLE IF EXISTS event_school_class");
            db.execSQL("DROP TABLE IF EXISTS event_lecturer");
            db.execSQL("DROP TABLE IF EXISTS event");
            db.execSQL("DROP TABLE IF EXISTS slot");
            db.execSQL("DROP TABLE IF EXISTS block");

            Iterable<String> stmts = AndroidUtils.readSqlFile(mContext, R.raw.schedules);
            for (String stmt : stmts) {
                db.execSQL(stmt);
            }
        }
    }
}
