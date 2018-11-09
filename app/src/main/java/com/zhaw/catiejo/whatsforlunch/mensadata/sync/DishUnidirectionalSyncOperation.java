package com.zhaw.catiejo.whatsforlunch.mensadata.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.database.Cursor;
import com.zhaw.catiejo.whatsforlunch.mensadata.dao.DishDao;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.DishDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.MenuDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.dto.MenuPlanDto;
import com.zhaw.catiejo.whatsforlunch.mensadata.helper.Constants;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.squareup.phrase.Phrase;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collection;

import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.ActionUri;
import static com.zhaw.catiejo.whatsforlunch.mensadata.CateringContentProvider.DishInfo;

class DishUnidirectionalSyncOperation extends UnidirectionalSyncOperation<MenuPlanDto, DishDao> {

    /**
     * Creates a new instance.
     *
     * @param context Android context.
     */
    DishUnidirectionalSyncOperation(Context context) {
        super(context);
    }

    @Override
    protected ImmutableCollection<DishDao> convertToLocalType(ImmutableCollection<MenuPlanDto> remoteObjects) {
        ImmutableSet.Builder<DishDao> builder = ImmutableSet.builder();

        for (MenuPlanDto menuPlanDto : remoteObjects) {
            for (MenuDto menuDto : menuPlanDto.getMenus()) {
                for (DishDto dishDto : menuDto.getDishes()) {

                    Optional<String> firstSideDish = Optional.absent();
                    Optional<String> secondSideDish = Optional.absent();
                    Optional<String> thirdSideDish = Optional.absent();

                    for (int i = 0; i < Math.min(dishDto.getSideDishes().size(), 3); i++) {
                        String content = dishDto.getSideDishes().get(i).getName();

                        if (Strings.isNullOrEmpty(content)) {
                            continue;
                        }

                        switch (i) {
                            case 0:
                                firstSideDish = Optional.of(content);
                                break;
                            case 1:
                                secondSideDish = Optional.of(content);
                                break;
                            case 2:
                                thirdSideDish = Optional.of(content);
                                break;
                        }
                    }

                    for (long facilityId : menuPlanDto.getGastronomicFacilityIds()) {
                        builder.add(new DishDao(null, dishDto.getId(), facilityId, dishDto.getLabel(), dishDto.getName(),
                                firstSideDish, secondSideDish, thirdSideDish, menuDto.getOfferedOn(),
                                dishDto.getInternalPrice(), dishDto.getPriceForPartners(), dishDto.getExternalPrice(),
                                DateTime.now(Constants.LocalTimeZone), dishDto.getVersion()));
                    }
                }
            }
        }
        return builder.build();
    }

    @Override
    protected Collection<? extends ContentProviderOperation> removeVanished(ImmutableCollection<DishDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forDishes(), null, null, null, null);

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                DishDao localDishDao = DishDao.fromCursor(cursor);

                Optional<DishDao> remoteDishDao = this.findDishDao(convertedRemoteObjects, localDishDao.getDishId(),
                        localDishDao.getFacilityId());

                if (remoteDishDao.isPresent()) {
                    continue;
                }

