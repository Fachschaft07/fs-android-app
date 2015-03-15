package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractContentFetcher;
import edu.hm.cs.fs.app.util.NetworkUtils;
import edu.hm.cs.fs.app.util.PrefUtils;
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

    static <Interface, Impl extends RealmObject, Fetcher extends AbstractContentFetcher<Fetcher, Impl>> void listAll(final Context context, final Fetcher fetcher, final Class<Impl> implType, final Callback<List<Interface>> callback, final OnHelperCallback<Interface, Impl> creator) {
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

        if (PrefUtils.isNotUpToDate(context, fetcher.getClass())) {
            listAllOnline(context, fetcher, implType, callback, creator);
        }
    }

    static <Interface, Impl extends RealmObject, Fetcher extends AbstractContentFetcher<Fetcher, Impl>> void listAllOnline(final Context context, final Fetcher fetcher, final Class<Impl> implType, final Callback<List<Interface>> callback, final OnHelperCallback<Interface, Impl> creator) {
        // Request data from web...
        // TODO Don't update every time the device is connected to the internet
        new RealmExecutor<List<Interface>>(context) {
            @Override
            public List<Interface> run(final Realm realm) {
                List<Interface> result = new ArrayList<>();
                if (NetworkUtils.isConnected(context)) {
                    List<Impl> implList = fetchOnlineData(fetcher, realm, creator);
                    for (Impl impl : implList) {
                        result.add(creator.createHelper(context, impl));
                    }
                }
                PrefUtils.setUpdated(context, fetcher.getClass());
                return result;
            }
        }.executeAsync(callback);
    }

    static <Interface, Impl extends RealmObject, Fetcher extends AbstractContentFetcher<Fetcher, Impl>> void listAllOffline(final Context context, final Class<Impl> implType, final Callback<List<Interface>> callback, final OnHelperCallback<Interface, Impl> creator) {
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

    static <Interface, Impl extends RealmObject, Fetcher extends AbstractContentFetcher<Fetcher, Impl>> List<Impl> fetchOnlineData(Fetcher fetcher, Realm realm, OnHelperCallback<Interface, Impl> callback) {
        List<Impl> implList = fetcher.fetch();
        if (!implList.isEmpty()) {
            // Get Impl class
            ParameterizedType listType = (ParameterizedType) implList.getClass().getGenericSuperclass();
            Class<Impl> classType = (Class<Impl>) listType.getActualTypeArguments()[0].getClass();

            // Delete old data
            realm.beginTransaction();
            for (Impl impl : realm.where(classType).findAll()) {
                impl.removeFromRealm();
            }
            // Insert new data
            for (Impl impl : implList) {
                callback.copyToRealmOrUpdate(realm, impl);
            }
            realm.commitTransaction();
        }
        return implList;
    }

    interface OnHelperCallback<T, Impl> {
        T createHelper(Context context, Impl impl);

        void copyToRealmOrUpdate(Realm realm, Impl impl);
    }
}
