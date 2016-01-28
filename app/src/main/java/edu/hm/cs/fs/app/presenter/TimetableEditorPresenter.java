package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.timetable.TimetableEditorListView;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.LessonGroup;
import rx.Observable;

@PerActivity
public class TimetableEditorPresenter extends BasePresenter<TimetableEditorListView> {
    @Inject
    public TimetableEditorPresenter() {
    }

    public void loadModules(@NonNull final Group group) {
        getView().showLoading();
        getView().clear();
        getModel().lessonsByGroup(group).subscribe(new BasicSubscriber<LessonGroup>(getView()) {
            @Override
            public void onNext(LessonGroup lessonGroup) {
                getView().add(lessonGroup);
            }
        });
    }

    public Observable<Boolean> isPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        return getModel().isPkSelected(lessonGroup, pk);
    }

    public void setPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        getModel().save(lessonGroup, pk, true);
    }

    public Observable<Boolean> isLessonGroupSelected(@NonNull final LessonGroup lessonGroup) {
        return getModel().isModuleSelected(lessonGroup);
    }

    public void setLessonGroupSelected(@NonNull final LessonGroup lessonGroup,
                                       final boolean selected) {
        getModel().save(lessonGroup, selected);
    }

    public void reset() {
        getModel().resetTimetable();
    }
}
