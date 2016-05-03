package edu.hm.cs.fs.app.presenter;

import android.content.Context;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.IView;
import edu.hm.cs.fs.domain.DataService;
import edu.hm.cs.fs.domain.IDataService;
import rx.Subscriber;
import rx.Subscription;

/**
 * @author Fabio
 */
public abstract class BasePresenter<V extends IView> extends nz.bradcampbell.compartment.BasePresenter<V> {
    @Inject
    DataService mDataService;

    @Inject
    Context mContext;

    private Subscription mSubscriber;

    public IDataService getModel() {
        return mDataService;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean checkSubscriber() {
        return mSubscriber == null || !mSubscriber.isUnsubscribed();
    }

    public void setSubscriber(Subscription subscriber) {
        this.mSubscriber = subscriber;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSubscriber != null) {
            mSubscriber.unsubscribe();
            mSubscriber = null;
        }
    }
}
