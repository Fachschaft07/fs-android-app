package edu.hm.cs.fs.app.domain;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public interface SchedulerProvider {
    SchedulerProvider DEFAULT = new SchedulerProvider() {
        @Override
        public <T> Observable.Transformer<T, T> applySchedulers() {
            return observable -> observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    <T> Observable.Transformer<T, T> applySchedulers();
}
