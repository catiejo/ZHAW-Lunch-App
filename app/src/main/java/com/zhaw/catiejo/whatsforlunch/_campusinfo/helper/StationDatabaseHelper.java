package com.zhaw.catiejo.whatsforlunch._campusinfo.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.zhaw.catiejo.whatsforlunch.R;

/**
 * Manages the SQLite database that stores the Swiss public transport stations.
 */
public class StationDatabaseHelper extends AbstractResourceSqliteOpenHelper {

    private static final String DATABASE_NAME = "stations.db";

    private static final int DATABASE_VERSION = 2;

    public StationDatabaseHelper(Context context) {
        super(context, R.raw.stations, DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    protected void onConfigure(SQLiteDatabase sqLiteDatabase) {
        // do nothing
    }

    @Override
    protected void onPostCopy(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // do nothing
    }
}
