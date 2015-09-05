package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.restclient.FsRestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data only for calendar.
 *
 * @author Fabio
 */
public class CalendarModel implements IModel {

    /**
     * @param callback
     */
    public void getHolidays(@NonNull final ICallback<List<Holiday>> callback) {
        FsRestClient.getV1().getHolidays(new Callback<List<Holiday>>() {
            @Override
            public void success(List<Holiday> holidays, Response response) {
                callback.onSuccess(holidays);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
