package edu.hm.cs.fs.app.view;

import edu.hm.cs.fs.app.presenter.IPresenter;

/**
 * @author Fabio
 */
public interface IDetailsView<P extends IPresenter> extends IView<P> {
    void close();
}
