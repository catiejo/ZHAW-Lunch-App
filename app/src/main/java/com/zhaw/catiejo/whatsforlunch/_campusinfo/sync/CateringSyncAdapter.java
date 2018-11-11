package com.zhaw.catiejo.whatsforlunch._campusinfo.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.zhaw.catiejo.whatsforlunch.WhatsForLunchApplication;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.DaggerContainer;
import io.reactivex.Single;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Executes the periodic synchronization of catering information triggered by the operating system.
 */
public class CateringSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = CateringSyncAdapter.class.getCanonicalName();

    @Inject
    ICateringController mCateringController;

    @Inject
    @Named("securityProviderInstallation")
    Single<Boolean> securityProviderInstallation;

    public CateringSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        ((DaggerContainer) context.getApplicationContext()).inject(this);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Going to sync catering information.");

        try {
            // Will throw if there's an error
            securityProviderInstallation.blockingGet();

            mCateringController.syncCateringInformation();

            Log.i(TAG, "Catering information synchronized.");
        } catch (Exception e) {
            Log.e(TAG, "Catering information synchronization failed.", e);
//            HockeyAppUtil.logException(getContext(), e);
        }
    }
}
