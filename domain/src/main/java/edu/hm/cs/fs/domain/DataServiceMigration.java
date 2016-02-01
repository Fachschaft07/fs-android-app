package edu.hm.cs.fs.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Fabio
 */
public class DataServiceMigration {
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
        final long lastVersion = mPreferences.getLong("last_version", 0);
        long currVersion;
        try {
            currVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignore) {
            Log.w(getClass().getSimpleName(), "Failed to update from old to new version");
            currVersion = 0;
        }

        if (lastVersion != currVersion) {
            final long newVersion = mPreferences.getLong("new_version", 0);
            mPreferences.edit().putLong("last_version", newVersion).apply();
            mPreferences.edit().putLong("new_version", currVersion).apply();
            Migration.onUpdate(mContext, mDataService, lastVersion, newVersion);
        }
    }
}
