package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.Meal;

/**
 * @author Fabio
 */
public interface IMealView extends IView {

    /**
     * Displays the meal entries on the view.
     *
     * @param content to display.
     */
    void showContent(@NonNull final List<Meal> content);
}
