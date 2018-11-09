package com.zhaw.catiejo.whatsforlunch.mensadata;

import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.HolidayDao;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.ServiceTimePeriodDao;
import com.google.common.base.Optional;
import org.joda.time.LocalDate;

/**
 * Provides catering-related operations.
 */
public interface ICateringController {
    /**
     * Returns the list of canteens including their service times.
     *
     * @param day Local date of the day which the service times should be included for.
     * @return Cursor wrapping the list of canteens.
     */
    Cursor getCanteens(LocalDate day);

    /**
     * Returns the list of cafeterias including their service times.
     *
     * @param day Local date of the day which the service times should be included for.
     * @return Cursor wrapping the list of cafeterias.
     */
    Cursor getCafeterias(LocalDate day);

    /**
     * Returns the menu (a list of dishes) offered by the given facility, identified by its ID, on the given day.
     *
     * @param facilityId ID of the facility.
     * @param day Local date of the day which the menu should be displayed for.
     * @return Cursor wrapping the list of dishes comprising the menu.
     */
    Cursor getMenu(long facilityId, LocalDate day);

    /**
     * Returns the service time period for the given facility, identified by its ID, that is valid for the given day.
     *
     * @param facilityId ID of the facility.
     * @param day Local date of the day.
     * @return Found service time period or {@link com.google.common.base.Optional#absent()}.
     */
    Optional<ServiceTimePeriodDao> getServiceTimePeriod(long facilityId, LocalDate day);

    /**
     * Synchronizes local database with catering information with remote service.
     */
    void syncCateringInformation();

    /**
     * Returns a holiday if a holiday falls on the given day and the facility is closed during that holiday. Otherwise,
     * {@link com.google.common.base.Optional#absent()} is returned.
     *
     * @param facilityId ID of the facility.
     * @param day Local date of the day.
     * @return Found holiday or {@link com.google.common.base.Optional#absent()}.
     */
    Optional<HolidayDao> getHoliday(long facilityId, LocalDate day);
}
