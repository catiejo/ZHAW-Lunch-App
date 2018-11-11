package com.zhaw.catiejo.whatsforlunch;

import android.content.Context;

import com.zhaw.catiejo.whatsforlunch.mensadata.CateringController;
import com.zhaw.catiejo.whatsforlunch.mensadata.ICateringController;
import com.zhaw.catiejo.whatsforlunch.mensadata.sync.ICateringInformationAvailabilityManager;
import dagger.Provides;
import javax.inject.Singleton;

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
}

