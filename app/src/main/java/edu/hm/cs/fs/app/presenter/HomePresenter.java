package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.HomeModel;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.view.IHomeView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;

/**
 * @author Fabio
 */
public class HomePresenter extends BasePresenter<IHomeView, HomeModel> {

    private final Context mContext;

    /**
     * @param context
     * @param view
     */
    public HomePresenter(Context context, IHomeView view) {
        this(context, view, ModelFactory.getHome());
    }

    /**
     * Needed for testing!
     */
    public HomePresenter(Context context, IHomeView view, HomeModel model) {
        super(view, model);
        mContext = context;
    }

    public void loadHappenings(boolean refresh) {
        getView().showLoading();
        getView().clear();

        getModel().getNewBlackboardEntries(refresh, new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(@NonNull List<BlackboardEntry> data) {
                getView().showBlackboardNews(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });

        getModel().getMealsOfToday(refresh, new ICallback<List<Meal>>() {
            @Override
            public void onSuccess(@NonNull List<Meal> data) {
                getView().showMealsOfToday(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });

        getModel().getNextLesson(mContext, new ICallback<Lesson>() {
            @Override
            public void onSuccess(@Nullable Lesson data) {
                getView().showNextLesson(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });

        getModel().getLostFound(new ICallback<Integer>() {
            @Override
            public void onSuccess(@NonNull Integer data) {
                getView().showLostAndFound(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });

        getModel().getNextHolidays(new ICallback<Holiday>() {
            @Override
            public void onSuccess(@Nullable Holiday data) {
                getView().showNextHoliday(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });

        getView().showAppRate();
    }
}
