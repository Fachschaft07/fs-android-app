package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.presenter.IPresenter;

/**
 * @author Fabio
 */
public interface IView<P extends IPresenter> {
    void setPresenter(@NonNull final P presenter);

    void showLoading();

    void hideLoading();

    void showError(@NonNull final IError error);
}
