package edu.hm.cs.fs.app.database.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;

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

    public void getVersion(@NonNull final ICallback<String> callback) {
        try {
            final String version = mContext
                    .getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0)
                    .versionName;
            callback.onSuccess(version);
        } catch (final PackageManager.NameNotFoundException e) {
            callback.onError(ErrorFactory.exception(e));
        }
    }
}
