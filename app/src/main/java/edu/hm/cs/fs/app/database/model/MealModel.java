package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.constant.StudentWorkMunich;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.MealController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Fabio
 */
public class MealModel extends CachedModel<Meal> {
    /**
     *
     * @param refresh
     * @param callback
     */
    public void getAll(final boolean refresh, @NonNull final ICallback<List<Meal>> callback) {
        getData(refresh, callback);
    }

    @Override
    protected void update(@NonNull final ICallback<List<Meal>> callback) {
        Controllers.create(MealController.class)
                .getMeals(StudentWorkMunich.MENSA_LEOPOLDSTRASSE, new Callback<List<Meal>>() {
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
