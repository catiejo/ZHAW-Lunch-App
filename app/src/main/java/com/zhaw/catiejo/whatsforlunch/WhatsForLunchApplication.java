package com.zhaw.catiejo.whatsforlunch;

import android.app.Application;
import android.content.Context;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.zhaw.catiejo.whatsforlunch.DayPicker.DayPickerActivity;
import com.zhaw.catiejo.whatsforlunch.MensaPicker.MensaPickerActivity;
import com.zhaw.catiejo.whatsforlunch.MenuDisplay.MenuDisplayActivity;
import com.zhaw.catiejo.whatsforlunch._campusinfo.CateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.CateringRemoteFacade;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringRemoteFacade;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.DaggerContainer;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit.DefaultRetrofitProxyFactory;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.retrofit.IRetrofitProxyFactory;
import com.zhaw.catiejo.whatsforlunch._campusinfo.sync.CateringInformationAvailabilityManager;
import com.zhaw.catiejo.whatsforlunch._campusinfo.sync.ICateringInformationAvailabilityManager;
import java.io.File;
import java.io.IOException;
import java.net.ResponseCache;
import javax.inject.Singleton;
import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

// Taken from the CampusInfo app and adapted to suit my use case.
public class WhatsForLunchApplication extends Application implements DaggerContainer {
    private ObjectGraph mGraph;

    public void onCreate() {
        super.onCreate();
        mGraph = ObjectGraph.create(new MensaHelper(this));
        mGraph.inject(this); //the class in CampusInfo implements DaggerContainer and has an override
    }

    @Override
    public void inject(Object object) {
        mGraph.inject(object);
    }
}

@Module(injects = {
        WhatsForLunchApplication.class,
        MensaPickerActivity.class,
        DayPickerActivity.class,
        MenuDisplayActivity.class
})
final class MensaHelper {
    private final Context appContext;
    MensaHelper(Context ctx) {
        appContext = ctx;
    }

    @Provides
    @Singleton
    ICateringController provideCateringController(ICateringInformationAvailabilityManager cateringInformationAvailabilityManager) {
        return new CateringController(this.appContext, cateringInformationAvailabilityManager);
    }

    @Provides
    @Singleton
    ICateringInformationAvailabilityManager provideCateringInformationAvailabilityManager(
            Bus bus, ICateringRemoteFacade cateringRemoteFacade) {
        return new CateringInformationAvailabilityManager(this.appContext, bus, cateringRemoteFacade);
    }

    @Provides
    @Singleton
    ICateringRemoteFacade provideCateringRemoteFacade(IRetrofitProxyFactory retrofitProxyFactory) {
        return new CateringRemoteFacade(this.appContext, retrofitProxyFactory);
    }

    @Provides
    @Singleton
    IRetrofitProxyFactory provideRetrofitProxyFactory(ResponseCache responseCache) {
        return new DefaultRetrofitProxyFactory(responseCache);
    }

    @Provides
    @Singleton
    ResponseCache provideResponseCache() {
        try {
            File httpCacheDir = new File(this.appContext.getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            return new HttpResponseCache(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus(ThreadEnforcer.ANY);
    }
}

