package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.exam.ExamListView;
import edu.hm.cs.fs.common.model.Exam;

@PerActivity
public class ExamListPresenter extends BasePresenter<ExamListView> {
    @Inject
    public ExamListPresenter() {
    }

    public void loadExams(boolean refresh) {
        getView().showLoading();
        getView().clear();
        getModel().exams(refresh).subscribe(new BasicSubscriber<Exam>(getView()) {
            @Override
            public void onNext(Exam exam) {
                getView().add(exam);
            }
        });
    }
}
