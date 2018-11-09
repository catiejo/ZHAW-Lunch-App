package com.zhaw.catiejo.whatsforlunch.mensadata.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider;
import com.zhaw.catiejo.whatsforlunch.mensadata.CateringRemoteFacadeResult;
import com.zhaw.catiejo.whatsforlunch.mensadata.ICateringRemoteFacade;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.GastronomicFacilityDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.GastronomicFacilityListDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.MenuPlanDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.MenuPlanListDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.helper.Constants;
//import ch.zhaw.init.android.campusinfo.util.HockeyAppUtil;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.phrase.Phrase;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.*;
import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringRemoteFacadeResult.Outcome;

/**
 * Controls the download of catering information.
 */
public class CateringInformationAvailabilityManager implements ICateringInformationAvailabilityManager {

    private static final String TAG = CateringInformationAvailabilityManager.class.getCanonicalName();

    /**
     * Grace period between subsequent downloads.
     */
    private static final int DOWNLOAD_GRACE_PERIOD = 60;

    private final ExecutorService executorService;

    private final Context context;

    private final Bus bus;

    private final ICateringRemoteFacade cateringRemoteFacade;

    /**
     * Time when the last download attempt for facility information and menu plans was executed. To be guarded by this.
     */
    private DateTime lastFullDownload = DateTime.now().minusSeconds(DOWNLOAD_GRACE_PERIOD);

    /**
     * Time when the last download attempt for menu plans only was executed. To be guarded by this.
     */
    private DateTime lastMenuPlanDownload = DateTime.now().minusSeconds(DOWNLOAD_GRACE_PERIOD);

    /**
     * Current state of the catering information. To be guarded by this.
     */
    private CateringInformationState state = CateringInformationState.AVAILABLE;

