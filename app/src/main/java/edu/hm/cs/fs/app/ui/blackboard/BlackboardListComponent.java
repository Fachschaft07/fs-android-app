package edu.hm.cs.fs.app.ui.blackboard;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.BlackBoardListPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface BlackboardListComponent extends HasPresenter<BlackBoardListPresenter> {
    void inject(BlackBoardListFragment fragment);
}
