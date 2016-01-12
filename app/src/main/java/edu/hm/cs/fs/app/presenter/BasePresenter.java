package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.domain.DataService;
import edu.hm.cs.fs.app.domain.IDataService;
import edu.hm.cs.fs.app.ui.IView;

/**
 * @author Fabio
 */
public abstract class BasePresenter<V extends IView> extends nz.bradcampbell.compartment.BasePresenter<V> {
    @Inject
    DataService mDataService;

    public IDataService getModel() {
        return mDataService;
    }
}
