package edu.hm.cs.fs.app.view;

import java.util.List;

import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public interface ITimetableView extends IView {

    /**
     * Displays the lesson entries on the view.
     *
     * @param content to display.
     */
    void showContent(List<Lesson> content);
}