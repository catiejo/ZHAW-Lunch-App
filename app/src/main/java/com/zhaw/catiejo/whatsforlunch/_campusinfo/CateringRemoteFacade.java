package com.zhaw.catiejo.whatsforlunch._campusinfo;

import android.content.Context;
import android.util.Log;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dto.GastronomicFacilityListDto;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dto.MenuPlanListDto;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit.IRetrofitProxyFactory;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit.JacksonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.base.Optional;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

import javax.inject.Inject;

import static com.zhaw.catiejo.whatsforlunch._campusinfo.CateringRemoteFacadeResult.Outcome;

/**
 * Facade to the remote Catering API.
 */
public class CateringRemoteFacade implements ICateringRemoteFacade {

    private static final String TAG = CateringRemoteFacade.class.getCanonicalName();

    /**
     * User agent to transmit with every request. Required by remote web service.
     */
    private static final String USER_AGENT_HEADER = "User-Agent: ZHAWCampusInfoForAndroid (http://www.init.zhaw.ch)";

    private final Context context;

    private final IRetrofitProxyFactory retrofitProxyFactory;

    @Inject
    public CateringRemoteFacade(Context context, IRetrofitProxyFactory retrofitProxyFactory) {
        this.context = context;
        this.retrofitProxyFactory = retrofitProxyFactory;
    }

    /**
     * Proxy interface for Retrofit.
     */
    private interface CateringProxy {
        @GET("/v1/catering/facilities")
        @Headers({USER_AGENT_HEADER, "Accept: application/json"})
        GastronomicFacilityListDto getGastronomicFacilityList();

        @Headers({USER_AGENT_HEADER, "Accept: application/json"})
        @GET("/v1/catering/menuplans/years/{year}/weeks/{week}")
        MenuPlanListDto getMenuPlan(@Path("year") int year, @Path("week") int week);
    }

    @Override
    public CateringRemoteFacadeResult<GastronomicFacilityListDto> getGastronomicFacilities() {
        try {
            CateringProxy cateringProxy = retrofitProxyFactory.createProxy(context.getString(R.string.endpoint_catering),
                    CateringProxy.class, new JacksonConverter(new ObjectMapper().registerModule(new JodaModule())));

            return new CateringRemoteFacadeResult<GastronomicFacilityListDto>(Outcome.SUCCESS,
                    Optional.fromNullable(cateringProxy.getGastronomicFacilityList()));
        } catch (RetrofitError retrofitError) {
            Log.e(TAG, "Request for list of gastronomic facilities failed.", retrofitError);
//            HockeyAppUtil.logException(context, retrofitError);

            if (!retrofitError.isNetworkError() && retrofitError.getResponse().getStatus() == 404) {
                return new CateringRemoteFacadeResult<GastronomicFacilityListDto>(Outcome.NOT_FOUND,
                        Optional.<GastronomicFacilityListDto>absent());
            }

            return new CateringRemoteFacadeResult<GastronomicFacilityListDto>(Outcome.ERROR,
                    Optional.<GastronomicFacilityListDto>absent());
        }
    }

    @Override
    public CateringRemoteFacadeResult<MenuPlanListDto> getMenuPlans(int year, int week) {
        try {
            CateringProxy cateringProxy = retrofitProxyFactory.createProxy(context.getString(R.string.endpoint_catering),
                    CateringProxy.class, new JacksonConverter(new ObjectMapper().registerModule(new JodaModule())));

            return new CateringRemoteFacadeResult<MenuPlanListDto>(Outcome.SUCCESS,
                    Optional.fromNullable(cateringProxy.getMenuPlan(year, week)));
        } catch (RetrofitError retrofitError) {
            Log.e(TAG, "Request for menu plan list failed.", retrofitError);
//            HockeyAppUtil.logException(context, retrofitError);

            if (!retrofitError.isNetworkError() && retrofitError.getResponse().getStatus() == 404) {
                return new CateringRemoteFacadeResult<MenuPlanListDto>(Outcome.NOT_FOUND, Optional.<MenuPlanListDto>absent());
            }

            return new CateringRemoteFacadeResult<MenuPlanListDto>(Outcome.ERROR, Optional.<MenuPlanListDto>absent());
        }
    }
}
