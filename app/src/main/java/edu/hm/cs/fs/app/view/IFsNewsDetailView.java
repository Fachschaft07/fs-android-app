package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

/**
 * @author Fabio
 */
public interface IFsNewsDetailView extends IDetailsView {
    void showTitle(@NonNull final String title);

    void showDescription(@NonNull final String description);

    void showDate(@NonNull final String format);

    void showLink(@NonNull final String link);
}
