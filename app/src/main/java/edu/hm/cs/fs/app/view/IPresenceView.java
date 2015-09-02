package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.Presence;

/**
 * @author Fabio
 */
public interface IPresenceView extends IView {

    /**
     * Displays the presence entries on the view.
     *
     * @param content to display.
     */
    void showContent(@NonNull final List<Presence> content);
}
