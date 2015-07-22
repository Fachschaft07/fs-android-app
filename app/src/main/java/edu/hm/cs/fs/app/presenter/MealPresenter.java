package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.MealModel;
import edu.hm.cs.fs.app.view.IMealView;
import edu.hm.cs.fs.common.model.Meal;

/**
 * Created by Fabio on 12.07.2015.
 */
public class MealPresenter extends BasePresenter<IMealView, MealModel> {
    /**
     *
     * @param view
     */
    public MealPresenter(IMealView view) {
        super(view, new MealModel());
    }

    /**
     *
     */
    public void loadMeals() {
        getView().showLoading();
        getModel().getMeals(new ICallback<List<Meal>>() {
            @Override
            public void onSuccess(final List<Meal> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(final String error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
