package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.presenter.IPresenter;

/**
 * Created by Fabio on 12.07.2015.
 */
public interface IView<P extends IPresenter> {
    void setPresenter(@NonNull final P presenter);

    void showLoading();

    void hideLoading();

    void showError(@NonNull final String error);
}
