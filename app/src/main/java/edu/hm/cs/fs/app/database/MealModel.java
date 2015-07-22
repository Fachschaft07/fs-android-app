package edu.hm.cs.fs.app.database;

import java.util.List;

import edu.hm.cs.fs.common.constant.StudentWorkMunich;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.MealController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Fabio on 12.07.2015.
 */
public class MealModel implements IModel {
    /**
     * @param callback
     */
    public void getMeals(final ICallback<List<Meal>> callback) {
        Controllers.create("192.168.178.107:8080", MealController.class)
                .getMeals(StudentWorkMunich.MENSA_LOTHSTRASSE, new Callback<List<Meal>>() {
                    @Override
                    public void success(final List<Meal> meals, final Response response) {
                        callback.onSuccess(meals);
                    }

                    @Override
                    public void failure(final RetrofitError error) {
                        callback.onError(error.getLocalizedMessage());
                    }
                });
    }
}
