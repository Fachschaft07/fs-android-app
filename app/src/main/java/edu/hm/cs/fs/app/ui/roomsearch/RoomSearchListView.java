package edu.hm.cs.fs.app.ui.roomsearch;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.ui.IListView;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;

/**
 * @author Fabio
 */
public interface RoomSearchListView extends IListView<SimpleRoom> {

    /**
     * Displays the current day on the view.
     *
     * @param day to display.
     */
    void showCurrentDay(@NonNull final Day day);

    /**
     * Displays the current time on the view.
     *
     * @param time to display.
     */
    void showCurrentTime(@NonNull final Time time);
}
