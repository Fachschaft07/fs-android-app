package edu.hm.cs.fs.app.ui.fs.presence;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.PresenceListPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface PresenceListComponent extends HasPresenter<PresenceListPresenter> {
    void inject(PresenceListFragment fragment);
}
