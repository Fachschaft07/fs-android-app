package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.database.model.RoomSearchModel;
import edu.hm.cs.fs.app.view.IRoomSearchView;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Room;

/**
 * Created by FHellman on 11.08.2015.
 */
public class RoomSearchPresenter extends BasePresenter<IRoomSearchView, RoomSearchModel> {
    /**
     * @param view
     */
    public RoomSearchPresenter(IRoomSearchView view) {
        this(view, ModelFactory.getRoom());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public RoomSearchPresenter(IRoomSearchView view, RoomSearchModel model) {
        super(view, model);
    }

    public void loadCurrentFreeRooms() {
        Calendar cal = Calendar.getInstance();

        Day currDay = Day.MONDAY;
        for (Day day : Day.values()) {
            if (day.getCalendarId() == cal.get(Calendar.DAY_OF_WEEK)) {
                currDay = day;
                break;
            }
        }

        Time currTime = Time.LESSON_1;
        for (Time time : Time.values()) {
            final Calendar start = time.getStart();
            start.add(Calendar.MINUTE, -15); // Remove the break time
            if (start.before(cal) && time.getEnd().after(cal)) {
                currTime = time;
                break;
            }
        }

        getView().showCurrentDay(currDay);
        getView().showCurrentTime(currTime);

        loadFreeRooms(currDay, currTime);
    }

    public void loadFreeRooms(@NonNull final Day day, @NonNull final Time time) {
        getView().showLoading();
        getModel().getFreeRooms(day, time, new ICallback<List<Room>>() {
            @Override
            public void onSuccess(@NonNull List<Room> data) {
                Collections.sort(data, new Comparator<Room>() {
                    @Override
                    public int compare(Room lhs, Room rhs) {
                        return lhs.getFreeUntilEnd().getStart()
                                .compareTo(rhs.getFreeUntilEnd().getStart());
                    }
                });
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
