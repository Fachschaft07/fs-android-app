package edu.hm.cs.fs.app.presenter;

import com.fk07.R;

import java.util.ArrayList;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.home.HomeView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.News;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

@PerActivity
public class HomePresenter extends BasePresenter<HomeView> {
    @Inject
    public HomePresenter() {
    }

    public void loadHappenings() {
        getView().clear();

        getModel().blackboardEntriesSinceYesterday(true)
                .collect(new Func0<ArrayList<BlackboardEntry>>() {
                    @Override
                    public ArrayList<BlackboardEntry> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<ArrayList<BlackboardEntry>, BlackboardEntry>() {
                    @Override
                    public void call(ArrayList<BlackboardEntry> blackboardEntries, BlackboardEntry blackboardEntry) {
                        blackboardEntries.add(blackboardEntry);
                    }
                })
                .filter(new Func1<ArrayList<BlackboardEntry>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<BlackboardEntry> data) {
                        return !data.isEmpty();
                    }
                })
                .subscribe(new Subscriber<ArrayList<BlackboardEntry>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ArrayList<BlackboardEntry> blackboardEntries) {
                        getView().showBlackboardNews(blackboardEntries);
                    }
                });

        getModel().mealsOfToday(true)
                .collect(new Func0<ArrayList<Meal>>() {
                    @Override
                    public ArrayList<Meal> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<ArrayList<Meal>, Meal>() {
                    @Override
                    public void call(ArrayList<Meal> meals, Meal meal) {
                        meals.add(meal);
                    }
                })
                .filter(new Func1<ArrayList<Meal>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<Meal> data) {
                        return !data.isEmpty();
                    }
                })
                .subscribe(new Subscriber<ArrayList<Meal>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ArrayList<Meal> meals) {
                        getView().showMealsOfToday(meals);
                    }
                });

        getModel().nextLesson()
                .subscribe(new Subscriber<Lesson>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(Lesson lesson) {
                        getView().showNextLesson(lesson);
                    }
                });

        getModel().lostfound(true).count()
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        getView().showLostAndFound(integer);
                    }
                });

        getModel().nextHolidays(true)
                .subscribe(new Subscriber<Holiday>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(Holiday holiday) {
                        getView().showNextHoliday(holiday);
                    }
                });

        getModel().fsNewsTop3(true)
                .collect(new Func0<ArrayList<News>>() {
                    @Override
                    public ArrayList<News> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<ArrayList<News>, News>() {
                    @Override
                    public void call(ArrayList<News> newsList, News news) {
                        newsList.add(news);
                    }
                })
                .filter(new Func1<ArrayList<News>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<News> data) {
                        return !data.isEmpty();
                    }
                })
                .subscribe(new Subscriber<ArrayList<News>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(new Exception(getContext().getString(R.string.news_error), e));
                    }

                    @Override
                    public void onNext(ArrayList<News> news) {
                        getView().showFsNews(news);
                    }
                });

        getModel().examsOfUser()
                .collect(new Func0<ArrayList<Exam>>() {
                    @Override
                    public ArrayList<Exam> call() {
                        return new ArrayList<>();
                    }
                }, new Action2<ArrayList<Exam>, Exam>() {
                    @Override
                    public void call(ArrayList<Exam> examList, Exam exam) {
                        examList.add(exam);
                    }
                })
                .filter(new Func1<ArrayList<Exam>, Boolean>() {
                    @Override
                    public Boolean call(ArrayList<Exam> exams) {
                        return !exams.isEmpty();
                    }
                })
                .subscribe(new Subscriber<ArrayList<Exam>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().showError(e);
                    }

                    @Override
                    public void onNext(ArrayList<Exam> exams) {
                        getView().showExams(exams);
                    }
                });

        getView().hideLoading();
    }
}
