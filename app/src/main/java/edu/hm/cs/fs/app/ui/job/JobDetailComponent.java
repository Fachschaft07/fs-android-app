package edu.hm.cs.fs.app.ui.job;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.JobDetailPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface JobDetailComponent extends HasPresenter<JobDetailPresenter> {
    void inject(JobDetailFragment fragment);
}
