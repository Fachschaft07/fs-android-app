package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.meal.MealListView;
import edu.hm.cs.fs.common.model.Meal;

@PerActivity
public class MealListPresenter extends BasePresenter<MealListView> {
    @Inject
    public MealListPresenter() {
    }

    /**
     * @param refresh
     */
    public void loadMeals(final boolean refresh) {
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().meals(refresh).subscribe(new BasicSubscriber<Meal>(getView()) {
            @Override
            public void onNext(Meal meal) {
                getView().add(meal);
            }
        }));
    }
}
