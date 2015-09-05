package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;

/**
 * @author Fabio
 */
public interface IRoomSearchView extends IView {

    /**
     * Displays the room entries on the view.
     *
     * @param content to display.
     */
    void showContent(@NonNull final List<SimpleRoom> content);

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
