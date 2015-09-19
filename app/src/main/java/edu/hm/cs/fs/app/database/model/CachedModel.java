package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;

/**
 * Caches and updates the data.
 *
 * @param <T>
 *         of the data.
 * @author Fabio
 */
public abstract class CachedModel<T> implements IModel {

    /**
     * A list with the cached items.
     */
    private final List<T> mDataCache = new ArrayList<>();

    /**
     * Get the data from the cache - the cache handle the refresh itself.
     *
     * @param refresh
     *         if a refresh is forced.
     * @param callback
     *         to retrieve the results.
     */
    protected void getData(final boolean refresh, @NonNull final ICallback<List<T>> callback) {
        if (mDataCache.isEmpty() || refresh) {
            update(new ICallback<List<T>>() {
                @Override
                public void onSuccess(@NonNull List<T> data) {
                    mDataCache.clear();
                    mDataCache.addAll(data);
                    callback.onSuccess(mDataCache);
                }

                @Override
                public void onError(@NonNull IError error) {
                    callback.onError(error);
                }
            });
        } else {
            callback.onSuccess(mDataCache);
        }
    }

    protected void cleanUp() {
        mDataCache.clear();
    }

    /**
     * Updates the content with the web.
     *
     * @param callback
     *         to retrieve the results.
     */
    protected abstract void update(@NonNull final ICallback<List<T>> callback);
}
