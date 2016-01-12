package edu.hm.cs.fs.app.ui.timetable;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.TimetablePresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface TimetableComponent extends HasPresenter<TimetablePresenter> {
    void inject(TimetableFragment fragment);
}
