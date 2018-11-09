package com.zhaw.catiejo.whatsforlunch.mensadata.helper;

import org.joda.time.DateTimeZone;

import java.util.Locale;

/**
 * Various constants used within the app.
 */
public final class Constants {

    /**
     * Dummy user name used to identify guest accounts.
     */
    public static final String GUEST_ACCOUNT_NAME = "guest@campusinfo.init.zhaw.ch";

    /**
     * Identifies a system account related to the CampusInfo App.
     */
    public static final String ACCOUNT_TYPE = "ch.zhaw.init.android.campusinfo.account";

    /**
     * Type of the auth token used by the app's authentication system.
     */
    public static final String AUTHTOKEN_TYPE = "authtokenType";

    /**
     * Time zone of the ZHAW. Should be used for all calculations because the whole logic relies on the local time zone
     * of the ZHAW.
     */
    public static final DateTimeZone LocalTimeZone = DateTimeZone.forID("Europe/Zurich");

    /**
     * Identifies a {@link ch.zhaw.init.android.campusinfo.dataaccess.dao.account.ZhawUserType} in an
     * {@link android.content.Intent} or {@link android.os.Bundle}.
     */
    public static final String ZHAW_USER_TYPE = "ZHAW_USER_TYPE";

    /**
     * Identifies a {@link ch.zhaw.init.android.campusinfo.ui.DayScheduleSelector} in an {@link android.content.Intent}
     * or {@link android.os.Bundle}.
     */
    public static final String DAY_SCHEDULE_SELECTOR = "DAY_SCHEDULE_SELECTOR";

    /**
     * Identifies a {@link ch.zhaw.init.android.campusinfo.ui.loader.RemoteAccountVerificationRequest} in an
     * {@link android.content.Intent} or {@link android.os.Bundle}.
     */
    public static final String REMOTE_ACCOUNT_VERIFICATION_REQUEST = "REMOTE_ACCOUNT_VERIFICATION_REQUEST";

    /**
     * Identifies the event to display in a {@link android.content.Intent} or {@link android.os.Bundle}.
     */
    public static final String DAY_SCHEDULE_EVENT = "DAY_SCHEDULE_EVENT";

    /**
     * Number of months the schedule time will extend into the past.
     */
    public static final int SCHEDULE_TIMELINE_MONTHS_INTO_PAST = 6;

    /**
     * Number of months the schedule time will extend into the future.
     */
    public static final int SCHEDULE_TIMELINE_MONTHS_INTO_FUTURE = 12;

    /**
     * Identifies a request (to be used with {@code startActivityForResult()} for the selection of a date to the
     * {@link ch.zhaw.init.android.campusinfo.ui.DaySchedulePickerActivity}.
     */
    public static final int DAY_SCHEDULE_PICKER_REQUEST = 1;

    /**
     * Maximum age of catering information in days before it has to be refreshed.
     */
    public static final int MAX_CATERING_INFORMATION_AGE = 7;

    /**
     * Maximum age of a schedule in days before it has to be refreshed.
     */
    public static final int MAX_SCHEDULE_AGE = 3;

    /**
     * Maximum age of a schedule index in days before it hast to be refreshed.
     */
    public static final int MAX_INDEX_AGE = 3;

    /**
     * Number of weeks of day schedules to sync before today.
     */
    public static final int SYNC_WEEKS_BEFORE_TODAY = 1;

    /**
     * Number of weeks of day schedules to sync after today.
     */
    public static final int SYNC_WEEKS_AFTER_NOW = 2;

    /**
     * Number of days to download with every schedule request.
     */
    public static final int DAYS_TO_DOWNLOAD = 7;

    /**
     * Identifies a {@link ch.zhaw.init.android.campusinfo.ui.MenuSelector} in an {@link android.content.Intent}
     * or {@link android.os.Bundle}.
     */
    public static final String MENU_SELECTOR = "MENU_SELECTOR";

    /**
     * Default locale for the app, used e.g. for number formatting.
     */
    public static final Locale LOCAL_LOCALE = new Locale("de", "CH");

    /**
     * Name of the SharedPreferences file that stores the app's version and related information that is used for the
     * update checks.
     */
    public static final String APP_INFO_SHARED_PREFS = "APP_INFO_SHARED_PREFS";

    /**
     * Name of the key within the APP_INFO_SHARED_PREFS that stores the version of the app that was launched the last
     * time before the current execution.
     */
    public static final String APP_INFO_VERSION_LAST_RAN = "APP_INFO_VERSION_LAST_RAN";

    /**
     * Name of the SharedPreferences file that stores the names of the last used stations.
     */
    public static final String TRANSPORT_LAST_STATIONS_PREFS = "TRANSPORT_LAST_STATIONS_PREFS";

    /**
     * Name of the key within the TRANSPORT_LAST_STATIONS_PREFS that stores the last stations that served as origins.
     */
    public static final String TRANSPORT_LAST_STATIONS_ORIGINS = "TRANSPORT_LAST_STATION_ORIGINS";

    /**
     * Name of the key within the TRANSPORT_LAST_STATIONS_PREFS that stores the last stations that served as
     * destinations.
     */
    public static final String TRANSPORT_LAST_STATIONS_DESTINATIONS = "TRANSPORT_LAST_STATION_DESTINATIONS";

    /**
     * Maximum number of last selected stations to display when picking a public transport station.
     */
    public static final int TRANSPORT_MAX_LAST_STATIONS = 4;

    /**
     * Name of the {@link android.content.SharedPreferences} file that stores the preferences for this activity.
     */
    public static final String DAY_SCHEDULE_ACTIVITY_PREFS = "DAY_SCHEDULE_ACTIVITY_PREFS";

    /**
     * Name of the key within DAY_SCHEDULE_ACTIVITY_PREFS that stores the email address of the last selected account.
     */
    public static final String PREFS_LAST_SELECTED_ACCOUNT_EMAIL = "PREFS_LAST_SELECTED_ACCOUNT_EMAIL";
}
