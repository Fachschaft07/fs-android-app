package edu.hm.cs.fs.app.presenter;

import android.content.Context;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.IView;
import edu.hm.cs.fs.domain.DataService;
import edu.hm.cs.fs.domain.IDataService;

/**
 * @author Fabio
 */
public abstract class BasePresenter<V extends IView> extends nz.bradcampbell.compartment.BasePresenter<V> {
    @Inject
    DataService mDataService;

    @Inject
    Context mContext;

    public IDataService getModel() {
        return mDataService;
    }

    public Context getContext() {
        return mContext;
    }
}
