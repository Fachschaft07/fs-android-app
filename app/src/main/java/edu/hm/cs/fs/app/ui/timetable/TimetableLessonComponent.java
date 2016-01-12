package edu.hm.cs.fs.app.ui.timetable;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.TimetableLessonPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface TimetableLessonComponent extends HasPresenter<TimetableLessonPresenter> {
    void inject(TimetableLessonFragment fragment);
}
