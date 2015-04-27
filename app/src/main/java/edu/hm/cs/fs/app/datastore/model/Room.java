package edu.hm.cs.fs.app.datastore.model;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;

/**
 * Created by Fabio on 27.04.2015.
 */
public interface Room {
    String getName();

    boolean isFree(Day day, Time time);

    Time getFreeUntil(Day day, Time start);

    void addOccupied(Day day, Time time);
}
