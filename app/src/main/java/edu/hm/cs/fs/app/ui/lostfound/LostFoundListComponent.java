package edu.hm.cs.fs.app.ui.lostfound;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.LostFoundListPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface LostFoundListComponent extends HasPresenter<LostFoundListPresenter> {
    void inject(LostFoundListFragment fragment);
}
