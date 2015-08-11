package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.presenter.IPresenter;
import edu.hm.cs.fs.common.model.PublicTransport;

/**
 * @author Fabio
 */
public interface IPublicTransportView<P extends IPresenter> extends IView<P> {
    void showContent(@NonNull final List<PublicTransport> content);
}
