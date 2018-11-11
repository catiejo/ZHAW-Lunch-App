package com.zhaw.catiejo.whatsforlunch.DayPicker;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.zhaw.catiejo.whatsforlunch.MenuDisplay.MenuDisplayActivity;
import com.zhaw.catiejo.whatsforlunch.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DayPickerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                startActivity(intent);
            }
        };
        mAdapter = new DayCardAdapter(getDayCardList(), listener);
        mRecyclerView.setAdapter(mAdapter);

        setUpToolbar();
    }

    // TODO: always selects current day, not currently selected day
    private List<DayCard> getDayCardList() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        int weekdayAsInt = Calendar.getInstance().get(Calendar.DAY_OF_WEEK); // Sunday = 1, Saturday = 7
        List<DayCard> week = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            String weekdayAsString = getWeekday(i);
            Calendar weekday = getXDaysFromNow(i - weekdayAsInt);
            boolean isPast = i < weekdayAsInt;
            boolean isToday = i == weekdayAsInt;
            week.add(new DayCard(weekdayAsString, dateFormat.format(weekday.getTime()), isPast, isToday));
        }
        if (weekdayAsInt < 2 || weekdayAsInt > 6) {
            week.get(0).setSelected(true);
        }
        return week;
    }

    private Calendar getXDaysFromNow(int x) {
        // https://stackoverflow.com/questions/912762/get-previous-day
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DAY_OF_MONTH, x);
        return day;
    }

    private String getWeekday(int day) {
        String weekday = "";
        switch(day) {
            case 1:
                weekday = "Sunday";
                break;
            case 2:
                weekday = "Monday";
                break;
            case 3:
                weekday = "Tuesday";
                break;
            case 4:
                weekday = "Wednesday";
                break;
            case 5:
                weekday = "Thursday";
                break;
            case 6:
                weekday = "Friday";
                break;
            default:
                weekday = "Saturday";
                break;
        }
        return weekday;
    }

    private void setUpToolbar() {
        ActionBar toolbar = getSupportActionBar();
        toolbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.dayPicker);
    }


    // https://stackoverflow.com/questions/35810229/how-to-display-and-set-click-event-on-back-arrow-on-toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(this, MenuDisplayActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
