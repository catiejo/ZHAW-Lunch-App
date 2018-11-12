package com.zhaw.catiejo.whatsforlunch.MenuDisplay;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.google.common.base.Optional;
import com.squareup.otto.Bus;
import com.zhaw.catiejo.whatsforlunch.DayPicker.DayPickerActivity;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.MensaPicker.MensaPickerActivity;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch.WhatsForLunchApplication;
import com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import javax.inject.Inject;

public class MenuDisplayActivity extends AppCompatActivity {
    // Injections required for syncing with server
    @Inject
    ICateringController mCateringController;
    @Inject
    Bus bus;

    private MenuContentObserver mMenuContentObserver;
    private LoadMenuTask mLoadMenuTask;
    private MensaContainer mMensa; //The mensa we're currently viewing
    private Button mExtendedFab;

    /* RecyclerView Variables */
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MenuDisplayAdapter mMenuDisplayAdapter;


    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        // Required to access the catering servers
        ((WhatsForLunchApplication) getApplication()).inject(this);

        this.setContentView(R.layout.activity_menu_display);
        // Extract the current mensa information (note: can be null!)
        mMensa = (MensaContainer) getIntent().getSerializableExtra(Constants.MENU_SELECTOR);

        // Get RecyclerView instance
        mRecyclerView = (RecyclerView) findViewById(R.id.menuRecycler);
        // Snaps the cards to the middle of the screen
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mMenuDisplayAdapter = new MenuDisplayAdapter(this, null);
        mRecyclerView.setAdapter(mMenuDisplayAdapter);
        // Influenced from https://goo.gl/B9CsiA and https://goo.gl/fQbj5E
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Button to choose a new mensa
        mExtendedFab = (Button) findViewById(R.id.fab);
        mExtendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadNewActivity(MensaPickerActivity.class);
            }
        });
    }

    // Adapted from CampusInfo app to handle syncing the menu data
    @Override
    public void onPause() {
        super.onPause();

        getContentResolver().unregisterContentObserver(mMenuContentObserver);

        if (mLoadMenuTask != null && !mLoadMenuTask.isCancelled()) {
            mLoadMenuTask.cancel(true);
            mLoadMenuTask = null;
        }
        if (mMenuDisplayAdapter != null) {
            mMenuDisplayAdapter.changeCursor(null);
        }
    }

    // Adapted from CampusInfo app to handle syncing the menu data
    @Override
    public void onResume() {
        super.onResume();
        mMenuContentObserver = new MenuContentObserver(new Handler(Looper.getMainLooper()));
        getContentResolver().registerContentObserver(CateringContentProvider.CONTENT_URI,
                true, mMenuContentObserver);
        // Restart the task
        mLoadMenuTask = new LoadMenuTask();
        mLoadMenuTask.execute();
    }

    // I use a custom toolbar to display the calendar icon. I got the
    // idea to use this override here:
    // https://medium.com/@101/android-toolbar-for-appcompatactivity-671b1d10f354
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_display_toolbar, menu);
        ActionBar toolbar = getSupportActionBar();
        if (mMensa == null) {
            Log.d("MenuDisplay", "The mensa is null");
            toolbar.setTitle("NULL");
//            toolbar.setSubtitle("null");
        } else {
            toolbar.setTitle(mMensa.getName() + " (" + mMensa.getDayOfWeek() + ")");
//            toolbar.setSubtitle(mMensa.getDayOfWeek());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // There's only one icon accessible from this view: the calendar day picker icon
        LoadNewActivity(DayPickerActivity.class);
        return true;
    }

    private void LoadNewActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        // always pass the mensa so you can resume the app state if the user hits back
        intent.putExtra(Constants.MENU_SELECTOR, mMensa);
        startActivity(intent);
    }

    // Taken from CampusInfo app and adapted to suit current use case.
    private class LoadMenuTask extends AsyncTask<Void, Void, Optional<Cursor>> {

        @Override
        protected Optional<Cursor> doInBackground(Void... params) {
            return Optional.fromNullable(mCateringController.getMenu(mMensa.getFacilityId(), mMensa.getDay()));
        }

        @Override
        protected void onPostExecute(Optional<Cursor> optionalCursor) {
            if (this.isCancelled() || mMenuDisplayAdapter == null) {
                return;
            }
            Log.d("MenuDisplay", DatabaseUtils.dumpCursorToString(optionalCursor.get()));
            mMenuDisplayAdapter.changeCursor(optionalCursor.orNull());
        }
    }

    // Taken from CampusInfo app and adapted to suit current use case.
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
            // Redo task
            mLoadMenuTask = new LoadMenuTask();
            mLoadMenuTask.execute();
        }
    }
}


