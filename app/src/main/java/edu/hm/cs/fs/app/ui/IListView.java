package edu.hm.cs.fs.app.ui;

import android.support.annotation.NonNull;


/**
 * @author Fabio
 */
public interface IListView<T> extends IView {
    void clear();

    void add(@NonNull final T item);
}
