package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.RoomType;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for rooms.
 *
 * @author Fabio
 */
public class RoomSearchModel implements IModel {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    /**
     * Get all free rooms at the specified day and time (time also later ones).
     *
     * @param day      to search.
     * @param time     to search.
     * @param callback to retrieve the result.
     */
    public void getFreeRooms(@NonNull final Day day, @NonNull final Time time,
                             @NonNull final ICallback<List<SimpleRoom>> callback) {
        REST_CLIENT.getRoomByDateTime(
                RoomType.ALL,
                day,
                time.getStart().get(Calendar.HOUR_OF_DAY),
                time.getStart().get(Calendar.MINUTE))
                .enqueue(new Callback<List<SimpleRoom>>() {
                    @Override
                    public void onResponse(Call<List<SimpleRoom>> call, Response<List<SimpleRoom>> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<SimpleRoom>> call, Throwable t) {
                        callback.onError(ErrorFactory.http(t));
                    }
                });
    }
}
