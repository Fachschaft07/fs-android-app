package edu.hm.cs.fs.app.ui.fs.news;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.FsNewsDetailPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface FsNewsDetailComponent extends HasPresenter<FsNewsDetailPresenter> {
    void inject(FsNewsDetailFragment fragment);
}
