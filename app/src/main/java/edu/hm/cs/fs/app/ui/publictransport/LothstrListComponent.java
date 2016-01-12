package edu.hm.cs.fs.app.ui.publictransport;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.PublicTransportLothstrPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface LothstrListComponent extends HasPresenter<PublicTransportLothstrPresenter> {
    void inject(LothstrListFragment fragment);
}
