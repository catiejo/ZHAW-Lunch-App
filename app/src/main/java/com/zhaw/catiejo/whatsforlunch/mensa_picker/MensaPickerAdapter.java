package com.zhaw.catiejo.whatsforlunch.mensa_picker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhaw.catiejo.whatsforlunch.cursorrecycleradapter.CursorRecyclerViewAdapter;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.menu_display.MenuDisplayActivity;
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
            checkMark = cv.findViewById(R.id.checkMark);
            mainText = cv.findViewById(R.id.mainText);
            supplementaryText = cv.findViewById(R.id.supplementaryText);
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
        if (MensaPickerActivity.mMensa != null && MensaPickerActivity.mMensa.getFacilityId() == currentMensa.getFacilityId()) {
            holder.checkMark.setImageResource(R.drawable.ic_check_blue_24dp);
            holder.mainText.setTextColor(Color.parseColor("#4A90E2"));
        }
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
