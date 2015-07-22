package edu.hm.cs.fs.app.view;

import edu.hm.cs.fs.app.presenter.IPresenter;

/**
 * Created by Fabio on 12.07.2015.
 */
public interface IDetailsView<T, P extends IPresenter> extends IView<P> {
    void setData(T data);

    void close();
}
