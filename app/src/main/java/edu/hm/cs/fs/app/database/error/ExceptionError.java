package edu.hm.cs.fs.app.database.error;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by FHellman on 14.08.2015.
 */
public class ExceptionError implements IError {
    private Exception mException;

    public ExceptionError(Exception e) {
        mException = e;
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
