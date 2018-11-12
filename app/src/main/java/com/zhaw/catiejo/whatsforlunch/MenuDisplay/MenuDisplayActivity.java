package com.zhaw.catiejo.whatsforlunch.MenuDisplay;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.squareup.otto.Bus;
import com.zhaw.catiejo.whatsforlunch.CursorRecyclerAdapter.CursorRecyclerViewAdapter;
import com.zhaw.catiejo.whatsforlunch.DayPicker.DayPickerActivity;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.MensaPicker.MensaPickerActivity;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch.WhatsForLunchApplication;
import com.zhaw.catiejo.whatsforlunch._campusinfo.CateringContentProvider;
import com.zhaw.catiejo.whatsforlunch._campusinfo.ICateringController;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.DishDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;

import java.text.NumberFormat;

import javax.inject.Inject;

public class MenuDisplayActivity extends AppCompatActivity {
    @Inject
    ICateringController mCateringController;
    @Inject
    Bus bus;

//    private MenuAdapter mMenuAdapter;
    private MenuCardAdapter mMenuCardAdapter;
    private MenuContentObserver mMenuContentObserver;
    private LoadMenuTask mLoadMenuTask;
    private MensaContainer mMensa; //The mensa we're currently viewing
    private FloatingActionButton mFab;
    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        ((WhatsForLunchApplication) getApplication()).inject(this);


//        this.setContentView(R.layout.activity_menu_display);
//        mMenuAdapter = new MenuAdapter(this, null, 0);
//        final ListView list = (ListView) findViewById(R.id.menuList);
//        list.setAdapter(mMenuAdapter);

        this.setContentView(R.layout.activity_main);
        mMenuCardAdapter = new MenuCardAdapter(this, null);
        mRecyclerView = (RecyclerView) findViewById(R.id.menuRecycler);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mMenuCardAdapter);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMensa = (MensaContainer) getIntent().getSerializableExtra(Constants.MENU_SELECTOR);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadNewActivity(MensaPickerActivity.class);
            }
        });

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
        if (mMenuCardAdapter != null) {
            mMenuCardAdapter.changeCursor(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMenuContentObserver = new MenuContentObserver(new Handler(Looper.getMainLooper()));
        getContentResolver().registerContentObserver(CateringContentProvider.CONTENT_URI,
                true, mMenuContentObserver);
        mLoadMenuTask = new LoadMenuTask();
        mLoadMenuTask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        setUpToolbar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        LoadNewActivity(DayPickerActivity.class);
        return true;
    }

    private void LoadNewActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        intent.putExtra(Constants.MENU_SELECTOR, mMensa);
        startActivity(intent);
    }

    private void setUpToolbar() {
        ActionBar toolbar = getSupportActionBar();
//        toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setDisplayHomeAsUpEnabled(true);
//        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorWhite)));
        if (mMensa == null) {
            toolbar.setTitle("NULL");
            toolbar.setSubtitle("null");
        } else {
            toolbar.setTitle(mMensa.getName());
            toolbar.setSubtitle(mMensa.getDayOfWeek());
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
            if (this.isCancelled() || mMenuCardAdapter == null) {
                return;
            }
            if (optionalCursor.get().moveToFirst()) {
                DishDao dish = DishDao.fromCursor(optionalCursor.get());
                Log.e("CJ", dish.toString());
            }
            Log.e("CJ", DatabaseUtils.dumpCursorToString(optionalCursor.get()));
            mMenuCardAdapter.changeCursor(optionalCursor.orNull());
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


