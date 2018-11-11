package com.zhaw.catiejo.whatsforlunch.MenuDisplay;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.dao.DishDao;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;

import java.text.NumberFormat;
import java.util.List;

public class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.MenuCardViewHolder> {
    private List<MenuCard> mData;
    private Cursor mCursor;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
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

    // Constructor
    public MenuCardAdapter(List<MenuCard> cards, Cursor cursor) {
        mCursor = cursor;
        mData = cards;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MenuCardAdapter.MenuCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_menu, parent, false);
        MenuCardViewHolder vh = new MenuCardViewHolder(c);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MenuCardViewHolder holder, int position) {
        //help provided here: https://www.binpress.com/android-recyclerview-cardview-guide/
        MenuCard mc = mData.get(position);
        holder.foodCounter.setText(mc.getFoodCounter());
        holder.menuItem.setText(mc.getMenuItem());
        holder.menuDescription.setText(mc.getMenuDescription());
        holder.studentPrice.setText(mc.getStudentPrice());
        holder.employeePrice.setText(mc.getEmployeePrice());
        holder.externalPrice.setText(mc.getExternalPrice());

//        DishDao dish = DishDao.fromCursor(mCursor);
//
//        holder.foodCounter.setText(dish.getLabel());
//        holder.menuItem.setText(dish.getName());
//        Iterable<String> sideDishes = Optional.presentInstances(Lists.newArrayList(dish.getFirstSideDish(),
//                dish.getSecondSideDish(), dish.getThirdSideDish()));
//        holder.menuDescription.setText(Joiner.on(" ").join(sideDishes));
//        holder.studentPrice.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getInternalPrice()));
//        holder.employeePrice.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getPriceForPartners()));
//        holder.externalPrice.setText(NumberFormat.getCurrencyInstance(Constants.LOCAL_LOCALE).format(dish.getExternalPrice()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addMenuCard(MenuCard mc) {
        mData.add(mc);
    }

}
