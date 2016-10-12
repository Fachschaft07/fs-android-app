package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.database.TimetableModel;
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
            public void onSuccess(@Nullable List<LessonGroup> data) {
                if(data != null) {
                    final List<LessonGroup> result = new ArrayList<>();
                    for (LessonGroup tmp : data) {
                        if(tmp.getModule() != null && tmp.getTeacher() != null && tmp.getGroup() != null) {
                            result.add(tmp);
                        }
                    }
                    getView().showContent(result);
                } else {
                    getView().showError(new IllegalArgumentException());
                }
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().showError(e);
                getView().hideLoading();
            }
        });
    }

    public boolean isPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        return getModel().isPkSelected(lessonGroup, pk);
    }

    public void setPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        getModel().save(lessonGroup, pk, true);
    }

    public boolean isLessonGroupSelected(@NonNull final LessonGroup lessonGroup) {
        return getModel().isModuleSelected(lessonGroup);
    }

    public void setLessonGroupSelected(@NonNull final LessonGroup lessonGroup,
                                       final boolean selected) {
        getModel().save(lessonGroup, selected);
    }

    public void reset() {
        getModel().resetConfiguration();
    }
}
