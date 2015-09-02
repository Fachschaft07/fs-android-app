package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;

/**
 * Caches and updates the data.
 *
 * @param <T> of the data.
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
     * @param refresh  if a refresh is forced.
     * @param callback to retrieve the results.
     */
    protected void getData(final boolean refresh, @NonNull final ICallback<List<T>> callback) {
        if (refresh || mDataCache.isEmpty()) {
            updateOffline(new ICallback<List<T>>() {
                @Override
                public void onSuccess(List<T> data) {
                    if (data.isEmpty()) {
                        updateOnline(new ICallback<List<T>>() {
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
                        callback.onSuccess(data);
                    }
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

    /**
     * Updates the content with the web.
     *
     * @param callback to retrieve the results.
     */
    protected abstract void updateOnline(@NonNull final ICallback<List<T>> callback);

    /**
     * Updates the content with the offline data.
     *
     * @param callback to retrieve the results.
     */
    protected void updateOffline(@NonNull final ICallback<List<T>> callback) {
        callback.onSuccess(new ArrayList<T>());
    }
}
