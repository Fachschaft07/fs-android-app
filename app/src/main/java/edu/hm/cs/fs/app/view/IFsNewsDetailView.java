package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

/**
 * @author Fabio
 */
public interface IFsNewsDetailView extends IDetailsView {
    /**
     * Displays the title on the view.
     *
     * @param title to display.
     */
    void showTitle(@NonNull final String title);

    /**
     * Displays the description on the view.
     *
     * @param description to display.
     */
    void showDescription(@NonNull final String description);

    /**
     * Displays the date on the view.
     *
     * @param date to display.
     */
    void showDate(@NonNull final String date);

    /**
     * Displays the link on the view.
     *
     * @param link to display.
     */
    void showLink(@NonNull final String link);
}
