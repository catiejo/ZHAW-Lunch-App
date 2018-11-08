package com.zhaw.catiejo.whatsforlunch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
        mAdapter = new DayCardAdapter(getDayCardList());
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<DayCard> getDayCardList() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        Calendar date = Calendar.getInstance();
        String dayOfYear = dateFormat.format(date.getTime());
        int weekdayAsInt = date.get(Calendar.DAY_OF_WEEK); // Sunday = 1, Saturday = 7
        List<DayCard> week = new ArrayList<>();
        for (int i = 2; i < 7; i++) {
            String weekdayAsString = getWeekday(weekdayAsInt);
            if (weekdayAsInt == i) {
                week.add(new DayCard(weekdayAsString, dayOfYear, false, true));
            } else if (i < weekdayAsInt) {
                Calendar earlierDay = getXDaysFromNow(weekdayAsInt - i);
                week.add(new DayCard(weekdayAsString, dateFormat.format(earlierDay.getTime()), true, false));
            }
            else {
                Calendar laterDay = getXDaysFromNow(i - weekdayAsInt);
                week.add(new DayCard(weekdayAsString, dateFormat.format(laterDay.getTime()), false, false));
            }
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
}
