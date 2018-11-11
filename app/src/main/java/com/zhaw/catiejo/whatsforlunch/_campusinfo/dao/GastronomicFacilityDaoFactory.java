package com.zhaw.catiejo.whatsforlunch._campusinfo.dao;

import android.database.Cursor;

/**
 * Creates a new {@link GastronomicFacilityDao} from the given cursor row.
 */
public class GastronomicFacilityDaoFactory implements IDaoFactory<GastronomicFacilityDao> {
    @Override
    public GastronomicFacilityDao constructFromCursor(Cursor cursor) {
        return GastronomicFacilityDao.fromCursor(cursor);
    }
}
