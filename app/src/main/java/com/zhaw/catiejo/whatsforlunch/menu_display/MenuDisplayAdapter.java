package com.zhaw.catiejo.whatsforlunch.menu_display;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.zhaw.catiejo.whatsforlunch.cursorrecycleradapter.CursorRecyclerViewAdapter;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.DishDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import java.text.NumberFormat;

public class MenuDisplayAdapter extends CursorRecyclerViewAdapter<MenuDisplayAdapter.MenuDisplayViewHolder> {

    public MenuDisplayAdapter(Context context, Cursor cursor){
        super(context,cursor);
    }

    public static class MenuDisplayViewHolder extends RecyclerView.ViewHolder {
        protected TextView foodCounter;
        protected TextView menuItem;
        protected TextView menuDescription;
        protected TextView studentPrice;
        protected TextView employeePrice;
        protected TextView externalPrice;

        public MenuDisplayViewHolder(CardView cv) {
            super(cv);
            foodCounter = cv.findViewById(R.id.foodCounter);
            menuItem = cv.findViewById(R.id.menuItem);
            menuDescription = cv.findViewById(R.id.menuDescription);
            studentPrice = cv.findViewById(R.id.studentPrice);
            employeePrice = cv.findViewById(R.id.employeePrice);
            externalPrice = cv.findViewById(R.id.externalPrice);
        }
    }

    @Override
    public MenuDisplayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_menu, parent, false);
        MenuDisplayViewHolder vh = new MenuDisplayViewHolder(cv);
        return vh;

    }

    @Override
    public void onBindViewHolder(MenuDisplayAdapter.MenuDisplayViewHolder holder, Cursor cursor) {
        DishDao dish = DishDao.fromCursor(cursor); //Class from _campusinfo library
        holder.foodCounter.setText(dish.getLabel());
        holder.menuItem.setText(dish.getName());
        Iterable<String> sideDishes = Optional.presentInstances(Lists.newArrayList(dish.getFirstSideDish(),
                dish.getSecondSideDish(), dish.getThirdSideDish()));
        holder.menuDescription.setText(Joiner.on(" ").join(sideDishes));
        holder.studentPrice.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getInternalPrice()));
        holder.employeePrice.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getPriceForPartners()));
        holder.externalPrice.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getExternalPrice()));
    }
}

