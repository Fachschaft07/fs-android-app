package edu.hm.cs.fs.domain;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * The scheduler provider is responsible for transforming an Observable to the main thread.
 */
public interface SchedulerProvider {
    /**
     * The default scheduler provider can be used in every case to transform the Observable to
     * the main thread.
     */
    SchedulerProvider DEFAULT = new SchedulerProvider() {
        @Override
        public <T> Observable.Transformer<T, T> applySchedulers() {
            return observable -> observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * Applies a scheduler to an Observable.
     *
     * @param <T> is the generic type of the Observables Transformer.
     * @return the transformer.
     */
    <T> Observable.Transformer<T, T> applySchedulers();
}
