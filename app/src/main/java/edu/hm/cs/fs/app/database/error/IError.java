package edu.hm.cs.fs.app.database.error;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by FHellman on 14.08.2015.
 */
public interface IError {
    boolean isConnected();

    String getMessage(@NonNull final Context context);
}
