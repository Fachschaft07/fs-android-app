package edu.hm.cs.fs.app.database.error;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.common.constant.ErrorCode;

/**
 * An error which can be thrown by a model and will be put throw to the view to handle the message.
 *
 * @author Fabio
 */
public interface IError {

    /**
     * If the error comes from a not existing internet connection then this method will return
     * <code>true</code>.
     *
     * @return <code>true</code> if no internet connection is available.
     */
    boolean isConnected();

    /**
     * Get the error message.
     *
     * @param context to access {@link Context#getString(int)}.
     * @return the message.
     */
    String getMessage(@NonNull final Context context);

    /**
     * Get the error code from the rest api - if this is a http error.
     *
     * @return the error code or null.
     */
    ErrorCode getErrorCode();
}