    /**
     * Creates new instance with the given context, event bus and facade to the catering service. Uses a cached thread
     * pool to execute tasks in the background.
     *
     * @param context              Application context.
     * @param bus                  Bus to post events to.
     * @param cateringRemoteFacade Facade to the catering service.
     */
    public CateringInformationAvailabilityManager(final Context context, final Bus bus,
                                                  final ICateringRemoteFacade cateringRemoteFacade) {
        this(context, bus, cateringRemoteFacade, new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()));
    }

    /**
     * Creates new instance with the given context, event bus, facade to the catering service and executor service that
     * is used to execute tasks in the background.
     *
     * @param context              Application context.
     * @param bus                  Bus to post events to.
     * @param cateringRemoteFacade Facade to the catering service.
     * @param executorService      Executor service to execute background tasks.
     */
    CateringInformationAvailabilityManager(final Context context, final Bus bus, final ICateringRemoteFacade cateringRemoteFacade,
                                           final ExecutorService executorService) {
        this.context = context;
        this.bus = bus;
        this.cateringRemoteFacade = cateringRemoteFacade;
        this.executorService = executorService;

        bus.register(this);
    }

    @Override
    public void ensureCateringInformationAvailability() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isGastronomicFacilityInformationUpToDate()) {
                        refreshCateringInformation();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to ensure availability of catering information.", e);
//                    HockeyAppUtil.logException(context, e);
                }
            }
        };
        executorService.submit(r);
    }

    @Override
    public void ensureMenuPlanAvailability() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isDishInformationUpToDate()) {
                        refreshDishInformation();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to ensure menu plan availability.", e);
//                    HockeyAppUtil.logException(context, e);
                }
            }
        };
        executorService.submit(r);
    }

    @Override
    public void syncCateringInformation() {
        try {
            refreshCateringInformation();
        } catch (Exception e) {
            throw new CateringInformationAvailabilityManagerException(e);
        }
    }

    @Override
    @Produce
    public CateringInformationAvailabilityNotification produceState() {
        return new CateringInformationAvailabilityNotification(state);
    }

    private boolean isGastronomicFacilityInformationUpToDate() {
        Cursor cursor = null;
        try {
            String selection = Phrase.from("{last_update} > ?")
                    .put("last_update", GastronomicFacilityInfo.LastUpdate)
                    .format()
                    .toString();
            String[] selectionArgs = new String[]{
                    DateMidnight.now(Constants.LocalTimeZone).minusDays(Constants.MAX_CATERING_INFORMATION_AGE).toString()
            };

            cursor = context.getContentResolver().query(ActionUri.forGastronomicFacilities(), null, selection,
                    selectionArgs, null);

            if (cursor == null) {
                throw new CateringInformationSynchronizationException("Cursor was unexpectedly null. ActionUri misconfiguration?");
            }

            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean isDishInformationUpToDate() {
        Cursor cursor = null;
        try {
            String selection = Phrase.from("{offered_on} = ?")
                    .put("offered_on", DishInfo.OfferedOn)
                    .format()
                    .toString();

            String[] selectionArgs = new String[]{LocalDate.now(Constants.LocalTimeZone).toString()};

            cursor = context.getContentResolver().query(ActionUri.forDishes(), null, selection,
                    selectionArgs, null);

            if (cursor == null) {
                throw new CateringInformationSynchronizationException("Cursor was unexpectedly null. ActionUri misconfiguration?");
            }

            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private synchronized void refreshCateringInformation() throws Exception {
        // If a download happened during the last DOWNLOAD_GRACE_PERIOD seconds, do not try it again.
        if (lastFullDownload.plusSeconds(DOWNLOAD_GRACE_PERIOD).compareTo(DateTime.now()) > 0) {
            return;
        }

        try {
            state = CateringInformationState.DOWNLOAD_IN_PROGRESS;
            bus.post(new CateringInformationAvailabilityNotification(state));

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            operations.addAll(getOperationsForFacilitySync());
            operations.addAll(getOperationsForMenuPlanSync());
            context.getContentResolver().applyBatch(CateringContentProvider.AUTHORITY, operations);

            state = CateringInformationState.AVAILABLE;
            bus.post(new CateringInformationAvailabilityNotification(state));

            lastFullDownload = DateTime.now();
        } catch (Exception e) {
            state = CateringInformationState.DOWNLOAD_FAILED;
            bus.post(new CateringInformationAvailabilityNotification(state));

            throw e;
        }
    }

    private synchronized void refreshDishInformation() throws Exception {
        // If a download happened during the last DOWNLOAD_GRACE_PERIOD seconds, do not try it again.
        if (lastFullDownload.plusSeconds(DOWNLOAD_GRACE_PERIOD).compareTo(DateTime.now()) > 0 ||
                lastMenuPlanDownload.plusSeconds(DOWNLOAD_GRACE_PERIOD).compareTo(DateTime.now()) > 0) {
            return;
        }

        try {
            state = CateringInformationState.DOWNLOAD_IN_PROGRESS;
            bus.post(new CateringInformationAvailabilityNotification(state));

            // refresh data
            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            operations.addAll(getOperationsForMenuPlanSync());
            context.getContentResolver().applyBatch(CateringContentProvider.AUTHORITY, operations);

            state = CateringInformationState.AVAILABLE;
            bus.post(new CateringInformationAvailabilityNotification(state));

            lastMenuPlanDownload = DateTime.now();
        } catch (Exception e) {
            state = CateringInformationState.DOWNLOAD_FAILED;
            bus.post(new CateringInformationAvailabilityNotification(state));

            throw e;
        }
    }

    private ArrayList<ContentProviderOperation> getOperationsForFacilitySync() {
        CateringRemoteFacadeResult<GastronomicFacilityListDto> result = cateringRemoteFacade.getGastronomicFacilities();

        if (result.getOutcome() == Outcome.ERROR) {
            throw new CateringInformationSynchronizationException("Download of facility information failed.");
        }

        ImmutableCollection<GastronomicFacilityDto> facilityDtos = ImmutableList.of();
        if (result.getResult().isPresent()) {
            facilityDtos = result.getResult().get().getGastronomicFacilities();
        }

        ArrayList<ContentProviderOperation> operations = Lists.newArrayList();

        GastronomicFacilityUnidirectionalSyncOperation facilitySyncOperation = new GastronomicFacilityUnidirectionalSyncOperation(context);
        operations.addAll(facilitySyncOperation.prepareOperations(facilityDtos));

        HolidayUnidirectionalSyncOperation holidaySyncOperation = new HolidayUnidirectionalSyncOperation(context);
        operations.addAll(holidaySyncOperation.prepareOperations(facilityDtos));

        ServiceTimePeriodUnidirectionalSyncOperation serviceTimePeriodSyncOperation = new ServiceTimePeriodUnidirectionalSyncOperation(context);
        operations.addAll(serviceTimePeriodSyncOperation.prepareOperations(facilityDtos));

        return operations;
    }

    private ArrayList<ContentProviderOperation> getOperationsForMenuPlanSync() {
        DateTime thisWeek = DateTime.now(Constants.LocalTimeZone);
        DateTime nextWeek = thisWeek.plusWeeks(1);

        CateringRemoteFacadeResult<MenuPlanListDto> resultThisWeek = cateringRemoteFacade.getMenuPlans(
                thisWeek.getWeekyear(), thisWeek.getWeekOfWeekyear());
        CateringRemoteFacadeResult<MenuPlanListDto> resultNextWeek = cateringRemoteFacade.getMenuPlans(
                nextWeek.getWeekyear(), nextWeek.getWeekOfWeekyear());

        if (resultThisWeek.getOutcome() == Outcome.ERROR || resultNextWeek.getOutcome() == Outcome.ERROR) {
            throw new CateringInformationSynchronizationException("Download of menu plans failed.");
        }

        ImmutableList.Builder<MenuPlanDto> builder = new ImmutableList.Builder<MenuPlanDto>();
        if (resultThisWeek.getOutcome() == Outcome.SUCCESS && resultThisWeek.getResult().isPresent()) {
            builder.addAll(resultThisWeek.getResult().get().getMenuPlans());
        }
        if (resultNextWeek.getOutcome() == Outcome.SUCCESS && resultNextWeek.getResult().isPresent()) {
            builder.addAll(resultNextWeek.getResult().get().getMenuPlans());
        }

        DishUnidirectionalSyncOperation dishSyncOperation = new DishUnidirectionalSyncOperation(context);
        return Lists.newArrayList(dishSyncOperation.prepareOperations(builder.build()));
    }
}
