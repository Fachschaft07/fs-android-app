package edu.hm.cs.fs.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractContentFetcher;

/**
 * Created by Fabio on 15.03.2015.
 */
public final class PrefUtils {
    private static final long DEFAULT_UPDATE_INTERVAL = TimeUnit.MILLISECONDS.convert(1l, TimeUnit.HOURS);

    private PrefUtils() {
    }

    public static <Fetcher extends AbstractContentFetcher<?, ?>> void setUpdateInterval(Context context, Class<Fetcher> fetcher, long intervalInMillisecond) {
        save(context, fetcher.getSimpleName() + "_update_interval", intervalInMillisecond);
    }

    public static <Fetcher extends AbstractContentFetcher<?, ?>> long getLastUpdate(Context context, Class<Fetcher> fetcher) {
        return find(context, fetcher.getSimpleName() + "_last_update", -1l);
    }

    public static <Fetcher extends AbstractContentFetcher<?, ?>> void setUpdated(Context context, Class<Fetcher> fetcher) {
        save(context, fetcher.getSimpleName() + "_last_update", new Date().getTime());
    }

    public static <Fetcher extends AbstractContentFetcher<?, ?>> boolean isNotUpToDate(Context context, Class<Fetcher> fetcher) {
        return System.currentTimeMillis() > find(context, fetcher.getClass().getSimpleName() + "_update_interval", DEFAULT_UPDATE_INTERVAL) + getLastUpdate(context, fetcher);
    }

    private static <T> void save(Context context, String key, T data) {
        final SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        }
        editor.apply();
    }

    private static <T> T find(Context context, String key, T defaultValue) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        T value = null;
        if (defaultValue instanceof String) {
            value = (T) prefs.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            value = (T) Integer.valueOf(prefs.getInt(key, (Integer) defaultValue));
        } else if (defaultValue instanceof Boolean) {
            value = (T) Boolean.valueOf(prefs.getBoolean(key, (Boolean) defaultValue));
        } else if (defaultValue instanceof Float) {
            value = (T) Float.valueOf(prefs.getFloat(key, (Float) defaultValue));
        } else if (defaultValue instanceof Long) {
            value = (T) Long.valueOf(prefs.getLong(key, (Long) defaultValue));
        }
        return value;
    }
}
