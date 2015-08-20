package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.error.IError;

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
    void onSuccess(@NonNull final T data);

    /**
     * Is called from the model if an error occurred.
     *
     * @param error with the message.
     */
    void onError(@NonNull final IError error);
}
