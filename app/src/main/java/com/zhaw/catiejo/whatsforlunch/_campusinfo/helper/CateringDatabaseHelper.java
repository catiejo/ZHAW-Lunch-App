package com.zhaw.catiejo.whatsforlunch._campusinfo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.AndroidUtils;

/**
 * Manages the SQLite database storing all catering information and the access to it.
 */
public final class CateringDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = CateringDatabaseHelper.class.getCanonicalName();

    private static final String DATABASE_NAME = "catering.db";

    static final int DATABASE_VERSION = 1;

    private final Context mContext;

    public CateringDatabaseHelper(Context context) {
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
        Iterable<String> stmts = AndroidUtils.readSqlFile(mContext, R.raw.catering);
        for (String stmt : stmts) {
            db.execSQL(stmt);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing (current version is initial version)
    }
}
