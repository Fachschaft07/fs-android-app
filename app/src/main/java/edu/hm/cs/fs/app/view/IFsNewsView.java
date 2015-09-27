package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public interface IFsNewsView extends IView {
    void showContent(@NonNull final List<News> data);
}
