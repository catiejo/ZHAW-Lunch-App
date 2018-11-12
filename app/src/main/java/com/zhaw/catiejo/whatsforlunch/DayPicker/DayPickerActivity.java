package com.zhaw.catiejo.whatsforlunch.DayPicker;

import android.content.Intent;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.MenuDisplay.MenuDisplayActivity;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;

import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class DayPickerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MensaContainer mMensa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMensa = (MensaContainer) getIntent().getSerializableExtra(Constants.MENU_SELECTOR);

        setContentView(R.layout.activity_day_picker);


        // In activity_main.xml, there's a Recycler View with the id recyclerView.
        // This gets a reference to that.
        mRecyclerView = (RecyclerView) findViewById(R.id.dayRecycler);

        // Horizontally scrolling layout manager
        // Influenced from https://goo.gl/B9CsiA and https://goo.gl/fQbj5E
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Set up the adapter...this connects the views (layouts?) to the data (my custom class, MenuCard)
        DayCardAdapter.OnItemClickListener listener = new DayCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DayCard card) {
                if (card.getIsInPast()) {
                    // don't allow viewing menus that are already past
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), MenuDisplayActivity.class);
                intent.putExtra(Constants.MENU_SELECTOR, new MensaContainer(card.getDate(), mMensa.getFacilityId(), mMensa.getName()));
                startActivity(intent);
            }
        };
        mAdapter = new DayCardAdapter(getDayCardList(mMensa.getDay()), listener);
        mRecyclerView.setAdapter(mAdapter);

        setUpToolbar();
    }

    private List<DayCard> getDayCardList(LocalDate currentDate) {
        int weekdayAsInt = currentDate.getDayOfWeek(); // Monday = 1, Sunday = 7
        List<DayCard> week = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            LocalDate weekday = getXDaysFrom(currentDate, i - weekdayAsInt);
            boolean isPast = i < LocalDate.now(Constants.LocalTimeZone).getDayOfWeek();
            boolean isToday = i == weekdayAsInt;
            week.add(new DayCard(weekday, isPast, isToday));
        }
        if (weekdayAsInt < 2 || weekdayAsInt > 6) {
            week.get(0).setSelected(true);
        }
        return week;
    }

    private LocalDate getXDaysFrom(LocalDate date, int x) {
        if (x < 0) {
            return date.minusDays(Math.abs(x));
        } else if (x > 0) {
            return date.plusDays(x);
        } else {
            return date;
        }
    }

    private void setUpToolbar() {
        ActionBar toolbar = getSupportActionBar();
        toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.dayPicker);
    }


    // https://stackoverflow.com/questions/35810229/how-to-display-and-set-click-event-on-back-arrow-on-toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MenuDisplayActivity.class);
                intent.putExtra(Constants.MENU_SELECTOR, mMensa);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
