package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

/**
 * @author Fabio
 */
public interface IInfoView extends IView {

    /**
     * Displays the version on the view.
     *
     * @param version to display.
     */
    void showVersion(@NonNull final String version);
}
