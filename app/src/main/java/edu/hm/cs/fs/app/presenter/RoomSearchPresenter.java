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
import edu.hm.cs.fs.common.model.simple.SimpleRoom;

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
        getModel().getFreeRooms(day, time, new ICallback<List<SimpleRoom>>() {
            @Override
            public void onSuccess(@NonNull List<SimpleRoom> data) {
                Collections.sort(data, new Comparator<SimpleRoom>() {
                    @Override
                    public int compare(SimpleRoom lhs, SimpleRoom rhs) {
                        Calendar calendarLhs = Calendar.getInstance();
                        calendarLhs.set(Calendar.HOUR_OF_DAY, lhs.getHour());
                        calendarLhs.set(Calendar.MINUTE, lhs.getMinute());

                        Calendar calendarRhs = Calendar.getInstance();
                        calendarRhs.set(Calendar.HOUR_OF_DAY, rhs.getHour());
                        calendarRhs.set(Calendar.MINUTE, rhs.getMinute());

                        return calendarLhs.compareTo(calendarRhs);
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
