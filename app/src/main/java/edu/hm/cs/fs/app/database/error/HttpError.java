package edu.hm.cs.fs.app.database.error;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fk07.R;

import retrofit.RetrofitError;

/**
 * An error which was generated out of a http error.
 *
 * @author Fabio
 */
public class HttpError implements IError {

    private RetrofitError mRetrofitError;

    /**
     * Creates a http error.
     *
     * @param error which was received.
     */
    protected HttpError(@NonNull final RetrofitError error) {
        mRetrofitError = error;
    }

    public boolean isConnected() {
        return mRetrofitError.getKind() != RetrofitError.Kind.NETWORK;
    }

    @Override
    public String getMessage(@NonNull Context context) {
        final String message;
        if (mRetrofitError.getKind() == RetrofitError.Kind.NETWORK) {
            message = context.getString(R.string.network_error);
        } else {
            message = mRetrofitError.getLocalizedMessage();
        }
        return message;
    }
}
