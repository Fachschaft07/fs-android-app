package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;
import android.text.Spanned;

import edu.hm.cs.fs.app.presenter.BlackBoardDetailPresenter;

/**
 * @author Fabio
 */
public interface IBlackBoardDetailView extends IDetailsView<BlackBoardDetailPresenter> {
    void showSubject(@NonNull final Spanned subject);

    void showGroups(@NonNull final String groups);

    void showDescription(@NonNull final Spanned description);

    void showUrl(@NonNull final String url);

    void showAuthor(@NonNull final String author);
}
