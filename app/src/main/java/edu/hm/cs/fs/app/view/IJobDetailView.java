package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;
import android.text.Spanned;

import edu.hm.cs.fs.app.presenter.JobDetailPresenter;

/**
 * @author Fabio
 */
public interface IJobDetailView extends IDetailsView<JobDetailPresenter> {
    void showSubject(@NonNull final Spanned subject);

    void showProvider(@NonNull final String provider);

    void showDescription(@NonNull final Spanned description);

    void showUrl(@NonNull final String url);

    void showAuthor(@NonNull final String author);
}
