package edu.hm.cs.fs.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Fabio
 */
public class DataServiceMigration implements Migration {
    private static final String LAST_VERSION = "last_version";
    private static final String NEW_VERSION = "new_version";
    private final Context mContext;
    @NonNull
    private final DataService mDataService;
    private final SharedPreferences mPreferences;

    public DataServiceMigration(@NonNull final Context context, @NonNull final DataService dataService) {
        mContext = context;
        mDataService = dataService;
        mPreferences = mContext.getSharedPreferences("VersionUpdates", Context.MODE_PRIVATE);
    }

    public void check() {
        if (getLastVersion() != getCurrVersion()) {
            mPreferences.edit().putLong(LAST_VERSION, getNewVersion()).apply();
            mPreferences.edit().putLong(NEW_VERSION, getCurrVersion()).apply();
            onUpdate(mContext, mDataService, getLastVersion(), getNewVersion());
        }
    }

    private long getLastVersion() {
        return mPreferences.getLong(LAST_VERSION, 0);
    }

    private long getNewVersion() {
        return mPreferences.getLong(NEW_VERSION, 0);
    }

    private long getCurrVersion() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignore) {
            Log.w(getClass().getSimpleName(), "Failed to update from old to new version");
        }
        return 0;
    }
}
