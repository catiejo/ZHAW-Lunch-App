package com.zhaw.catiejo.whatsforlunch.mensadata.dao;

import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.IDaoFactory;

/**
 * Creates a new {@link GastronomicFacilityDao} from the given cursor row.
 */
public class GastronomicFacilityDaoFactory implements IDaoFactory<GastronomicFacilityDao> {
    @Override
    public GastronomicFacilityDao constructFromCursor(Cursor cursor) {
        return GastronomicFacilityDao.fromCursor(cursor);
    }
}
