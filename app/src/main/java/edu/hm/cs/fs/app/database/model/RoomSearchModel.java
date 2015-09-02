package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Room;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.RoomController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data from the {@link RoomController}.
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
    public void getFreeRooms(@NonNull final Day day, @NonNull final Time time, @NonNull final ICallback<List<Room>> callback) {
        //final int hour = time.getStart().get(Calendar.HOUR_OF_DAY);
        //final int minute = time.getStart().get(Calendar.MINUTE);
        Controllers.create(RoomController.class).getHolidays(day, time, new Callback<List<Room>>() {
            @Override
            public void success(List<Room> rooms, Response response) {
                callback.onSuccess(rooms);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
