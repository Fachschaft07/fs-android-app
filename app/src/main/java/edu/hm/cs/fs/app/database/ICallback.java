package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

/**
 * The communication interface between the presenter and database layer.
 *
 * @param <T> type of the data.
 * @author Fabio
 */
public interface ICallback<T> {

    /**
     * Is called from the model if a result is available.
     *
     * @param data from the model.
     */
    void onSuccess(final T data);

    /**
     * Is called from the model if an error occurred.
     *
     * @param e with the message.
     */
    void onError(@NonNull final Throwable e);
}
