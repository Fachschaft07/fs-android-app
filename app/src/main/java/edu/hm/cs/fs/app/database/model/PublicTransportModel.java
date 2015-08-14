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
 * Created by FHellman on 10.08.2015.
 */
public class PublicTransportModel implements IModel {
    private static PublicTransportModel mInstance;

    private PublicTransportModel() {
    }

    public static PublicTransportModel getInstance() {
        if(mInstance == null) {
            mInstance = new PublicTransportModel();
        }
        return mInstance;
    }

    public void getDepartureTimesForPasing(@NonNull final ICallback<List<PublicTransport>> callback) {
        getDepartureTimes(PublicTransportLocation.PASING, callback);
    }

    public void getDepartureTimesForLothstr(@NonNull final ICallback<List<PublicTransport>> callback) {
        getDepartureTimes(PublicTransportLocation.LOTHSTR, callback);
    }

    private void getDepartureTimes(@NonNull final PublicTransportLocation location, @NonNull final ICallback<List<PublicTransport>> callback) {
        Controllers.create(SERVER_IP, PublicTransportController.class)
                .getPublicTransports(location, new Callback<List<PublicTransport>>() {
                    @Override
                    public void success(List<PublicTransport> publicTransports, Response response) {
                        callback.onSuccess(publicTransports);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onError(ErrorFactory.network(error));
                    }
                });
    }
}
