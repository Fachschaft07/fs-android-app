package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hm.cs.fs.app.datastore.model.Occupied;
import edu.hm.cs.fs.app.datastore.model.Room;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.OccupiedImpl;
import edu.hm.cs.fs.app.datastore.model.impl.RoomImpl;
import edu.hm.cs.fs.app.datastore.web.OccupiedFetcher;
import edu.hm.cs.fs.app.datastore.web.RoomFetcher;

/**
 * Created by Fabio on 27.04.2015.
 */
public class RoomHelper extends BaseHelper implements Room {
    private final String mRoom;
    private final Map<Day, List<Time>> mOccupiedMap = new HashMap<>();

    RoomHelper(Context context, RoomImpl room) {
        super(context);
        mRoom = room.getName();
    }

    @Override
    public String getName() {
        return mRoom;
    }

    @Override
    public boolean isFree(Day day, Time time) {
        return !mOccupiedMap.get(day).contains(time);
    }

    public Time getFreeUntil(Day day, Time start) {
        Time tmp = start;
        List<Time> times = mOccupiedMap.get(day);
        for (Time time : times) {
            if(time.compareTo(tmp) > 0) {
                tmp = time;
            }
        }
        return tmp;
    }

    @Override
    public void addOccupied(Day day, Time time) {
        if(!mOccupiedMap.containsKey(day)) {
            mOccupiedMap.put(day, new ArrayList<Time>());
        }
        mOccupiedMap.get(day).add(time);
    }

    public static void listAllFreeRooms(final Context context, final Day day, final Time time, final Callback<List<Room>> callback) {
        // First request all rooms
        listAll(context, new RoomFetcher(context), RoomImpl.class, new Callback<List<Room>>() {
            @Override
            public void onResult(final List<Room> allRooms) {
                // Next fetch all the occupied times for every room
                listAll(context, new OccupiedFetcher(context), OccupiedImpl.class, new Callback<List<Occupied>>() {
                    @Override
                    public void onResult(List<Occupied> result) {
                        // Assign occupied time to each room
                        for (Occupied occupied : result) {
                            Room room = getRoomByName(occupied.getRoom());
                            if(room != null) {
                                room.addOccupied(occupied.getDay(), occupied.getTime());
                            }
                        }

                        // Query free rooms for the specified day and time
                        List<Room> freeRooms = new ArrayList<>();
                        for (Room room : allRooms) {
                            if(room.isFree(day, time)) {
                                freeRooms.add(room);
                            }
                        }
                        callback.onResult(freeRooms);
                    }

                    private Room getRoomByName(String name) {
                        for (Room room : allRooms) {
                            if(room.getName().equals(name)) {
                                return room;
                            }
                        }
                        return null;
                    }
                }, new OnHelperCallback<Occupied, OccupiedImpl>() {
                    @Override
                    public Occupied createHelper(Context context, final OccupiedImpl occupied) {
                        return new Occupied() {
                            @Override
                            public String getRoom() {
                                return occupied.getRoom();
                            }

                            @Override
                            public Day getDay() {
                                return Day.of(occupied.getDay());
                            }

                            @Override
                            public Time getTime() {
                                return Time.of(occupied.getTime());
                            }
                        };
                    }
                });
            }
        }, new OnHelperCallback<Room, RoomImpl>() {
            @Override
            public Room createHelper(Context context, RoomImpl room) {
                return new RoomHelper(context, room);
            }
        });
    }
}
