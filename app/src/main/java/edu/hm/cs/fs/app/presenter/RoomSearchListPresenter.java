package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.Calendar;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.roomsearch.RoomSearchListView;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;

@PerActivity
public class RoomSearchListPresenter extends BasePresenter<RoomSearchListView> {
    @Inject
    public RoomSearchListPresenter() {
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
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().freeRooms(day, time).subscribe(new BasicSubscriber<SimpleRoom>(getView()) {
            @Override
            public void onNext(SimpleRoom simpleRoom) {
                getView().add(simpleRoom);
            }
        }));
    }
}
