package edu.hm.cs.fs.app.ui.publictransport;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.PublicTransportPasingPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface PasingListComponent extends HasPresenter<PublicTransportPasingPresenter> {
    void inject(PasingListFragment fragment);
}
