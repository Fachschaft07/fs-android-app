package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.MealModel;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.view.IMealView;
import edu.hm.cs.fs.common.model.Meal;

/**
 * @author Fabio
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
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull final Throwable e) {
                getView().showError(e);
                getView().hideLoading();
            }
        });
    }
}
