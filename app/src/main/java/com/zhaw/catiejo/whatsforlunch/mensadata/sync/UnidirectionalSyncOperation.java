package com.zhaw.catiejo.whatsforlunch.mensadata.sync;

import android.content.ContentProviderOperation;
import android.content.Context;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Template for a unidirectional sync operation. It prepares all the {@link ContentProviderOperation}s necessary to
 * update the local database so that it mirrors the state of the remote database after applying all
 * {@link ContentProviderOperation}s successfully.
 *
 * @param <RemoteType> Type of the remote objects that are "the source of truth" and usually received from the remote
 *                     service.
 * @param <LocalType>  Type of local objects that are stored in the database.
 */
abstract class UnidirectionalSyncOperation<RemoteType, LocalType> {

    private final Context context;

    /**
     * Creates a new instance.
     *
     * @param context Android context.
     */
    UnidirectionalSyncOperation(Context context) {
        this.context = context;
    }

    /**
     * Prepares all {@link ContentProviderOperation}s that are required to bring the local database up to date.
     *
     * @param remoteObjects Remote objects to convert.
     * @return List of operations (or an empty list if there are none).
     */
    Collection<? extends ContentProviderOperation> prepareOperations(ImmutableCollection<RemoteType> remoteObjects) {
        ImmutableCollection<LocalType> convertedRemoteObjects = convertToLocalType(remoteObjects);

        ArrayList<ContentProviderOperation> operations = Lists.newArrayList();
        operations.addAll(removeVanished(convertedRemoteObjects));
        operations.addAll(updateOutdated(convertedRemoteObjects));
        operations.addAll(addNew(convertedRemoteObjects));
        return operations;
    }

    /**
     * @return The Android context.
     */
    Context getContext() {
        return context;
    }

    /**
     * Converts the collection of remote objects to "local" objects that can be compared with the objects stored in the
     * database.
     *
     * @param remoteObjects Objects to convert.
     * @return Converted objects.
     */
    protected abstract ImmutableCollection<LocalType> convertToLocalType(ImmutableCollection<RemoteType> remoteObjects);

    /**
     * Prepares all operations necessary to remove all objects from the local database that are present in the local
     * database but not in the list of remote objects.
     *
     * @param convertedRemoteObjects List of remote objects.
     * @return List of delete operations.
     */
    protected abstract Collection<? extends ContentProviderOperation> removeVanished(ImmutableCollection<LocalType> convertedRemoteObjects);

    /**
     * Prepares all operations necessary to update all objects in the local database that have changed on the remote
     * server since the last sync.
     *
     * @param convertedRemoteObjects List of remote objects.
     * @return List of update operations.
     */
    protected abstract Collection<? extends ContentProviderOperation> updateOutdated(ImmutableCollection<LocalType> convertedRemoteObjects);

    /**
     * Prepares all operations necessary to insert all objects to the local database that were added on the remote
     * server since the last sync.
     *
     * @param convertedRemoteObjects List of remote objects.
     * @return List of insert operations.
     */
    protected abstract Collection<? extends ContentProviderOperation> addNew(ImmutableCollection<LocalType> convertedRemoteObjects);
}
