package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.timetable.TimetableListView;
import edu.hm.cs.fs.common.model.Lesson;

@PerActivity
public class TimetablePresenter extends BasePresenter<TimetableListView> {
    @Inject
    public TimetablePresenter() {
    }

    public void loadTimetable(final boolean refresh) {
        getView().showLoading();
        getView().clear();
        getModel().timetable(refresh).subscribe(new BasicSubscriber<Lesson>(getView()) {
            @Override
            public void onNext(Lesson lesson) {
                getView().add(lesson);
            }
        });
    }
}
