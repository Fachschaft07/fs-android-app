package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.exam.ExamListView;
import edu.hm.cs.fs.common.model.Exam;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

@PerActivity
public class ExamListPresenter extends BasePresenter<ExamListView> {
    @Inject
    public ExamListPresenter() {
    }

    public void loadExams(boolean refresh) {
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().exams(refresh).subscribe(new BasicSubscriber<Exam>(getView()) {
            @Override
            public void onNext(Exam exam) {
                getView().add(exam);
            }
        }));
    }

    public void search(String search) {
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().exams(false, search).subscribe(new BasicSubscriber<Exam>(getView()) {
            @Override
            public void onNext(Exam exam) {
                getView().add(exam);
            }
        }));
    }

    public Observable<Boolean> pin(Exam exam) {
        return getModel().pinExam(exam);
    }

    public Observable<Boolean> isPined(Exam exam) {
        return getModel().isExamPined(exam);
    }

    public void importFromTimetable() {
        getModel().examsByTimetable().flatMap(new Func1<Exam, Observable<?>>() {
            @Override
            public Observable<?> call(Exam exam) {
                return isPined(exam);
            }
        });
    }
}
