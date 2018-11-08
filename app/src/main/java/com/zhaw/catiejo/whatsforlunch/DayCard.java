package com.zhaw.catiejo.whatsforlunch;

public class DayCard {
    private String mDayOfWeek;
    private String mDayOfYear;
    private boolean mIsInPast;
    private boolean mIsSelected;

    public String getDayOfWeek() { return mDayOfWeek; }
    public String getDayOfYear() { return mDayOfYear; }
    public boolean getIsInPast() { return mIsInPast; }
    public boolean getIsSelected() { return mIsSelected; }

    public DayCard(String dayOfWeek, String dayOfYear, boolean isInPast, boolean isSelected) {
        mDayOfWeek = dayOfWeek;
        mDayOfYear = dayOfYear;
        mIsInPast = isInPast;
        mIsSelected = isSelected;
    }

    public void setSelected(boolean isSelected) {
        mIsSelected = isSelected;
    }
}
