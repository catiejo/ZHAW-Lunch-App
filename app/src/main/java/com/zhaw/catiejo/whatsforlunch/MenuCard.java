package com.zhaw.catiejo.whatsforlunch;

import android.widget.CursorAdapter;

public class MenuCard {
    private int mFoodCounter;
    private int mMenuItem;
    private int mMenuDescription;
    private int mStudentPrice;
    private int mEmployeePrice;
    private int mExternalPrice;

    public int getFoodCounter() { return mFoodCounter; }
    public int getMenuItem() { return mMenuItem; }
    public int getMenuDescription() { return mMenuDescription; }
    public int getStudentPrice() { return mStudentPrice; }
    public int getEmployeePrice() { return mEmployeePrice; }
    public int getExternalPrice() { return mExternalPrice; }

    public MenuCard(int mensaName, int menuItem, int menuDescription, int studentPrice, int employeePrice, int externalPrice) {
        mFoodCounter = mensaName;
        mMenuItem = menuItem;
        mMenuDescription = menuDescription;
        mStudentPrice = studentPrice;
        mEmployeePrice = employeePrice;
        mExternalPrice = externalPrice;
    }

}