                operations.add(ContentProviderOperation.newDelete(ActionUri.forDish(localDishDao.getId()))
                        .build());
            }

            return operations;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected Collection<? extends ContentProviderOperation> updateOutdated(ImmutableCollection<DishDao> convertedRemoteObjects) {
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forDishes(), null, null, null, null);

            String selection = Phrase.from("{dish_id} = ? AND {facility_id} = ? AND {version} = ?")
                    .put("dish_id", DishInfo.DishId)
                    .put("facility_id", DishInfo.FacilityId)
                    .put("version", DishInfo.Version)
                    .format()
                    .toString();

            ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
            while (cursor != null && cursor.moveToNext()) {
                DishDao localDishDao = DishDao.fromCursor(cursor);

                Optional<DishDao> remoteDishDao = findDishDao(convertedRemoteObjects, localDishDao.getDishId(),
                        localDishDao.getFacilityId());

                if (!remoteDishDao.isPresent()) {
                    continue;
                }
                /**
                 * The side dishes are separately versioned entities. Therefore, in a distant future it might happen
                 * that they change but the dish does not so the updates would get lost when only comparing the versions
                 * of the dishes and not the contents of the side dishes too.
                 */
                if (localDishDao.getVersion().equals(remoteDishDao.get().getVersion()) &&
                        sideDishesAreUnchanged(localDishDao, remoteDishDao)) {

                    String[] selectionArgs = new String[]{
                            localDishDao.getDishId().toString(),
                            localDishDao.getFacilityId().toString(),
                            localDishDao.getVersion()
                    };

                    operations.add(ContentProviderOperation.newUpdate(ActionUri.forDish(localDishDao.getId()))
                            .withValue(DishInfo.LastUpdate, DateTime.now(Constants.LocalTimeZone).toString())
                            .withSelection(selection, selectionArgs)
                            .build());

                    continue;
                }

                String[] selectionArgs = new String[]{
                        localDishDao.getDishId().toString(),
                        localDishDao.getFacilityId().toString(),
                        localDishDao.getVersion()
                };

                DishDao updateDao = new DishDao(localDishDao.getId(), remoteDishDao.get().getDishId(),
                        remoteDishDao.get().getFacilityId(), remoteDishDao.get().getLabel(),
                        remoteDishDao.get().getName(), remoteDishDao.get().getFirstSideDish(),
                        remoteDishDao.get().getSecondSideDish(), remoteDishDao.get().getThirdSideDish(),
                        remoteDishDao.get().getOfferedOn(), remoteDishDao.get().getInternalPrice(),
                        remoteDishDao.get().getPriceForPartners(), remoteDishDao.get().getExternalPrice(),
                        DateTime.now(Constants.LocalTimeZone), remoteDishDao.get().getVersion());

                operations.add(ContentProviderOperation.newUpdate(ActionUri.forDish(localDishDao.getId()))
                        .withValues(updateDao.toContentValues())
                        .withSelection(selection, selectionArgs)
                        .build());
            }

            return operations;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected Collection<? extends ContentProviderOperation> addNew(ImmutableCollection<DishDao> convertedRemoteObjects) {
        ImmutableSet.Builder<DishDao> builder = ImmutableSet.builder();
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(ActionUri.forDishes(), null, null, null, null);
            while (cursor != null && cursor.moveToNext()) {
                builder.add(DishDao.fromCursor(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        ImmutableSet<DishDao> localDishDaos = builder.build();
        ArrayList<ContentProviderOperation> operations = Lists.newArrayList();

        for (DishDao remoteDishDao : convertedRemoteObjects) {
            Optional<DishDao> localDishDao = findDishDao(localDishDaos, remoteDishDao.getDishId(),
                    remoteDishDao.getFacilityId());

            if (localDishDao.isPresent()) {
                continue;
            }

            operations.add(ContentProviderOperation.newInsert(ActionUri.forDishes())
                    .withValues(remoteDishDao.toContentValues())
                    .build());
        }

        return operations;
    }

    private Optional<DishDao> findDishDao(ImmutableCollection<DishDao> listToSearch, long dishIdWanted,
                                          long facilityIdWanted) {
        for (DishDao dishDao : listToSearch) {
            if (dishDao.getDishId().equals(dishIdWanted) && dishDao.getFacilityId().equals(facilityIdWanted)) {
                return Optional.of(dishDao);
            }
        }
        return Optional.absent();
    }

    private boolean sideDishesAreUnchanged(DishDao localDishDao, Optional<DishDao> remoteDishDao) {
        return localDishDao.getFirstSideDish().equals(remoteDishDao.get().getFirstSideDish()) &&
                localDishDao.getSecondSideDish().equals(remoteDishDao.get().getSecondSideDish()) &&
                localDishDao.getThirdSideDish().equals(remoteDishDao.get().getThirdSideDish());
    }
}
