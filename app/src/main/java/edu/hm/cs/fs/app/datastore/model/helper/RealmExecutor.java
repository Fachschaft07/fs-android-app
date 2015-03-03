package edu.hm.cs.fs.app.datastore.model.helper;

import android.content.Context;
import android.os.AsyncTask;

import io.realm.Realm;

/**
 * Created by Fabio on 28.02.2015.
 */
public abstract class RealmExecutor<T> {
    private final Context mContext;

    public RealmExecutor(Context context) {
        mContext = context;
    }

    public abstract T run(Realm realm);

    public T execute() {
        Realm mRealm = Realm.getInstance(mContext);
        try {
            return run(mRealm);
        } finally {
            mRealm.close();
        }
    }

    public void executeAsync(final Callback<T> callback) {
        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(final Void... params) {
                return RealmExecutor.this.execute();
            }

            @Override
            protected void onPostExecute(final T t) {
                super.onPostExecute(t);
                callback.onResult(t);
            }
        }.execute();
    }
}
