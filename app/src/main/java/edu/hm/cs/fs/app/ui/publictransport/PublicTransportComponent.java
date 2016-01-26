package edu.hm.cs.fs.app.ui.publictransport;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.PublicTransportTabPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface PublicTransportComponent extends HasPresenter<PublicTransportTabPresenter> {
    void inject(PublicTransportTabFragment fragment);
}
