package edu.hm.cs.fs.app.database;

/**
 * Created by Fabio on 12.07.2015.
 */
public interface ICallback<T> {
    void onSuccess(T data);

    void onError(String error);
}
