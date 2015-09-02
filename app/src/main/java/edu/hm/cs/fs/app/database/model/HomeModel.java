package edu.hm.cs.fs.app.database.model;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.LostFound;
import edu.hm.cs.fs.common.model.Meal;

/**
 * Requests the data from the the other models.
 *
 * @author Fabio
 */
public class HomeModel implements IModel {

    /**
     * The amount of days in the past to declare the blackboard entries as hot news and only return
     * them.
     */
    private static final int BLACKBOARD_DAYS_BACK = -2;

    /**
     * Get the blackboard entries which where not older then 2 days.
     *
     * @param callback to retrieve the result.
     */
    public void getNewBlackboardEntries(boolean refresh, @NonNull final ICallback<List<BlackboardEntry>> callback) {
        final BlackBoardModel blackboardModel = ModelFactory.getBlackboard();
        blackboardModel.getAll(refresh, new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(@NonNull List<BlackboardEntry> data) {
                final List<BlackboardEntry> entries = new ArrayList<>();

                for (BlackboardEntry entry : data) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, BLACKBOARD_DAYS_BACK);
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
        });
    }

    /**
     * Get the meals of today.
     *
     * @param callback to retrieve the result.
     */
    public void getMealsOfToday(boolean refresh, @NonNull final ICallback<List<Meal>> callback) {
        final MealModel mealModel = ModelFactory.getMeal();
        mealModel.getAll(refresh, new ICallback<List<Meal>>() {
            @Override
            public void onSuccess(@NonNull List<Meal> data) {
                List<Meal> result = new ArrayList<>();
                for (Meal meal : data) {
                    if (isToday(meal.getDate())) {
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
                Calendar today = Calendar.getInstance();

                Calendar calMeal = Calendar.getInstance();
                calMeal.setTime(date);

                return today.get(Calendar.YEAR) == calMeal.get(Calendar.YEAR) && today.get(Calendar.MONTH) == calMeal.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) == calMeal.get(Calendar.DAY_OF_MONTH);
            }
        });
    }

    /**
     * Get the next lesson.
     *
     * @param context  to access the timetable file.
     * @param callback to retrieve the result.
     */
    public void getNextLesson(@NonNull final Context context, @NonNull final ICallback<Lesson> callback) {
        ModelFactory.getTimetable(context).getNextLesson(callback);
    }

    /**
     * Get the amount of lost and founds.
     *
     * @param callback to retrieve the data.
     */
    public void getLostFound(@NonNull final ICallback<Integer> callback) {
        ModelFactory.getLostFound().getLostFound(new ICallback<List<LostFound>>() {
            @Override
            public void onSuccess(@NonNull List<LostFound> data) {
                callback.onSuccess(data.size());
            }

            @Override
            public void onError(@NonNull IError error) {
                callback.onError(error);
            }
        });
    }

    /**
     * @param callback
     */
    public void getNextHolidays(@NonNull final ICallback<Holiday> callback) {
        ModelFactory.getCalendar().getHolidays(new ICallback<List<Holiday>>() {
            @Override
            public void onSuccess(List<Holiday> data) {
                if (!data.isEmpty()) {
                    Collections.sort(data, new Comparator<Holiday>() {
                        @Override
                        public int compare(Holiday lhs, Holiday rhs) {
                            return lhs.getStart().compareTo(rhs.getStart());
                        }
                    });
                    callback.onSuccess(data.get(0));
                } else {
                    callback.onSuccess(null);
                }
            }

            @Override
            public void onError(@NonNull IError error) {
                callback.onError(error);
            }
        });
    }
}
