package com.zhaw.catiejo.whatsforlunch;

import android.util.Log;
import org.joda.time.LocalDate;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

public class MensaContainer implements Serializable {
    private final LocalDate day;
    private final long facilityId;
    private final String name;
    private final String dayOfWeek;

    public MensaContainer(final LocalDate day, final long facilityId, final String name) {
        this.day = checkNotNull(day);
        this.facilityId = checkNotNull(facilityId);
        this.name = checkNotNull(name);
        this.dayOfWeek = getWeekday(day);
    }

    public String getName() {
        return name;
    }
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public long getFacilityId() { return facilityId; }
    public LocalDate getDay() { return day; }

    public static String getWeekday(LocalDate date) {
        int day = date.getDayOfWeek(); // Monday = 1, Sunday = 7
        String[] daysOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int i = 0; i < daysOfTheWeek.length; i++) {
            if (day == i + 1) {
                return daysOfTheWeek[i];
            }
        }
        Log.e("MensaContainer", "Day of the week was not found!");
        return "Weekend";
    }
}
