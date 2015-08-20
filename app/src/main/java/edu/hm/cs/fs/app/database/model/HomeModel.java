package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Meal;

/**
 * @author Fabio
 */
public class HomeModel implements IModel {
    public void getNewBlackboardEntries(@NonNull final ICallback<List<BlackboardEntry>> callback) {
        final BlackBoardModel blackboardModel = ModelFactory.getBlackboard();
        blackboardModel.getAll(false, new ICallback<List<BlackboardEntry>>() {
                    @Override
                    public void onSuccess(@NonNull List<BlackboardEntry> data) {
                        final List<BlackboardEntry> entries = new ArrayList<>();

                        for (BlackboardEntry entry : data) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -2);
                            if (cal.getTime().before(entry.getPublish())) {
                                entries.add(entry);
                            }
                        }

                        callback.onSuccess(entries);
                    }

                    @Override
                    public void onError(@NonNull IError error) {
                        callback.onError(error);
                    }
                }
        );
    }

    public void getMealsOfToday(@NonNull final ICallback<List<Meal>> callback) {
        final MealModel mealModel = ModelFactory.getMeal();
        mealModel.getAll(false, new ICallback<List<Meal>>() {
            @Override
            public void onSuccess(@NonNull List<Meal> data) {
                List<Meal> result = new ArrayList<>();
                for (Meal meal : data) {
                    if(isToday(meal.getDate())) {
                        result.add(meal);
                    }
                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(@NonNull IError error) {
                callback.onError(error);
            }

            private boolean isToday(Date date) {
                Calendar calToday = Calendar.getInstance();

                Calendar calMeal = Calendar.getInstance();
                calMeal.setTime(date);

                return calToday.get(Calendar.YEAR) == calMeal.get(Calendar.YEAR) &&
                        calToday.get(Calendar.MONTH) == calMeal.get(Calendar.MONTH) &&
                        calToday.get(Calendar.DAY_OF_MONTH) == calMeal.get(Calendar.DAY_OF_MONTH);
            }
        });
    }
}
