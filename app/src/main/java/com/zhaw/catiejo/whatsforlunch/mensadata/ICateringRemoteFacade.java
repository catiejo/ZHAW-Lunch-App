package com.zhaw.catiejo.whatsforlunch.mensadata;

import com.zhaw.catiejo.whatsforlunch.mensadata.dto.GastronomicFacilityListDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.MenuPlanListDto;

/**
 * Facade to the remote Catering API.
 */
public interface ICateringRemoteFacade {

    /**
     * Gets the list of gastronomic facilities including their holidays and service times.
     *
     * @return List of gastronomic facilities.
     */
    CateringRemoteFacadeResult<GastronomicFacilityListDto> getGastronomicFacilities();

    /**
     * Gets the list of menu plans for all gastronomic facilities that are offered in the given week of the given year.
     *
     * @param year Year of the calendar week.
     * @param week Week of the calendar week.
     * @return List of menu plans.
     */
    CateringRemoteFacadeResult<MenuPlanListDto> getMenuPlans(int year, int week);
}
