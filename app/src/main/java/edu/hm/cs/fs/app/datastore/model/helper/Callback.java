package edu.hm.cs.fs.app.datastore.model.helper;

/**
 * Created by Fabio on 18.02.2015.
 */
public interface Callback<T> {
	void onResult(T result);
}
