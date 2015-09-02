package edu.hm.cs.fs.app.database.error;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * An error which was generated out of an exception.
 *
 * @author Fabio
 */
public class ExceptionError implements IError {

    private Exception mException;

    /**
     * Creates an exception error.
     *
     * @param exception which was thrown.
     */
    public ExceptionError(@NonNull final Exception exception) {
        mException = exception;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public String getMessage(@NonNull Context context) {
        return mException.getLocalizedMessage();
    }
}
