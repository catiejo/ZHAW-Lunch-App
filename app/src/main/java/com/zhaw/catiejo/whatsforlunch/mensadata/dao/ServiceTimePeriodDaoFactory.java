package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.IDaoFactory;

/**
 * Creates a new {@link ServiceTimePeriodDao} from the given cursor row.
 */
public class ServiceTimePeriodDaoFactory implements IDaoFactory<ServiceTimePeriodDao> {
    @Override
    public ServiceTimePeriodDao constructFromCursor(Cursor cursor) {
        return ServiceTimePeriodDao.fromCursor(cursor);
    }
}
