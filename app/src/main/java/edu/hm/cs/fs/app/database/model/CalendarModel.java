package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for calendar.
 *
 * @author Fabio
 */
public class CalendarModel implements IModel {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    /**
     * @param callback
     */
    public void getNextHolidays(@NonNull final ICallback<List<Holiday>> callback) {
        REST_CLIENT.getHolidays().enqueue(new Callback<List<Holiday>>() {
            @Override
            public void onResponse(Call<List<Holiday>> call, Response<List<Holiday>> response) {
                final List<Holiday> body = response.body();
                for (int index = 0; index < body.size(); ) {
                    if (body.get(index).getStart().getTime() < System.currentTimeMillis()) {
                        body.remove(index);
                    } else {
                        index++;
                    }
                }
                callback.onSuccess(body);
            }

            @Override
            public void onFailure(Call<List<Holiday>> call, Throwable t) {
                callback.onError(ErrorFactory.http(t));
            }
        });
    }
}
