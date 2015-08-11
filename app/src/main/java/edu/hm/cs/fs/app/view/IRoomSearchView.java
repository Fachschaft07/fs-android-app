package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.presenter.RoomSearchPresenter;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Room;

/**
 * @author Fabio
 */
public interface IRoomSearchView extends IView<RoomSearchPresenter> {
    void showContent(@NonNull final List<Room> content);

    void showCurrentDay(@NonNull final Day day);

    void showCurrentTime(@NonNull final Time time);
}
