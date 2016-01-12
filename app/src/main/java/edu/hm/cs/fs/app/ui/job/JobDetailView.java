package edu.hm.cs.fs.app.ui.job;

import android.support.annotation.NonNull;
import android.text.Spanned;

import edu.hm.cs.fs.app.ui.IDetailsView;

/**
 * @author Fabio
 */
public interface JobDetailView extends IDetailsView {

    /**
     * Displays the subject on the view.
     *
     * @param subject to display.
     */
    void showSubject(@NonNull final Spanned subject);

    /**
     * Displays the provider on the view.
     *
     * @param provider to display.
     */
    void showProvider(@NonNull final String provider);

    /**
     * Displays the description on the view.
     *
     * @param description to display.
     */
    void showDescription(@NonNull final Spanned description);

    /**
     * Displays the url on the view.
     *
     * @param url to display.
     */
    void showUrl(@NonNull final String url);

    /**
     * Displays the author on the view.
     *
     * @param author to display.
     */
    void showAuthor(@NonNull final String author);
}
