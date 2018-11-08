package com.zhaw.catiejo.whatsforlunch;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public class DayCardAdapter extends RecyclerView.Adapter<DayCardAdapter.DayCardViewHolder> {
    private List<DayCard> mData;
    public static class DayCardViewHolder extends RecyclerView.ViewHolder {

        public DayCardViewHolder(CardView mc) {
            super(mc);
        }
    }

    public DayCardAdapter(List<DayCard> cards) {
        mData = cards;
    }

    @Override
    public DayCardAdapter.DayCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_menu, parent, false);
        DayCardViewHolder vh = new DayCardViewHolder(c);
        return vh;
    }

    @Override
    public void onBindViewHolder(DayCardAdapter.DayCardViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}