package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractContentFetcher;
import edu.hm.cs.fs.app.util.NetworkUtils;
import edu.hm.cs.fs.app.util.PrefUtils;
import hugo.weaving.DebugLog;
import io.realm.Realm;
import io.realm.RealmObject;

public abstract class BaseHelper {
    private final Context mContext;

    protected BaseHelper(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @DebugLog
    static <Interface, Impl extends RealmObject, Fetcher extends AbstractContentFetcher<Fetcher, Impl>> void listAll(final Context context, final Fetcher fetcher, final Class<Impl> implType, final Callback<List<Interface>> callback, final OnHelperCallback<Interface, Impl> creator) {
        listAllOffline(context, implType, callback, creator);

        if (PrefUtils.isRefreshable(context, fetcher.getClass())) {
            listAllOnline(context, fetcher, implType, callback, creator);
        }
    }

    @DebugLog
    static <Interface, Impl extends RealmObject, Fetcher extends AbstractContentFetcher<Fetcher, Impl>> void listAllOnline(final Context context, final Fetcher fetcher, final Class<Impl> implType, final Callback<List<Interface>> callback, final OnHelperCallback<Interface, Impl> creator) {
        // Request data from web...
        new RealmExecutor<List<Interface>>(context) {
            @Override
            public List<Interface> run(final Realm realm) {
                List<Interface> result = new ArrayList<>();
                if (NetworkUtils.isConnected(context)) {
                    List<Impl> implList = fetchOnlineData(fetcher, realm, true, creator);
                    for (Impl impl : implList) {
                        result.add(creator.createHelper(context, impl));
                    }
                }
                PrefUtils.setUpdated(context, fetcher.getClass());
                return result;
            }
        }.executeAsync(callback);
    }

    @DebugLog
    static <Interface, Impl extends RealmObject> void listAllOffline(final Context context, final Class<Impl> implType, final Callback<List<Interface>> callback, final OnHelperCallback<Interface, Impl> creator) {
        // Request data from database...
        new RealmExecutor<List<Interface>>(context) {
            @Override
            public List<Interface> run(final Realm realm) {
                List<Interface> result = new ArrayList<>();
                for (Impl impl : realm.where(implType).findAll()) {
                    result.add(creator.createHelper(context, impl));
                }
                return result;
            }
        }.executeAsync(callback);
    }

    @DebugLog
    static <Interface, Impl extends RealmObject, Fetcher extends AbstractContentFetcher<Fetcher, Impl>> List<Impl> fetchOnlineData(Fetcher fetcher, Realm realm, boolean reset, OnHelperCallback<Interface, Impl> callback) {
        List<Impl> implList = fetcher.fetch();
        if (!implList.isEmpty()) {
            // Delete old data
            realm.beginTransaction();
            callback.reset(realm);

            // Insert new data
            for (Impl impl : implList) {
                callback.copyToRealmOrUpdate(realm, impl);
            }
            realm.commitTransaction();
        }
        return implList;
    }

    abstract static class OnHelperCallback<T, Impl extends RealmObject> {
        private final Class<Impl>[] mClassesToReset;

        @SafeVarargs
        public OnHelperCallback(Class<Impl>... classesToReset) {
            mClassesToReset = classesToReset;
        }

        public abstract T createHelper(Context context, Impl impl);

        public void copyToRealmOrUpdate(Realm realm, Impl impl) {
        }

        private void reset(Realm realm) {
            for (Class<Impl> implClass : mClassesToReset) {
                realm.where(implClass).findAll().clear();
            }
        }
    }
}
