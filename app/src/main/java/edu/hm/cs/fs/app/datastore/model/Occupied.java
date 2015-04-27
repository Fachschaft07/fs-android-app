package edu.hm.cs.fs.app.datastore.model;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Time;

/**
 * Created by Fabio on 27.04.2015.
 */
public interface Occupied {
    String getRoom();

    Day getDay();

    Time getTime();
}
