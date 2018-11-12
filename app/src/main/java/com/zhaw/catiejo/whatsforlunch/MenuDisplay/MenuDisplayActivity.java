package com.zhaw.catiejo.whatsforlunch.MenuDisplay;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.squareup.otto.Bus;
import com.squareup.phrase.Phrase;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.MensaPicker.MensaPickerActivity;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch.WhatsForLunchApplication;
import com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.DishDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.HolidayDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.ServiceTimePeriodDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MenuDisplayActivity extends AppCompatActivity {
    @Inject
    ICateringController mCateringController;
    @Inject
    Bus bus;

    private MenuAdapter mMenuAdapter;
    private MenuContentObserver mMenuContentObserver;
    private LoadMenuTask mLoadMenuTask;
    private MensaContainer mMensa; //The mensa we're currently viewing
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        ((WhatsForLunchApplication) getApplication()).inject(this);


        this.setContentView(R.layout.activity_menu_display);

        mMenuAdapter = new MenuAdapter(this, null, 0);
        final ListView list = (ListView) findViewById(R.id.menuList);
        list.setAdapter(mMenuAdapter);

        mMensa = (MensaContainer) getIntent().getSerializableExtra(Constants.MENU_SELECTOR);
        setUpToolbar();

        mFab = (FloatingActionButton) findViewById(R.id.fab2);
//        mFab.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();

        getContentResolver().unregisterContentObserver(mMenuContentObserver);

        if (mLoadMenuTask != null && !mLoadMenuTask.isCancelled()) {
            mLoadMenuTask.cancel(true);
            mLoadMenuTask = null;
        }
        // Explicitly release the cursor. Otherwise these nasty "cursor not finalized" warnings pop up. Although I must
        // admit I don't know why.
        if (mMenuAdapter != null) {
            mMenuAdapter.changeCursor(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMenuContentObserver = new MenuContentObserver(new Handler(Looper.getMainLooper()));
        getContentResolver().registerContentObserver(CateringContentProvider.CONTENT_URI,
                true, mMenuContentObserver);
        reloadMenu();
    }

    private void reloadMenu() {
        mLoadMenuTask = new LoadMenuTask();
        mLoadMenuTask.execute();
    }


//    @Override
//    protected void onStart() {
//        Log.e("CJ", "tart!");
//        super.onStart();
//        bus.register(this);
//
//        mMenuContentObserver = new MenuDisplayActivity.MenuContentObserver(new Handler(Looper.getMainLooper()));
//        getContentResolver().registerContentObserver(CateringContentProvider.CONTENT_URI,
//                true, mMenuContentObserver);
//        mLoadMenuTask = new MenuDisplayActivity.LoadMenuTask();
//        mLoadMenuTask.execute();
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        bus.unregister(this);
//
//        getContentResolver().unregisterContentObserver(mMenuContentObserver);
//
//        if (mLoadMenuTask != null && !mLoadMenuTask.isCancelled()) {
//            mLoadMenuTask.cancel(true);
//            mLoadMenuTask = null;
//        }
//    }

    private void setUpToolbar() {
        ActionBar toolbar = getSupportActionBar();
        toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setDisplayHomeAsUpEnabled(true);
        if (mMensa == null) {
            toolbar.setTitle("NULL");
        } else {
            toolbar.setTitle(mMensa.getName());
        }
    }

    private class LoadMenuTask extends AsyncTask<Void, Void, Optional<Cursor>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Optional<Cursor> doInBackground(Void... params) {
            Log.e("CJ", mMensa.getName());
            return Optional.fromNullable(mCateringController.getMenu(mMensa.getFacilityId(), mMensa.getDay()));
        }

        @Override
        protected void onPostExecute(Optional<Cursor> optionalCursor) {
            Log.e("CJ", "post");
            if (this.isCancelled() || mMenuAdapter == null) {
                return;
            }
            if (optionalCursor.get().moveToFirst()) {
                DishDao dish = DishDao.fromCursor(optionalCursor.get());
                Log.e("CJ", dish.toString());
            }
            Log.e("CJ", DatabaseUtils.dumpCursorToString(optionalCursor.get()));
            mMenuAdapter.changeCursor(optionalCursor.orNull());
        }

        @Override
        protected void onCancelled() {
        }
    }

    private class MenuContentObserver extends ContentObserver {

        public MenuContentObserver(Handler handler) {
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
            mLoadMenuTask = new LoadMenuTask();
            mLoadMenuTask.execute();
        }
    }
}


