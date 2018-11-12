package com.zhaw.catiejo.whatsforlunch.MensaPicker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaw.catiejo.whatsforlunch.CursorRecyclerAdapter.CursorRecyclerViewAdapter;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.MenuDisplay.MenuDisplayActivity;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;

import org.joda.time.LocalDate;

public class MensaPickerAdapter extends CursorRecyclerViewAdapter<MensaPickerAdapter.MensaPickerViewHolder> {
    public MensaPickerAdapter(Context context, Cursor cursor){
        super(context,cursor);
    }

    public static class MensaPickerViewHolder extends RecyclerView.ViewHolder {
        protected ImageView checkMark;
        protected TextView mainText;
        protected TextView supplementaryText;

        public MensaPickerViewHolder(CardView cv) {
            super(cv);
            checkMark = (ImageView) cv.findViewById(R.id.checkMark);
            mainText = (TextView) cv.findViewById(R.id.mainText);
            supplementaryText = (TextView) cv.findViewById(R.id.supplementaryText);
        }
    }

    @Override
    public MensaPickerAdapter.MensaPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_picker, parent, false);
        MensaPickerViewHolder vh = new MensaPickerViewHolder(cv);
        return vh;

    }

    @Override
    public void onBindViewHolder(final MensaPickerViewHolder holder, Cursor cursor) {
        final MensaContainer currentMensa = new MensaContainer(LocalDate.now(Constants.LocalTimeZone),
                cursor.getLong(cursor.getColumnIndex("FacilityId")),
                cursor.getString(cursor.getColumnIndex("Name")));
        holder.mainText.setText(currentMensa.getName());
        holder.supplementaryText.setText(""); //TODO: distance when I get location services working
        // credit: https://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIXME always resets to current day when you pick a mensa
                Intent intent = new Intent(v.getContext(), MenuDisplayActivity.class);
                intent.putExtra(Constants.MENU_SELECTOR, currentMensa);
                v.getContext().startActivity(intent);
            }
        });

    }

}
