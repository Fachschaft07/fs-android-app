package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.List;
import java.util.Map;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Fabio on 18.02.2015.
 */
public class OccupiedImpl extends RealmObject {
	private String room;
    private String time;
    private String day;

    public String getRoom() {
        return room;
    }

    public void setRoom(final String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
