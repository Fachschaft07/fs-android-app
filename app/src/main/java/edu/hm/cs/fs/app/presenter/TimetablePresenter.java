package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.view.ITimetableView;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * Created by FHellman on 11.08.2015.
 */
public class TimetablePresenter extends BasePresenter<ITimetableView, TimetableModel> {
    /**
     * @param view
     */
    public TimetablePresenter(ITimetableView view) {
        this(view, TimetableModel.getInstance());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public TimetablePresenter(ITimetableView view, TimetableModel model) {
        super(view, model);
    }

    public void loadTimetable() {
        getView().showLoading();
        getModel().getTimetable(new ICallback<List<Lesson>>() {
            @Override
            public void onSuccess(List<Lesson> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
