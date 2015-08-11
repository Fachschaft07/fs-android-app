package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import edu.hm.cs.fs.app.database.IModel;

/**
 * @author Fabio
 */
public class InfoModel implements IModel {
    private static InfoModel mInstance;
    private final Context mContext;

    private InfoModel(final Context context) {
        mContext = context;
    }

    public static InfoModel getInstance(final Context context) {
        if (mInstance == null) {
            mInstance = new InfoModel(context);
        }
        return mInstance;
    }

    public String getVersion() {
        try {
            return mContext
                    .getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0)
                    .versionName;
        } catch (final PackageManager.NameNotFoundException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        return "";
    }
}
