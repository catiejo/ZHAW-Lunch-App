package com.zhaw.catiejo.whatsforlunch._campusinfo.dao;

import android.database.Cursor;

/**
 * Creates a new {@link HolidayDao} from the given cursor row.
 */
public class HolidayDaoFactory implements IDaoFactory<HolidayDao> {
    @Override
    public HolidayDao constructFromCursor(Cursor cursor) {
        return HolidayDao.fromCursor(cursor);
    }
}
