package com.zhaw.catiejo.whatsforlunch.MensaPicker;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.common.base.Optional;
import com.squareup.otto.Bus;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch.WhatsForLunchApplication;
import com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;

import org.joda.time.LocalDate;

import javax.inject.Inject;

public class MensaPickerActivity extends AppCompatActivity {
    @Inject
    ICateringController cateringController;
    @Inject
    Bus bus;

    private MensaCardAdapter mMensaAdapter;
    private CanteenContentObserver canteenContentObserver;
    private LoadCanteensTask loadCanteensTask;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        ((WhatsForLunchApplication) getApplication()).inject(this);

        this.setContentView(R.layout.activity_mensa_picker);

        mMensaAdapter = new MensaCardAdapter(this, null, 0);

        final ListView list = (ListView) findViewById(R.id.mensaList);
        list.setAdapter(mMensaAdapter);

        setUpToolbar();

    }

    @Override
    protected void onStart() {
        super.onStart();
        bus.register(this);

        canteenContentObserver = new CanteenContentObserver(new Handler(Looper.getMainLooper()));
        getContentResolver().registerContentObserver(CateringContentProvider.CONTENT_URI,
                true, canteenContentObserver);
        loadCanteensTask = new LoadCanteensTask();
        loadCanteensTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bus.unregister(this);

        getContentResolver().unregisterContentObserver(canteenContentObserver);

        if (loadCanteensTask != null && !loadCanteensTask.isCancelled()) {
            loadCanteensTask.cancel(true);
            loadCanteensTask = null;
        }
    }

    private void setUpToolbar() {
        ActionBar toolbar = getSupportActionBar();
        toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.mensaPicker);
    }

    private class LoadCanteensTask extends AsyncTask<Void, Void, Optional<Cursor>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Optional<Cursor> doInBackground(Void... params) {
            return Optional.fromNullable(cateringController.getCanteens(LocalDate.now(Constants.LocalTimeZone)));
        }

        @Override
        protected void onPostExecute(Optional<Cursor> optionalCursor) {
            if (this.isCancelled() || mMensaAdapter == null) {
                return;
            }
            mMensaAdapter.swapCursor(optionalCursor.orNull());
        }

        @Override
        protected void onCancelled() {
        }
    }

    private class CanteenContentObserver extends ContentObserver {

        public CanteenContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            loadCanteensTask = new LoadCanteensTask();
            loadCanteensTask.execute();
        }
    }


}