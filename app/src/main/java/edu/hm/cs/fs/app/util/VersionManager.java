package edu.hm.cs.fs.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author Fabio
 */
public class VersionManager {
    private static final String LAST_VERSION = "last_version";
    private static final String NEW_VERSION = "new_version";
    private final Context mContext;
    private final OnVersionUpgradeListener mListener;
    private final SharedPreferences mPreferences;

    public VersionManager(@NonNull final Context context,
                          @NonNull final OnVersionUpgradeListener listener) {
        mContext = context;
        mListener = listener;
        mPreferences = mContext.getSharedPreferences("VersionUpdates", Context.MODE_PRIVATE);
    }

    public void checkVersions() {
        mPreferences.edit().putInt(LAST_VERSION, getNewVersion()).apply();
        mPreferences.edit().putInt(NEW_VERSION, getCurrVersion()).apply();
        mListener.onUpdate(getLastVersion(), getNewVersion());
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

    public interface OnVersionUpgradeListener {
        void onUpdate(final int oldVersion, final int newVersion);
    }
}
