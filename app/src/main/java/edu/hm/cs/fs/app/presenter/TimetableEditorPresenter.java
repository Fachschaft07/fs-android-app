package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.app.view.ITimetableEditorView;
import edu.hm.cs.fs.common.model.Group;

/**
 * Created by FHellman on 18.08.2015.
 */
public class TimetableEditorPresenter extends BasePresenter<ITimetableEditorView, TimetableModel> {

    /**
     * @param view
     */
    public TimetableEditorPresenter(@NonNull final Context context, @NonNull final ITimetableEditorView view) {
        this(view, ModelFactory.getTimetable(context));
    }

    /**
     * Needed for testing!
     */
    public TimetableEditorPresenter(@NonNull final ITimetableEditorView view, @NonNull final TimetableModel model) {
        super(view, model);
    }

    public void loadModules(@NonNull final Group group) {
        //getView().showLoading();
        /*
        getModel().getAllModules(group, new ICallback<List<Object>>() {
            @Override
            public void onSuccess(@NonNull List<Object> data) {
                // TODO Add modules
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

    public void deselectModule(@NonNull final String moduleId, @NonNull final String teacherId, final int pk) {
        //getModel().removeModule(moduleId, teacherId, pk);
    }

    public void selectModule(@NonNull final String moduleId, @NonNull final String teacherId, final int pk) {
        //getModel().addModule(moduleId, teacherId, pk);
    }

    public void reset() {
        //getModel().revert();
    }

    public void save() {
        //getModel().updateOnline();
    }
}
