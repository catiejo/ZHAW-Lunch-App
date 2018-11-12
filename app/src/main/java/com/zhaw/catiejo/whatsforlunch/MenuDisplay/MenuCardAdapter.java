package com.zhaw.catiejo.whatsforlunch.MenuDisplay;

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
import com.zhaw.catiejo.whatsforlunch.CursorRecyclerAdapter.CursorRecyclerViewAdapter;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.DishDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import java.text.NumberFormat;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class MenuCardAdapter extends CursorRecyclerViewAdapter<MenuCardAdapter.MenuCardViewHolder> {

    public MenuCardAdapter(Context context, Cursor cursor){
        super(context,cursor);
    }

    public static class MenuCardViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView foodCounter;
        protected TextView menuItem;
        protected TextView menuDescription;
        protected TextView studentPrice;
        protected TextView employeePrice;
        protected TextView externalPrice;
        public MenuCardViewHolder(CardView mc) {
            super(mc);
            foodCounter = (TextView) mc.findViewById(R.id.foodCounter);
            menuItem = (TextView) mc.findViewById(R.id.menuItem);
            menuDescription = (TextView) mc.findViewById(R.id.menuDescription);
            studentPrice = (TextView) mc.findViewById(R.id.studentPrice);
            employeePrice = (TextView) mc.findViewById(R.id.employeePrice);
            externalPrice = (TextView) mc.findViewById(R.id.externalPrice);
        }
    }

    @Override
    public MenuCardAdapter.MenuCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_menu, parent, false);
        MenuCardViewHolder vh = new MenuCardViewHolder(c);
        return vh;

    }

    @Override
    public void onBindViewHolder(MenuCardViewHolder holder, Cursor cursor) {
        DishDao dish = DishDao.fromCursor(cursor);
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

