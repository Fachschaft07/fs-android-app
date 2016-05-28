package edu.hm.cs.fs.app.view;

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
     * @param e to display.
     */
    void showError(@NonNull final Throwable e);
}
