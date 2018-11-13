package com.zhaw.catiejo.whatsforlunch.day_picker;

import com.zhaw.catiejo.whatsforlunch.MensaContainer;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DayPickerCard {
    private String mDayOfWeek;
    private String mDayOfYear;
    private boolean mIsInPast;
    private boolean mIsSelected;
    private LocalDate mDate;

    public String getDayOfWeek() { return mDayOfWeek; }
    public String getDayOfYear() { return mDayOfYear; }
    public boolean getIsInPast() { return mIsInPast; }
    public boolean getIsSelected() { return mIsSelected; }
    public LocalDate getDate() { return mDate; }

    public DayPickerCard(LocalDate date, boolean isInPast, boolean isSelected) {
        DateTimeFormatter pattern = DateTimeFormat.forPattern("MM/dd/yyyy");
        mDate = date;
        mDayOfWeek = MensaContainer.getWeekday(date);
        mDayOfYear = date.toString( pattern );
        mIsInPast = isInPast;
        mIsSelected = isSelected;
    }

    public void setSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }
}
