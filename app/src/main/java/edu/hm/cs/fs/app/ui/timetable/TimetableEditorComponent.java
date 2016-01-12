package edu.hm.cs.fs.app.ui.timetable;

import dagger.Component;
import edu.hm.cs.fs.app.AppComponent;
import edu.hm.cs.fs.app.presenter.TimetableEditorPresenter;
import edu.hm.cs.fs.app.ui.PerActivity;
import nz.bradcampbell.compartment.HasPresenter;

@Component(dependencies = AppComponent.class)
@PerActivity
public interface TimetableEditorComponent extends HasPresenter<TimetableEditorPresenter> {
    void inject(TimetableEditorFragment fragment);
}
