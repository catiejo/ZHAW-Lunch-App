package com.zhaw.catiejo.whatsforlunch._campusinfo.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.zhaw.catiejo.whatsforlunch._campusinfo.sync.CateringSyncAdapter;

/**
 * Provides the necessary service for the automatic schedule syncing. The actual sync process is controlled by the
 * {@link ch.zhaw.init.android.campusinfo.ui.adapter.ScheduleSyncAdapter}.
 */
public class CateringSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static CateringSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new CateringSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
