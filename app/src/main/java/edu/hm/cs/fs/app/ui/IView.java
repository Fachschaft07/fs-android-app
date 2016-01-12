package edu.hm.cs.fs.app.ui;

import android.support.annotation.NonNull;


/**
 * @author Fabio
 */
public interface IView {

    /**
     * Displays the loading view.
     */
    void showLoading();

    /**
     * Hides the loading view.
     */
    void hideLoading();

    /**
     * Displays a Snackbar with the error message.
     *
     * @param error to display.
     */
    void showError(@NonNull final Throwable error);
}
