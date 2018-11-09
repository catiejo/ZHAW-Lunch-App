package com.zhaw.catiejo.whatsforlunch.mensadata;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.zhaw.catiejo.whatsforlunch.mensadata.helper.CateringDatabaseHelper;
import com.zhaw.catiejo.whatsforlunch.mensadata.helper.ArrayUtils;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.squareup.phrase.Phrase;

import java.util.ArrayList;
import java.util.Map;

/**
 * {@link android.content.ContentProvider} that allows to store, manipulate and query catering information like menu
 * plans.
 */
public final class CateringContentProvider extends ContentProvider {

    /**
     * Authority of the {@link CateringContentProvider}.
     */
    public static final String AUTHORITY = CateringContentProvider.class.getCanonicalName();

    /**
     * Root {@link android.net.Uri} of the {@link CateringContentProvider}. Do not build the {@link android.net.Uri}s
     * yourself, use the helper class {@link ActionUri} instead.
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Matcher used to recognize the different URIs.
     */
    private static final UriMatcher sUriMatcher;

    /**
     * Defines whether a batch operation is ongoing (and therefore no new transactions have to be opened).
     */
    private ThreadLocal<Boolean> mInBatchOperation = new ThreadLocal<Boolean>();

    /**
     * The active SQLiteOpenHelper.
     */
    private CateringDatabaseHelper mOpenHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (Action action : Action.values()) {
            sUriMatcher.addURI(AUTHORITY, action.uri(), action.ordinal());
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CateringDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final int action = sUriMatcher.match(uri);

        if (action == Action.GASTRONOMIC_FACILITIES.ordinal()) {
            return queryGastronomicFacilities(uri, projection, selection, selectionArgs, sortOrder);
        } else if (action == Action.GASTRONOMIC_FACILITY.ordinal()) {
            return queryGastronomicFacility(uri, projection, selection, selectionArgs, sortOrder);
        } else if (action == Action.HOLIDAYS.ordinal()) {
            return queryHolidays(uri, projection, selection, selectionArgs, sortOrder);
        } else if (action == Action.HOLIDAY.ordinal()) {
            return queryHoliday(uri, projection, selection, selectionArgs, sortOrder);
        } else if (action == Action.SERVICE_TIME_PERIODS.ordinal()) {
            return queryServiceTimePeriods(uri, projection, selection, selectionArgs, sortOrder);
        } else if (action == Action.SERVICE_TIME_PERIOD.ordinal()) {
            return queryServiceTimePeriod(uri, projection, selection, selectionArgs, sortOrder);
        } else if (action == Action.DISHES.ordinal()) {
            return queryDishes(uri, projection, selection, selectionArgs, sortOrder);
        } else if (action == Action.DISH.ordinal()) {
            return queryDish(uri, projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }

    private Cursor queryGastronomicFacilities(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(GastronomicFacilityInfo.Table);

        Cursor result = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    private Cursor queryGastronomicFacility(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", GastronomicFacilityInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        long id = ContentUris.parseId(uri);
        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return queryGastronomicFacilities(uri, projection, newSelection, newArgs, sortOrder);
    }

    private Cursor queryHolidays(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(HolidayInfo.Table);

        Cursor result = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    private Cursor queryHoliday(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", HolidayInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        long id = ContentUris.parseId(uri);
        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return queryHolidays(uri, projection, newSelection, newArgs, sortOrder);
    }

    private Cursor queryServiceTimePeriods(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(ServiceTimePeriodInfo.Table);

        Cursor result = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    private Cursor queryServiceTimePeriod(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", ServiceTimePeriodInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        long id = ContentUris.parseId(uri);
        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return queryServiceTimePeriods(uri, projection, newSelection, newArgs, sortOrder);
    }

    private Cursor queryDishes(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DishInfo.Table);

        Cursor result = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        result.setNotificationUri(getContext().getContentResolver(), uri);

        return result;
    }

    private Cursor queryDish(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", DishInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        long id = ContentUris.parseId(uri);
        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return queryDishes(uri, projection, newSelection, newArgs, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        final int action = sUriMatcher.match(uri);

        if (action == Action.GASTRONOMIC_FACILITIES.ordinal()) {
            return Action.GASTRONOMIC_FACILITIES.mimeType();
        } else if (action == Action.GASTRONOMIC_FACILITY.ordinal()) {
            return Action.GASTRONOMIC_FACILITY.mimeType();
        } else if (action == Action.HOLIDAYS.ordinal()) {
            return Action.HOLIDAYS.mimeType();
        } else if (action == Action.HOLIDAY.ordinal()) {
            return Action.HOLIDAY.mimeType();
        } else if (action == Action.SERVICE_TIME_PERIODS.ordinal()) {
            return Action.SERVICE_TIME_PERIODS.mimeType();
        } else if (action == Action.SERVICE_TIME_PERIOD.ordinal()) {
            return Action.SERVICE_TIME_PERIOD.mimeType();
        } else if (action == Action.DISHES.ordinal()) {
            return Action.DISHES.mimeType();
        } else if (action == Action.DISH.ordinal()) {
            return Action.DISH.mimeType();
        }

        return null;
    }

    @Override
    public Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int action = sUriMatcher.match(uri);

        Map<Integer, DbOperation<Uri>> routes = Maps.newHashMap();
        routes.put(Action.GASTRONOMIC_FACILITIES.ordinal(), new DbOperation<Uri>() {
            @Override
            public Uri execute() {
                return insertIntoGastronomicFacilityTable(values);
            }
        });
        routes.put(Action.HOLIDAYS.ordinal(), new DbOperation<Uri>() {
            @Override
            public Uri execute() {
                return insertIntoHolidayTable(values);
            }
        });
        routes.put(Action.SERVICE_TIME_PERIODS.ordinal(), new DbOperation<Uri>() {
            @Override
            public Uri execute() {
                return insertIntoServiceTimePeriodTable(values);
            }
        });
        routes.put(Action.DISHES.ordinal(), new DbOperation<Uri>() {
            @Override
            public Uri execute() {
                return insertIntoDishTable(values);
            }
        });

        if (!routes.containsKey(action)) {
            String message = "Failed to insert into %s. Insertion for URI is not supported or URI is not supported.";
            throw new IllegalArgumentException(String.format(message, uri));
        }

        if (inBatchOperation()) {
            return routes.get(action).execute();
        }

        try {
            db.beginTransaction();

            Uri retVal = routes.get(action).execute();
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);

            return retVal;
        } finally {
            db.endTransaction();
        }
    }

    private Uri insertIntoGastronomicFacilityTable(ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = db.insertOrThrow(GastronomicFacilityInfo.Table, null, values);
        return ActionUri.forGastronomicFacility(id);

    }

    private Uri insertIntoHolidayTable(ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = db.insertOrThrow(HolidayInfo.Table, null, values);
        return ActionUri.forHoliday(id);

    }

    private Uri insertIntoServiceTimePeriodTable(ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = db.insertOrThrow(ServiceTimePeriodInfo.Table, null, values);
        return ActionUri.forServiceTimePeriod(id);
    }

    private Uri insertIntoDishTable(ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long id = db.insertOrThrow(DishInfo.Table, null, values);
        return ActionUri.forDish(id);
    }

    @Override
    public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int action = sUriMatcher.match(uri);

        Map<Integer, DbOperation<Integer>> routes = Maps.newHashMap();
        routes.put(Action.GASTRONOMIC_FACILITIES.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteFromGastronomicFacilityTable(selection, selectionArgs);
            }
        });
        routes.put(Action.GASTRONOMIC_FACILITY.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteSingleGastronomicFacility(ContentUris.parseId(uri), selection, selectionArgs);
            }
        });
        routes.put(Action.HOLIDAYS.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteFromHolidayTable(selection, selectionArgs);
            }
        });
        routes.put(Action.HOLIDAY.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteSingleHoliday(ContentUris.parseId(uri), selection, selectionArgs);
            }
        });
        routes.put(Action.SERVICE_TIME_PERIODS.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteFromServiceTimePeriodTable(selection, selectionArgs);
            }
        });
        routes.put(Action.SERVICE_TIME_PERIOD.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteSingleServiceTimePeriod(ContentUris.parseId(uri), selection, selectionArgs);
            }
        });
        routes.put(Action.DISHES.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteFromDishTable(selection, selectionArgs);
            }
        });
        routes.put(Action.DISH.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return deleteSingleDish(ContentUris.parseId(uri), selection, selectionArgs);
            }
        });

        if (!routes.containsKey(action)) {
            String message = "Failed to delete from %s. Deletes for URI are not supported or URI is not supported.";
            throw new IllegalArgumentException(String.format(message, uri));
        }

        if (inBatchOperation()) {
            return routes.get(action).execute();
        }

        try {
            db.beginTransaction();

            int retVal = routes.get(action).execute();
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);

            return retVal;
        } finally {
            db.endTransaction();
        }
    }

    private int deleteFromGastronomicFacilityTable(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.delete(GastronomicFacilityInfo.Table, selection, selectionArgs);
    }

    private int deleteSingleGastronomicFacility(long id, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", GastronomicFacilityInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return deleteFromGastronomicFacilityTable(newSelection, newArgs);
    }

    private int deleteFromHolidayTable(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.delete(HolidayInfo.Table, selection, selectionArgs);
    }

    private int deleteSingleHoliday(long id, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", HolidayInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return deleteFromHolidayTable(newSelection, newArgs);
    }

    private int deleteFromServiceTimePeriodTable(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.delete(ServiceTimePeriodInfo.Table, selection, selectionArgs);
    }

    private int deleteSingleServiceTimePeriod(long id, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", ServiceTimePeriodInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return deleteFromServiceTimePeriodTable(newSelection, newArgs);
    }

    private int deleteFromDishTable(String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.delete(DishInfo.Table, selection, selectionArgs);
    }

    private int deleteSingleDish(long id, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", DishInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return deleteFromDishTable(newSelection, newArgs);
    }

    @Override
    public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int action = sUriMatcher.match(uri);

        Map<Integer, DbOperation<Integer>> routes = Maps.newHashMap();
        routes.put(Action.GASTRONOMIC_FACILITIES.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateGastronomicFacilityTable(values, selection, selectionArgs);
            }
        });
        routes.put(Action.GASTRONOMIC_FACILITY.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateSingleGastronomicFacility(ContentUris.parseId(uri), values, selection, selectionArgs);
            }
        });
        routes.put(Action.HOLIDAYS.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateHolidayTable(values, selection, selectionArgs);
            }
        });
        routes.put(Action.HOLIDAY.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateSingleHoliday(ContentUris.parseId(uri), values, selection, selectionArgs);
            }
        });
        routes.put(Action.SERVICE_TIME_PERIODS.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateServiceTimePeriodTable(values, selection, selectionArgs);
            }
        });
        routes.put(Action.SERVICE_TIME_PERIOD.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateSingleServiceTimePeriod(ContentUris.parseId(uri), values, selection, selectionArgs);
            }
        });
        routes.put(Action.DISHES.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateDishTable(values, selection, selectionArgs);
            }
        });
        routes.put(Action.DISH.ordinal(), new DbOperation<Integer>() {
            @Override
            public Integer execute() {
                return updateSingleDish(ContentUris.parseId(uri), values, selection, selectionArgs);
            }
        });

        if (!routes.containsKey(action)) {
            String message = "Failed to update %s. Updates for URI are not supported or URI is not supported.";
            throw new IllegalArgumentException(String.format(message, uri));
        }

        if (inBatchOperation()) {
            return routes.get(action).execute();
        }

        try {
            db.beginTransaction();

            int retVal = routes.get(action).execute();

            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);

            return retVal;
        } finally {
            db.endTransaction();
        }
    }

    private int updateGastronomicFacilityTable(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.update(GastronomicFacilityInfo.Table, values, selection, selectionArgs);
    }

    private int updateSingleGastronomicFacility(long id, ContentValues values, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", GastronomicFacilityInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return updateGastronomicFacilityTable(values, newSelection, newArgs);
    }

    private int updateHolidayTable(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.update(HolidayInfo.Table, values, selection, selectionArgs);
    }

    private int updateSingleHoliday(long id, ContentValues values, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", HolidayInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return updateHolidayTable(values, newSelection, newArgs);
    }

    private int updateServiceTimePeriodTable(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.update(ServiceTimePeriodInfo.Table, values, selection, selectionArgs);
    }

    private int updateSingleServiceTimePeriod(long id, ContentValues values, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", ServiceTimePeriodInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return updateServiceTimePeriodTable(values, newSelection, newArgs);
    }

    private int updateDishTable(ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.update(DishInfo.Table, values, selection, selectionArgs);
    }

    private int updateSingleDish(long id, ContentValues values, String selection, String[] selectionArgs) {
        final String where = Strings.isNullOrEmpty(selection) ? "{id} = ?" : "{id} = ? AND ({old_selection})";

        String newSelection = Phrase.from(where)
                .put("id", DishInfo.Id)
                .putOptional("old_selection", selection)
                .format()
                .toString();

        String[] newArgs = ArrayUtils.concat(new String[]{String.valueOf(id)}, selectionArgs, String.class);

        return updateDishTable(values, newSelection, newArgs);
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        try {
            db.beginTransaction();
            mInBatchOperation.set(true);

            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }

            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(CONTENT_URI, null);

            return results;
        } finally {
            mInBatchOperation.set(false);
            db.endTransaction();
        }
    }

    private boolean inBatchOperation() {
        return mInBatchOperation.get() != null && mInBatchOperation.get();
    }

    /**
     * Returns the SQLiteOpenHelper that allows direct access to the backing database.
     *
     * @return SQLiteOpenHelper that allows direct access to the backing database.
     */
    SQLiteOpenHelper getOpenHelperForTest() {
        return mOpenHelper;
    }

    private static enum Action {
        GASTRONOMIC_FACILITIES("facilities", "vnd.android.cursor.dir/vnd.zhawcampusinfo.catering.facility"),
        GASTRONOMIC_FACILITY("facilities/#", "vnd.android.cursor.item/vnd.zhawcampusinfo.catering.facility"),
        HOLIDAYS("holidays", "vnd.android.cursor.dir/vnd.zhawcampusinfo.catering.holiday"),
        HOLIDAY("holidays/#", "vnd.android.cursor.item/vnd.zhawcampusinfo.catering.holiday"),
        SERVICE_TIME_PERIODS("servicetimeperiods", "vnd.android.cursor.dir/vnd.zhawcampusinfo.catering.servicetimeperiod"),
        SERVICE_TIME_PERIOD("servicetimeperiods/#", "vnd.android.cursor.item/vnd.zhawcampusinfo.catering.servicetimeperiod"),
        DISHES("dishes", "vnd.android.cursor.dir/vnd.zhawcampusinfo.catering.dish"),
        DISH("dishes/#", "vnd.android.cursor.item/vnd.zhawcampusinfo.catering.dish");
        private final String uri;
        private final String mimeType;

        Action(final String uri, final String mimeType) {
            this.uri = uri;
            this.mimeType = mimeType;
        }

        String uri() {
            return uri;
        }

        String mimeType() {
            return mimeType;
        }
    }

    /**
     * Utility class that helps to build request {@link Uri}s for the {@link CateringContentProvider}.
     */
    public static final class ActionUri {
        /**
         * Returns {@link Uri} that points to the list of gastronomic facilities.
         *
         * @return {@link Uri} that points to the list of gastronomic facilities.
         */
        public static Uri forGastronomicFacilities() {
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, Action.GASTRONOMIC_FACILITIES.uri());
        }

        /**
         * Returns {@link Uri} that points to a single gastronomic facility identified by its rowid.
         *
         * @return {@link Uri} that points to a single gastronomic facility identified by its rowid.
         */
        public static Uri forGastronomicFacility(long id) {
            String uri = Action.GASTRONOMIC_FACILITY.uri().replace("#", Long.toString(id));
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, uri);
        }

        /**
         * Returns {@link Uri} that points to the list of holidays.
         *
         * @return {@link Uri} that points to the list of holidays.
         */
        public static Uri forHolidays() {
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, Action.HOLIDAYS.uri());
        }

        /**
         * Returns {@link Uri} that points to a single holiday identified by its rowid.
         *
         * @return {@link Uri} that points to a single holiday identified by its rowid.
         */
        public static Uri forHoliday(long id) {
            String uri = Action.HOLIDAY.uri().replace("#", Long.toString(id));
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, uri);
        }

        /**
         * Returns {@link Uri} that points to the list of service time periods.
         *
         * @return {@link Uri} that points to the list of service time periods.
         */
        public static Uri forServiceTimePeriods() {
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, Action.SERVICE_TIME_PERIODS.uri());
        }

        /**
         * Returns {@link Uri} that points to a single service time period identified by its rowid.
         *
         * @return {@link Uri} that points to a single service time period identified by its rowid.
         */
        public static Uri forServiceTimePeriod(long id) {
            String uri = Action.SERVICE_TIME_PERIOD.uri().replace("#", Long.toString(id));
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, uri);
        }

        /**
         * Returns {@link Uri} that points to the list of dishes.
         *
         * @return {@link Uri} that points to the list of dishes.
         */
        public static Uri forDishes() {
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, Action.DISHES.uri());
        }

        /**
         * Returns {@link Uri} that points to a single dish identified by its rowid.
         *
         * @return {@link Uri} that points to a single dish identified by its rowid.
         */
        public static Uri forDish(long id) {
            String uri = Action.DISH.uri().replace("#", Long.toString(id));
            return Uri.withAppendedPath(CateringContentProvider.CONTENT_URI, uri);
        }
    }

    /**
     * Meta data for the table that stores
     * {@link com.zhaw.catiejo.whatsforlunch.mensadata.dao.GastronomicFacilityDao} objects. Contains
     * constants that hold the name of the table and columns.
     */
    public static final class GastronomicFacilityInfo {
        /**
         * The name of the table.
         */
        public static final String Table = "gastronomic_facility";

        /**
         * The name of the column that contains the SQLite rowid of the gastronomic facility.
         */
        public static final String Id = "_id";

        /**
         * The name of the column that contains the ID of the gastronomic facility.
         */
        public static final String FacilityId = "facility_id";

        /**
         * The name of the column that contains the type of the gastronomic facility.
         */
        public static final String Type = "type";

        /**
         * The name of the column that contains the name of the gastronomic facility.
         */
        public static final String Name = "name";

        /**
         * The name of the column that contains the location of the gastronomic facility.
         */
        public static final String Location = "location";

        /**
         * The name of the column that contains the date when the gastronomic facility was last updated.
         */
        public static final String LastUpdate = "last_update";

        /**
         * The name of the column that contains the version of the gastronomic facility.
         */
        public static final String Version = "version";
    }

    /**
     * Meta data for the table that stores
     * {@link com.zhaw.catiejo.whatsforlunch.mensadata.dao.HolidayDao} objects. Contains
     * constants that hold the name of the table and columns.
     */
    public static final class HolidayInfo {
        /**
         * The name of the table.
         */
        public static final String Table = "holiday";

        /**
         * The name of the column that contains the SQLite rowid of the holiday.
         */
        public static final String Id = "_id";

        /**
         * The name of the column that contains the ID of the holiday.
         */
        public static final String HolidayId = "holiday_id";

        /**
         * The name of the column that contains the ID of the facility the holiday is assigned to.
         */
        public static String FacilityId = "facility_id";

        /**
         * The name of the column that contains the name of the holiday.
         */
        public static String Name = "name";

        /**
         * The name of the column that contains the start date of the holiday.
         */
        public static String StartsAt = "starts_at";

        /**
         * The name of the column that contains the end date of the holiday.
         */
        public static String EndsAt = "ends_at";

        /**
         * The name of the column that contains the date when the holiday was last updated.
         */
        public static final String LastUpdate = "last_update";

        /**
         * The name of the column that contains the version of the holiday.
         */
        public static final String Version = "version";
    }

    /**
     * Meta data for the table that stores
     * {@link com.zhaw.catiejo.whatsforlunch.mensadata.dao.ServiceTimePeriodDao} objects. Contains
     * constants that hold the name of the table and columns.
     */
    public static class ServiceTimePeriodInfo {
        /**
         * The name of the table.
         */
        public static final String Table = "service_time_period";

        /**
         * The name of the column that contains the SQLite rowid of the service time period.
         */
        public static final String Id = "_id";

        /**
         * The name of the column that contains the ID of the service time period.
         */
        public static final String ServiceTimePeriodId = "service_time_period_id";

        /**
         * The name of the column that contains the ID of the facility the service time period is assigned to.
         */
        public static String FacilityId = "facility_id";

        /**
         * The name of the column that contains the start date of the service time period.
         */
        public static String StartsOn = "starts_on";

        /**
         * The name of the column that contains the end date of the service time period.
         */
        public static String EndsOn = "ends_on";

        /**
         * The name of the column that contains the opening time start on Monday.
         */
        public static String OpeningTimeStartMonday = "opening_time_start_monday";

        /**
         * The name of the column that contains the opening time end on Monday.
         */
        public static String OpeningTimeEndMonday = "opening_time_end_monday";

        /**
         * The name of the column that contains the opening time start on Tuesday.
         */
        public static String OpeningTimeStartTuesday = "opening_time_start_tuesday";

        /**
         * The name of the column that contains the opening time end on Tuesday.
         */
        public static String OpeningTimeEndTuesday = "opening_time_end_tuesday";

        /**
         * The name of the column that contains the opening time start on Wednesday.
         */
        public static String OpeningTimeStartWednesday = "opening_time_start_wednesday";

        /**
         * The name of the column that contains the opening time end on Wednesday.
         */
        public static String OpeningTimeEndWednesday = "opening_time_end_wednesday";

        /**
         * The name of the column that contains the opening time start on Thursday.
         */
        public static String OpeningTimeStartThursday = "opening_time_start_thursday";

        /**
         * The name of the column that contains the opening time end on Thursday.
         */
        public static String OpeningTimeEndThursday = "opening_time_end_thursday";

        /**
         * The name of the column that contains the opening time start on Friday.
         */
        public static String OpeningTimeStartFriday = "opening_time_start_friday";

        /**
         * The name of the column that contains the opening time end on Friday.
         */
        public static String OpeningTimeEndFriday = "opening_time_end_friday";

        /**
         * The name of the column that contains the opening time start on Saturday.
         */
        public static String OpeningTimeStartSaturday = "opening_time_start_saturday";

        /**
         * The name of the column that contains the opening time end on Saturday.
         */
        public static String OpeningTimeEndSaturday = "opening_time_end_saturday";

        /**
         * The name of the column that contains the opening time start on Sunday.
         */
        public static String OpeningTimeStartSunday = "opening_time_start_sunday";

        /**
         * The name of the column that contains the opening time end on Sunday.
         */
        public static String OpeningTimeEndSunday = "opening_time_end_sunday";

        /**
         * The name of the column that contains the lunch time start on Monday.
         */
        public static String LunchTimeStartMonday = "lunch_time_start_monday";

        /**
         * The name of the column that contains the lunch time end on Monday.
         */
        public static String LunchTimeEndMonday = "lunch_time_end_monday";

        /**
         * The name of the column that contains the lunch time start on Tuesday.
         */
        public static String LunchTimeStartTuesday = "lunch_time_start_tuesday";

        /**
         * The name of the column that contains the lunch time end on Tuesday.
         */
        public static String LunchTimeEndTuesday = "lunch_time_end_tuesday";

        /**
         * The name of the column that contains the lunch time start on Wednesday.
         */
        public static String LunchTimeStartWednesday = "lunch_time_start_wednesday";

        /**
         * The name of the column that contains the lunch time end on Wednesday.
         */
        public static String LunchTimeEndWednesday = "lunch_time_end_wednesday";

        /**
         * The name of the column that contains the lunch time start on Thursday.
         */
        public static String LunchTimeStartThursday = "lunch_time_start_thursday";

        /**
         * The name of the column that contains the lunch time end on Thursday.
         */
        public static String LunchTimeEndThursday = "lunch_time_end_thursday";

        /**
         * The name of the column that contains the lunch time start on Friday.
         */
        public static String LunchTimeStartFriday = "lunch_time_start_friday";

        /**
         * The name of the column that contains the lunch time end on Friday.
         */
        public static String LunchTimeEndFriday = "lunch_time_end_friday";

        /**
         * The name of the column that contains the lunch time start on Saturday.
         */
        public static String LunchTimeStartSaturday = "lunch_time_start_saturday";

        /**
         * The name of the column that contains the lunch time end on Saturday.
         */
        public static String LunchTimeEndSaturday = "lunch_time_end_saturday";

        /**
         * The name of the column that contains the lunch time start on Sunday.
         */
        public static String LunchTimeStartSunday = "lunch_time_start_sunday";

        /**
         * The name of the column that contains the lunch time end on Sunday.
         */
        public static String LunchTimeEndSunday = "lunch_time_end_sunday";

        /**
         * The name of the column that contains the date when the service time period was last updated.
         */
        public static final String LastUpdate = "last_update";

        /**
         * The name of the column that contains the version of the service time period.
         */
        public static final String Version = "version";
    }

    /**
     * Meta data for the table that stores
     * {@link com.zhaw.catiejo.whatsforlunch.mensadata.dao.DishDao} objects. Contains
     * constants that hold the name of the table and columns.
     */
    public static class DishInfo {
        /**
         * The name of the table.
         */
        public static final String Table = "dish";

        /**
         * The name of the column that contains the SQLite rowid of the dish.
         */
        public static final String Id = "_id";

        /**
         * The name of the column that contains the ID of the dish.
         */
        public static final String DishId = "dish_id";

        /**
         * The name of the column that contains the ID of the facility that offers the dish.
         */
        public static String FacilityId = "facility_id";

        /**
         * The name of the column that contains the label of the dish.
         */
        public static String Label = "label";

        /**
         * The name of the column that contains the name of the dish.
         */
        public static String Name = "name";

        /**
         * The name of the column that contains the first side dish of the dish.
         */
        public static String FirstSideDish = "first_side_dish";

        /**
         * The name of the column that contains the second side dish of the dish.
         */
        public static String SecondSideDish = "second_side_dish";

        /**
         * The name of the column that contains the third side dish of the dish.
         */
        public static String ThirdSideDish = "third_side_dish";

        /**
         * The name of the column that contains the date the dish is offered on.
         */
        public static String OfferedOn = "offered_on";

        /**
         * The name of the column that contains the internal price.
         */
        public static String InternalPrice = "internal_price";

        /**
         * The name of the column that contains the price for partners.
         */
        public static String PriceForPartners = "price_for_partners";

        /**
         * The name of the column that contains the external price.
         */
        public static String ExternalPrice = "external_price";

        /**
         * The name of the column that contains the date when the dish was last updated.
         */
        public static final String LastUpdate = "last_update";

        /**
         * The name of the column that contains the version of the dish.
         */
        public static final String Version = "version";
    }
}