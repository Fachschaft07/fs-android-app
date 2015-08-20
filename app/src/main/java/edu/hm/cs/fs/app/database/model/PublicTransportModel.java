package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.constant.PublicTransportLocation;
import edu.hm.cs.fs.common.model.PublicTransport;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.PublicTransportController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Fabio
 */
public class PublicTransportModel implements IModel {
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
    private void getDepartureTimes(@NonNull final PublicTransportLocation location,
                                   @NonNull final ICallback<List<PublicTransport>> callback) {
        Controllers.create(PublicTransportController.class)
                .getPublicTransports(location, new Callback<List<PublicTransport>>() {
                    @Override
                    public void success(List<PublicTransport> publicTransports, Response response) {
                        callback.onSuccess(publicTransports);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onError(ErrorFactory.http(error));
                    }
                });
    }
}
