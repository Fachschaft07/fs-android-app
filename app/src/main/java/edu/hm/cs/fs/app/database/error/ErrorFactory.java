package edu.hm.cs.fs.app.database.error;

import retrofit.RetrofitError;

/**
 * Created by FHellman on 14.08.2015.
 */
public final class ErrorFactory {
    private ErrorFactory() {
    }

    public static IError network(RetrofitError error) {
        return new NetworkError(error);
    }

    public static IError exception(Exception e) {
        return new ExceptionError(e);
    }
}
