package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.ui.IView;
import rx.Subscriber;

public abstract class BasicSubscriber<T> extends Subscriber<T> {
    @NonNull
    private final IView mView;

    public BasicSubscriber(@NonNull final IView view) {
        mView = view;
    }

    @Override
    public void onCompleted() {
        mView.hideLoading();
    }

    @Override
    public void onError(Throwable e) {
        mView.showError(e);
        mView.hideLoading();
    }
}
