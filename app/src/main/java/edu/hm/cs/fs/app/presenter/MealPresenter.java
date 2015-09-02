package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.MealModel;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.view.IMealView;
import edu.hm.cs.fs.common.model.Meal;

/**
 * Created by Fabio on 12.07.2015.
 */
public class MealPresenter extends BasePresenter<IMealView, MealModel> {

    /**
     * @param view
     */
    public MealPresenter(IMealView view) {
        this(view, ModelFactory.getMeal());
    }

    /**
     * Needed for testing!
     */
    public MealPresenter(IMealView view, MealModel model) {
        super(view, model);
    }

    /**
     * @param refresh
     */
    public void loadMeals(final boolean refresh) {
        getView().showLoading();
        getModel().getAll(refresh, new ICallback<List<Meal>>() {
            @Override
            public void onSuccess(@NonNull final List<Meal> data) {
                for (int index = 0; index < data.size(); ) {
                    if (isTodayOrFuture(data.get(index))) {
                        index++;
                    } else {
                        data.remove(index);
                    }
                }
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull final IError error) {
                getView().showError(error);
                getView().hideLoading();
            }

            private boolean isTodayOrFuture(@NonNull final Meal meal) {
                Calendar calendar = Calendar.getInstance();
                Calendar calendarMeal = Calendar.getInstance();
                calendarMeal.setTime(meal.getDate());
                return calendar.get(Calendar.YEAR) <= calendarMeal.get(Calendar.YEAR)
                        && calendar.get(Calendar.MONTH) <= calendarMeal.get(Calendar.MONTH)
                        && calendar.get(Calendar.DAY_OF_MONTH)
                        <= calendarMeal.get(Calendar.DAY_OF_MONTH);
            }
        });
    }
}
