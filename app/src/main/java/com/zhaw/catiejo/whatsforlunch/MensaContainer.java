package com.zhaw.catiejo.whatsforlunch;
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
        String weekday = "";
        switch(day) {
            case 1:
                weekday = "Monday";
                break;
            case 2:
                weekday = "Tuesday";
                break;
            case 3:
                weekday = "Wednesday";
                break;
            case 4:
                weekday = "Thursday";
                break;
            case 5:
                weekday = "Friday";
                break;
            case 6:
                weekday = "Saturday";
                break;
            default:
                weekday = "Sunday";
                break;
        }
        return weekday;
    }
}
