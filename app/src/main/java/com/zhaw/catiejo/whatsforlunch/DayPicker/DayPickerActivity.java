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
import java.util.ArrayList;
import java.util.List;


public class DayPickerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DayPickerAdapter mDayPickerAdapter;
    // The mensa from the previous task. Needed in case user hits cancel button in toolbar
    private MensaContainer mMensa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_day_picker);
        mMensa = (MensaContainer) getIntent().getSerializableExtra(Constants.MENU_SELECTOR); // can be null, but shouldn't be

        // Set up RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.dayRecycler);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Click listener creates a new mensa object with the selected date, then goes back to the menu display
        mDayPickerAdapter = new DayPickerAdapter(getDayCardList(mMensa.getDay()), new DayPickerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DayPickerCard card) {
                if (card.getIsInPast()) {
                    // don't allow viewing menus that are already past
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), MenuDisplayActivity.class);
                intent.putExtra(Constants.MENU_SELECTOR, new MensaContainer(card.getDate(), mMensa.getFacilityId(), mMensa.getName()));
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mDayPickerAdapter);

        setUpToolbar();
    }

    private List<DayPickerCard> getDayCardList(LocalDate currentDate) {
        int weekdayAsInt = currentDate.getDayOfWeek(); // Monday = 1, Sunday = 7
        List<DayPickerCard> week = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            LocalDate weekday = getXDaysFrom(currentDate, i - weekdayAsInt);
            boolean isPast = i < LocalDate.now(Constants.LocalTimeZone).getDayOfWeek();
            boolean isToday = i == weekdayAsInt;
            week.add(new DayPickerCard(weekday, isPast, isToday));
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
        toolbar.setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
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
