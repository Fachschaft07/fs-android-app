package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.presenter.PresencePresenter;
import edu.hm.cs.fs.common.model.Presence;

/**
 * @author Fabio
 */
public interface IPresenceView extends IView<PresencePresenter> {
    void showContent(@NonNull final List<Presence> content);
}
