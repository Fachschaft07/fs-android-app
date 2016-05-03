package edu.hm.cs.fs.app.database.error;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.common.constant.ErrorCode;

/**
 * An error which was generated out of a http error.
 *
 * @author Fabio
 */
public class HttpError implements IError {

    private final Throwable mRetrofitError;
    //private final ExceptionResponse mExceptionResponse;

    /**
     * Creates a http error.
     *
     * @param error which was received.
     */
    protected HttpError(@NonNull final Throwable error) {
        mRetrofitError = error;
        /*
        mExceptionResponse = (ExceptionResponse) mRetrofitError
                .getBodyAs(new TypeToken<ExceptionResponse>() {
                }.getType());
                */
    }

    public boolean isConnected() {
        return true; //mRetrofitError.getKind() != RetrofitError.Kind.NETWORK;
    }

    @Override
    public String getMessage(@NonNull Context context) {
        /*
        final String message;
        if (mRetrofitError.getKind() == RetrofitError.Kind.NETWORK) {
            message = context.getString(R.string.network_error);
        } else {
            message = mRetrofitError.getLocalizedMessage();
        }
        */
        return mRetrofitError.getLocalizedMessage();
    }

    @Override
    public ErrorCode getErrorCode() {
        /*
        if(mExceptionResponse != null) {
            return ErrorCode.getErrorCodeByCode(mExceptionResponse.getErrorCode());
        }
        */
        return ErrorCode.ERROR_101;
    }
}
