package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public interface IFsNewsView extends IView {
    /**
     * Displays the fs news items on the view.
     *
     * @param content to display.
     */
    void showContent(@NonNull final List<News> content);
}
