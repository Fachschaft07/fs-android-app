package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.presenter.InfoPresenter;

/**
 * @author Fabio
 */
public interface IInfoView extends IView<InfoPresenter> {
    void showVersion(@NonNull final String version);
}
