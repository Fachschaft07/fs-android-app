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
import edu.hm.cs.fs.restclient.FsRestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data only for rooms.
 *
 * @author Fabio
 */
public class RoomSearchModel implements IModel {

    /**
     * Get all free rooms at the specified day and time (time also later ones).
     *
     * @param day      to search.
     * @param time     to search.
     * @param callback to retrieve the result.
     */
    public void getFreeRooms(@NonNull final Day day, @NonNull final Time time,
                             @NonNull final ICallback<List<SimpleRoom>> callback) {
        FsRestClient.getV1().getRoomByDateTime(
                RoomType.ALL,
                day,
                time.getStart().get(Calendar.HOUR_OF_DAY),
                time.getStart().get(Calendar.MINUTE),
                new Callback<List<SimpleRoom>>() {
            @Override
            public void success(List<SimpleRoom> rooms, Response response) {
                callback.onSuccess(rooms);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
