package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.model.Presence;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.FsController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data from the {@link FsController}.
 *
 * @author Fabio
 */
public class FsModel implements IModel {

    /**
     * Get the presence.
     *
     * @param callback to retrieve the result.
     */
    public void getPresence(@NonNull final ICallback<List<Presence>> callback) {
        Controllers.create(FsController.class).getPresence(new Callback<List<Presence>>() {
            @Override
            public void success(List<Presence> presences, Response response) {
                callback.onSuccess(presences);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
