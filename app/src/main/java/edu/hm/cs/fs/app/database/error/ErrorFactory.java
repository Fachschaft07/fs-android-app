package edu.hm.cs.fs.app.database.error;

/**
 * A factory class for creating error messages of the specified type.
 *
 * @author Fabio
 */
public final class ErrorFactory {

    private ErrorFactory() {
    }

    /**
     * Creates a http error message.
     *
     * @param error which occurs.
     * @return the error.
     */
    public static IError http(Throwable error) {
        return new HttpError(error);
    }

    /**
     * Creates an exception error message.
     *
     * @param exception which occurs.
     * @return the error.
     */
    public static IError exception(Exception exception) {
        return new ExceptionError(exception);
    }
}
