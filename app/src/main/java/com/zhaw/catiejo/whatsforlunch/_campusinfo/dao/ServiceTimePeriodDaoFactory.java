package com.zhaw.catiejo.whatsforlunch._campusinfo.dao;

import android.database.Cursor;

/**
 * Creates a new {@link ServiceTimePeriodDao} from the given cursor row.
 */
public class ServiceTimePeriodDaoFactory implements IDaoFactory<ServiceTimePeriodDao> {
    @Override
    public ServiceTimePeriodDao constructFromCursor(Cursor cursor) {
        return ServiceTimePeriodDao.fromCursor(cursor);
    }
}
