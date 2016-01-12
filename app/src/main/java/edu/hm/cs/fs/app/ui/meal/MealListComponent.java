package edu.hm.cs.fs.app.ui.meal;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.MealListPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface MealListComponent extends HasPresenter<MealListPresenter> {
    void inject(MealListFragment fragment);
}
