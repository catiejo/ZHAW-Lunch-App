package com.zhaw.catiejo.whatsforlunch.day_picker;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zhaw.catiejo.whatsforlunch.R;
import java.util.List;

// Click listener code taken from: https://antonioleiva.com/recyclerview-listener/
public class DayPickerAdapter extends RecyclerView.Adapter<DayPickerAdapter.DayCardViewHolder> {
    private List<DayPickerCard> mData;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(DayPickerCard card);
    }

    public static class DayCardViewHolder extends RecyclerView.ViewHolder {
        protected TextView dayOfWeek;
        protected TextView dayOfYear;
        protected ImageView checkMark;

        public DayCardViewHolder(CardView dc) {
            super(dc);
            dayOfWeek = dc.findViewById(R.id.mainText);
            dayOfYear = dc.findViewById(R.id.supplementaryText);
            checkMark = dc.findViewById(R.id.checkMark);
        }

        public void bind(final DayPickerCard card, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(card);
                }
            });

        }
    }

    public DayPickerAdapter(List<DayPickerCard> cards, OnItemClickListener listener) {
        mData = cards;
        mListener = listener;
    }

    @Override
    public DayPickerAdapter.DayCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_picker, parent, false);
        return new DayCardViewHolder(c);
    }

    @Override
    public void onBindViewHolder(DayPickerAdapter.DayCardViewHolder holder, int position) {
        DayPickerCard dc = mData.get(position);
        holder.dayOfWeek.setText(dc.getDayOfWeek());
        holder.dayOfYear.setText(dc.getDayOfYear());
        holder.bind(dc, mListener); // bind the card to the listener
        if (dc.getIsInPast()) {
            holder.dayOfWeek.setPaintFlags(holder.dayOfWeek.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.dayOfWeek.setTextColor(Color.parseColor("#9B9B9B"));
        }
        if (dc.getIsSelected()) {
            holder.checkMark.setImageResource(R.drawable.ic_check_blue_24dp);
            holder.dayOfWeek.setTextColor(Color.parseColor("#1166AC"));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}