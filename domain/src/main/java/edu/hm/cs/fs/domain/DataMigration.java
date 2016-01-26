package edu.hm.cs.fs.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Fabio
 */
public class DataMigration {
    private static final String LAST_VERSION = "last_version";
    private static final String NEW_VERSION = "new_version";
    private final Context mContext;
    private final SharedPreferences mPreferences;
    private final VersionMigrate mMigrator;

    public DataMigration(@NonNull final Context context, @NonNull final DataService dataService) {
        mContext = context;
        mPreferences = mContext.getSharedPreferences("VersionUpdates", Context.MODE_PRIVATE);

        mMigrator = createMigrate(dataService);
    }

    private VersionMigrate createMigrate(@NonNull final DataService dataService) {
        return (oldVersion, newVersion) -> {
            if (newVersion == VersionMigrate.VERSION_CODE_201) {
                // Version 2.0.1
                dataService.resetTimetable();
            } else if (newVersion > VersionMigrate.VERSION_CODE_2110) {
                // TODO Migrate Timetable from file to diskservice
            }
        };
    }

    public void checkVersions() {
        if (getLastVersion() != getCurrVersion()) {
            mPreferences.edit().putInt(LAST_VERSION, getNewVersion()).apply();
            mPreferences.edit().putInt(NEW_VERSION, getCurrVersion()).apply();
            mMigrator.onUpdate(getLastVersion(), getNewVersion());
        }
    }

    private int getLastVersion() {
        return mPreferences.getInt(LAST_VERSION, 0);
    }

    private int getNewVersion() {
        return mPreferences.getInt(NEW_VERSION, 0);
    }

    private int getCurrVersion() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException ignore) {
            Log.w(getClass().getSimpleName(), "Failed to update from old to new version");
        }
        return 0;
    }

    public interface VersionMigrate {
        int VERSION_CODE_201 = 201;
        int VERSION_CODE_2110 = 2110;

        void onUpdate(final int oldVersion, final int newVersion);
    }
}
