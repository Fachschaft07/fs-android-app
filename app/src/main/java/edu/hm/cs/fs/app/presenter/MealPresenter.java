package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.MealModel;
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
        this(view, MealModel.getInstance());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public MealPresenter(IMealView view, MealModel model) {
        super(view, model);
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
            public void onError(final IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
