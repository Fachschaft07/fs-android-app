package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.model.LostFound;
import edu.hm.cs.fs.restclient.FsRestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data only for lost & found.
 *
 * @author Fabio
 */
public class LostFoundModel implements IModel {
    /**
     *
     * @param callback
     */
    public void getLostFound(@NonNull final ICallback<List<LostFound>> callback) {
        FsRestClient.getV1().getLostAndFound(new Callback<List<LostFound>>() {
            @Override
            public void success(List<LostFound> lostFounds, Response response) {
                callback.onSuccess(lostFounds);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
