package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.IDaoFactory;

/**
 * Creates a new {@link HolidayDao} from the given cursor row.
 */
public class HolidayDaoFactory implements IDaoFactory<HolidayDao> {
    @Override
    public HolidayDao constructFromCursor(Cursor cursor) {
        return HolidayDao.fromCursor(cursor);
    }
}
