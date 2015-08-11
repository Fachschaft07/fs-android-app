package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.presenter.BlackBoardPresenter;
import edu.hm.cs.fs.common.model.BlackboardEntry;

/**
 * @author Fabio
 */
public interface IBlackBoardView extends IView<BlackBoardPresenter> {
    void showContent(@NonNull final List<BlackboardEntry> content);
}
