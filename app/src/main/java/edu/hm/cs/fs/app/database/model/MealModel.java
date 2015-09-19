package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.constant.StudentWorkMunich;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.restclient.FsRestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data only for mensa.
 *
 * @author Fabio
 */
public class MealModel extends CachedModel<Meal> {

    /**
     * Get all meal entries.
     *
     * @param refresh  should set to <code>true</code> if the blackboard entries should be updated
     *                 from the web.
     * @param callback to retrieve the result.
     */
    public void getAll(final boolean refresh, @NonNull final ICallback<List<Meal>> callback) {
        getData(refresh, callback);
    }

    @Override
    protected void update(@NonNull final ICallback<List<Meal>> callback) {
        FsRestClient.getV1().getMeals(StudentWorkMunich.MENSA_LOTHSTRASSE, new Callback<List<Meal>>() {
            @Override
            public void success(final List<Meal> meals, final Response response) {
                callback.onSuccess(meals);
            }

            @Override
            public void failure(final RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
