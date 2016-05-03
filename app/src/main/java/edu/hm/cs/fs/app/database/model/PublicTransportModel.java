package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.constant.PublicTransportLocation;
import edu.hm.cs.fs.common.model.PublicTransport;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for public transport.
 *
 * @author Fabio
 */
public class PublicTransportModel implements IModel {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    /**
     * Get every Public Transport from Pasing.
     *
     * @param callback to retrieve the data.
     */
    public void getPasing(@NonNull final ICallback<List<PublicTransport>> callback) {
        getDepartureTimes(PublicTransportLocation.PASING, callback);
    }

    /**
     * Get every Public Transport from Lothstrasse.
     *
     * @param callback to retrieve the data.
     */
    public void getLothstrasse(@NonNull final ICallback<List<PublicTransport>> callback) {
        getDepartureTimes(PublicTransportLocation.LOTHSTR, callback);
    }

    /**
     * Get every Public Transport by the location.
     *
     * @param location to get the Public Transport from.
     * @param callback to retrieve the data.
     */
    private void getDepartureTimes(@NonNull final PublicTransportLocation location, @NonNull final ICallback<List<PublicTransport>> callback) {
        REST_CLIENT.getPublicTransports(location).enqueue(new Callback<List<PublicTransport>>() {
            @Override
            public void onResponse(Call<List<PublicTransport>> call, Response<List<PublicTransport>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<PublicTransport>> call, Throwable t) {
                callback.onError(ErrorFactory.http(t));
            }
        });
    }
}
