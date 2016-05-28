package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

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
                public void onSuccess(@Nullable List<T> data) {
                    mDataCache.clear();
                    if(data != null) {
                        mDataCache.addAll(data);
                    }
                    callback.onSuccess(mDataCache);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    callback.onError(e);
                }
            });
        } else {
            callback.onSuccess(mDataCache);
        }
    }

    /**
     * Updates the content with the web.
     *
     * @param callback
     *         to retrieve the results.
     */
    protected abstract void update(@NonNull final ICallback<List<T>> callback);
}
