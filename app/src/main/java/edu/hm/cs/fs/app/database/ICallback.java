package edu.hm.cs.fs.app.database;

import edu.hm.cs.fs.app.database.error.IError;

/**
 * The communication interface between the presenter and database layer.
 *
 * @author Fabio
 */
public interface ICallback<T> {
    /**
     * Is called from the model if a result is available.
     *
     * @param data from the model.
     */
    void onSuccess(T data);

    /**
     * Is called from the model if an error ocurred.
     *
     * @param error with the message.
     */
    void onError(IError error);
}
