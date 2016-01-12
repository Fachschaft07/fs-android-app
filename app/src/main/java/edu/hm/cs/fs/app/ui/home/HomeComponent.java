package edu.hm.cs.fs.app.ui.home;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.HomePresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface HomeComponent extends HasPresenter<HomePresenter> {
    void inject(HomeFragment fragment);
}
