package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.app.view.ITimetableView;

/**
 * @author Fabio
 */
public class TimetablePresenter extends BasePresenter<ITimetableView, TimetableModel> {

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

    public void loadTimetable() {
        getView().showLoading();
        /*
        getModel().get(new ICallback<List<Lesson>>() {
            @Override
            public void onSuccess(@NonNull List<Lesson> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
        */
    }
}
