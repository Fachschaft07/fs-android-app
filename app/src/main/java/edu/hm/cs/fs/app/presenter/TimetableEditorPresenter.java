package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.app.view.ITimetableEditorView;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.LessonGroup;

/**
 * @author Fabio
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
        getView().showLoading();
        getModel().getLessonsByGroup(group, new ICallback<List<LessonGroup>>() {
            @Override
            public void onSuccess(@NonNull List<LessonGroup> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }

    public boolean isPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        return getModel().isPkSelected(lessonGroup, pk);
    }

    public void setPkSelected(@NonNull final LessonGroup lessonGroup, final int pk,
                              final boolean selected) {
        getModel().setPkSelected(lessonGroup, pk, selected);
    }

    public void setLessonGroupSelected(@NonNull final LessonGroup lessonGroup,
                                       final boolean selected) {
        getModel().setSelected(lessonGroup, selected);
    }

    public void cancel() {
        getModel().revert();
    }

    public void save() {
        getModel().save();
    }
}
