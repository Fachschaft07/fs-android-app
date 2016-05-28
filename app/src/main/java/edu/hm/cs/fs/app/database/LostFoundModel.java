package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.LostFound;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for lost & found.
 *
 * @author Fabio
 */
public class LostFoundModel implements IModel {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    /**
     * @param callback
     */
    public void getLostFound(@NonNull final ICallback<List<LostFound>> callback) {
        REST_CLIENT.getLostAndFound().enqueue(new Callback<List<LostFound>>() {
            @Override
            public void onResponse(Call<List<LostFound>> call, Response<List<LostFound>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<LostFound>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
