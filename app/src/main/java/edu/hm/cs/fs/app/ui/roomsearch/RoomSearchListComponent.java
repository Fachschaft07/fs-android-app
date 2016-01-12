package edu.hm.cs.fs.app.ui.roomsearch;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.RoomSearchListPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface RoomSearchListComponent extends HasPresenter<RoomSearchListPresenter> {
    void inject(RoomSearchListFragment fragment);
}
