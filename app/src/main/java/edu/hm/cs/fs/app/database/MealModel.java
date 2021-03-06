package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.constant.StudentWorkMunich;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for mensa.
 *
 * @author Fabio
 */
public class MealModel extends CachedModel<Meal> {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

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
        REST_CLIENT.getMeals(StudentWorkMunich.MENSA_LOTHSTRASSE).enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(Call<List<Meal>> call, Response<List<Meal>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Meal>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
