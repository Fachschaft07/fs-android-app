package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.LostFound;

/**
 * @author Fabio
 */
public interface ILostFoundView extends IView {

    /**
     * Displays the lost and found entries on the view.
     *
     * @param content to display.
     */
    void showContent(@NonNull final List<LostFound> content);
}
