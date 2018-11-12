package com.zhaw.catiejo.whatsforlunch.MenuDisplay;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.DishDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.text.NumberFormat;

public class MenuAdapter extends CursorAdapter {

    public MenuAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.e("cj", "I like cheese");
        return LayoutInflater.from(context).inflate(R.layout.card_menu, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.e("cj", "BANANANANANAANANA");

        DishDao dish = DishDao.fromCursor(cursor);

        TextView menuLabelView = (TextView)view.findViewById(R.id.foodCounter);
        menuLabelView.setText(dish.getLabel());
        Log.e("cj", dish.getLabel());

        TextView menuNameView = (TextView)view.findViewById(R.id.menuItem);
        menuNameView.setText(dish.getName());
        Log.e("cj", dish.getName());

        Iterable<String> sideDishes = Optional.presentInstances(Lists.newArrayList(dish.getFirstSideDish(),
                dish.getSecondSideDish(), dish.getThirdSideDish()));
        TextView sideDishesView = (TextView)view.findViewById(R.id.menuDescription);
        sideDishesView.setText(Joiner.on(" ").join(sideDishes));

        TextView internalPriceView = (TextView)view.findViewById(R.id.studentPrice);
        internalPriceView.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getInternalPrice()));

        TextView partnerPriceView = (TextView)view.findViewById(R.id.employeePrice);
        partnerPriceView.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getPriceForPartners()));

        TextView externalPriceView = (TextView)view.findViewById(R.id.externalPrice);
        externalPriceView.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getExternalPrice()));
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
