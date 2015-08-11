package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Room;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.RoomController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by FHellman on 11.08.2015.
 */
public class RoomModel implements IModel {
    private static RoomModel mInstance;

    private RoomModel() {
    }

    public static RoomModel getInstance() {
        if (mInstance == null) {
            mInstance = new RoomModel();
        }
        return mInstance;
    }

    public void getFreeRooms(@NonNull final Day day, @NonNull final Time time,
                             @NonNull final ICallback<List<Room>> callback) {
        Controllers.create(SERVER_IP, RoomController.class)
                .getHolidays(day, time, new Callback<List<Room>>() {
                    @Override
                    public void success(List<Room> rooms, Response response) {
                        Collections.sort(rooms, new Comparator<Room>() {
                            @Override
                            public int compare(Room lhs, Room rhs) {
                                return lhs.getFreeUntilEnd().getStart()
                                .compareTo(rhs.getFreeUntilEnd().getStart());
                            }
                        });
                        callback.onSuccess(rooms);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onError(error.getLocalizedMessage());
                    }
                });
    }
}
