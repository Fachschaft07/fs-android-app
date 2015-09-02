package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.error.IError;


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
    void showError(@NonNull final IError error);
}
