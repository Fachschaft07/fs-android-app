package edu.hm.cs.fs.app.database.model;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;

/**
 * Requests the data from the {@link Context}.
 *
 * @author Fabio
 */
public class InfoModel implements IModel {

    private final Context mContext;

    /**
     * Creates the model.
     *
     * @param context to access the package stuff.
     */
    public InfoModel(final Context context) {
        mContext = context;
    }

    /**
     * Get the version name of the app.
     *
     * @param callback to retrieve the result.
     */
    public void getVersion(@NonNull final ICallback<String> callback) {
        try {
            final String version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            callback.onSuccess(version);
        } catch (final PackageManager.NameNotFoundException e) {
            callback.onError(ErrorFactory.exception(e));
        }
    }
}
