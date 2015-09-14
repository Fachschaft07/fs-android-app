package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.LessonGroup;

/**
 * @author Fabio
 */
public interface ITimetableEditorView extends IView {
    /**
     *
     * @param data
     */
    void showContent(@NonNull final List<LessonGroup> data);
}
