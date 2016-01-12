package edu.hm.cs.fs.app.ui.exam;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.ExamListPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface ExamListComponent extends HasPresenter<ExamListPresenter> {
    void inject(ExamListFragment fragment);
}
