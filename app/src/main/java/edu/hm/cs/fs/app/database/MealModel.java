package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

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
    private static MealModel mInstance;

    private MealModel() {
    }

    public static MealModel getInstance() {
        if(mInstance == null) {
            mInstance = new MealModel();
        }
        return mInstance;
    }

    /**
     * @param callback
     */
    public void getMeals(@NonNull final ICallback<List<Meal>> callback) {
        Controllers.create(SERVER_IP, MealController.class)
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
