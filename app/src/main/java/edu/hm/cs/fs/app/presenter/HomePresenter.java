package edu.hm.cs.fs.app.presenter;

import java.util.ArrayList;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.home.HomeView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.News;
import rx.functions.Func0;

@PerActivity
public class HomePresenter extends BasePresenter<HomeView> {
    @Inject
    public HomePresenter() {
    }

    public void loadHappenings() {
        getView().clear();

        /*
        getModel().blackboardEntriesSinceYesterday()
                .collect((Func0<ArrayList<BlackboardEntry>>) ArrayList::new, ArrayList::add)
                .subscribe(blackboardEntries -> getView().showBlackboardNews(blackboardEntries));

        getModel().mealsOfToday()
                .collect((Func0<ArrayList<Meal>>) ArrayList::new, ArrayList::add)
                .subscribe(meals -> getView().showMealsOfToday(meals));

        getModel().nextLesson().subscribe(lesson -> getView().showNextLesson(lesson));

        getModel().lostfound().count().subscribe(count -> getView().showLostAndFound(count));

        getModel().nextHolidays().subscribe(holiday -> getView().showNextHoliday(holiday));

        getModel().fsNewsTop3()
                .collect((Func0<ArrayList<News>>) ArrayList::new, ArrayList::add)
                .subscribe(news -> getView().showFsNews(news));
                */
    }
}
