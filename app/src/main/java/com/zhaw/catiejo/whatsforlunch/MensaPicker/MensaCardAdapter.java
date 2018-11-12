package com.zhaw.catiejo.whatsforlunch.MensaPicker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.zhaw.catiejo.whatsforlunch.MenuDisplay.MenuDisplayActivity;
import com.zhaw.catiejo.whatsforlunch.MensaContainer;
import com.zhaw.catiejo.whatsforlunch.R;
import com.zhaw.catiejo.whatsforlunch._campusinfo.helper.Constants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MensaCardAdapter extends CursorAdapter {
    public MensaCardAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.card_day, null);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final long facilityId = cursor.getLong(cursor.getColumnIndex("FacilityId"));
        final String name = cursor.getString(cursor.getColumnIndex("Name"));
        final LocalTime now = LocalTime.now(Constants.LocalTimeZone);

        TextView mensaName = (TextView) view.findViewById(R.id.dayOfWeek);
        mensaName.setText(name);
        TextView mensaDistance = (TextView) view.findViewById(R.id.dayOfYear);
        mensaDistance.setText("TODO");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIXME always resets to current day when you pick a mensa
                final MensaContainer selector = new MensaContainer(LocalDate.now(Constants.LocalTimeZone), facilityId, name);
                final Intent intent = new Intent(context, MenuDisplayActivity.class);
                intent.putExtra(Constants.MENU_SELECTOR, selector);
                context.startActivity(intent);
            }
        });
    }
}
