package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.database.TimetableModel;
import edu.hm.cs.fs.app.model.EventWrapper;
import edu.hm.cs.fs.app.view.ITimetableView;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public class TimetablePresenter extends BasePresenter<ITimetableView, TimetableModel> {
    private final List<EventWrapper> mEventCache = new ArrayList<>();

    /**
     * @param context
     * @param view
     */
    public TimetablePresenter(@NonNull final Context context, @NonNull final ITimetableView view) {
        this(view, ModelFactory.getTimetable(context));
    }

    /**
     * Needed for testing!
     */
    public TimetablePresenter(@NonNull final ITimetableView view, @NonNull final TimetableModel model) {
        super(view, model);
    }

    public void loadTimetable(final boolean refresh, final int newYear, final int newMonth) {
        getView().showLoading();
        if(refresh) {
            mEventCache.clear();
        }
        getModel().getTimetable(refresh, new ICallback<List<Lesson>>() {
            @Override
            public void onSuccess(@NonNull List<Lesson> data) {
                getView().showContent(cache(EventWrapper.wrap(data, newYear, newMonth)));
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().showError(e);
                getView().hideLoading();
            }
        });
        if (refresh) {
            getModel().getHolidays(new ICallback<List<Holiday>>() {
                @Override
                public void onSuccess(List<Holiday> data) {
                    getView().showContent(cache(EventWrapper.wrap(data)));
                    getView().hideLoading();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    getView().showError(e);
                    getView().hideLoading();
                }
            });
            getModel().getExams(new ICallback<List<Exam>>() {
                @Override
                public void onSuccess(List<Exam> data) {
                    getView().showContent(cache(EventWrapper.wrap(data)));
                    getView().hideLoading();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    getView().showError(e);
                    getView().hideLoading();
                }
            });
        }
    }

    private List<EventWrapper> cache(@NonNull final List<EventWrapper> wrap) {
        for (EventWrapper event : wrap) {
            if (!mEventCache.contains(event)) {
                mEventCache.add(event);
            }
        }
        return mEventCache;
    }
}
