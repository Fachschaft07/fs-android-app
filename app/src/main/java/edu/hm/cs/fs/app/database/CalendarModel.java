package edu.hm.cs.fs.app.database;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Termin;
import edu.hm.cs.fs.restclient.CalendarController;
import edu.hm.cs.fs.restclient.Controllers;

/**
 * Created by FHellman on 11.08.2015.
 */
public class CalendarModel implements IModel {
    private static CalendarModel mInstance;
    private boolean mFixData;
    private List<WeekViewEvent> mHolidays;
    private List<WeekViewEvent> mTermins;

    private CalendarModel() {
    }

    public static CalendarModel getInstance() {
        if(mInstance == null) {
            mInstance = new CalendarModel();
        }
        return mInstance;
    }

    public void loadEvent(final int year, final int month,
                          @NonNull final ICallback<List<WeekViewEvent>> callback) {
        if(!mFixData) {
            final CalendarController controller = Controllers.create(SERVER_IP, CalendarController.class);

            // Request holidays and cache them
            List<Holiday> holidays = controller.getHolidays();
            mHolidays = new ArrayList<>();
            for (Holiday holiday : holidays) {
                WeekViewEvent event = new WeekViewEvent();
                event.setId(holiday.getName().hashCode());
                event.setName(holiday.getName());
                event.setColor(Color.YELLOW);
                event.setStartTime(dateToCalendar(holiday.getStart()));
                event.setEndTime(dateToCalendar(holiday.getEnd()));
                mHolidays.add(event);
            }

            // Request termins and cache them
            List<Termin> termins = controller.getTermins();
            mTermins = new ArrayList<>();
            for (Termin termin : termins) {
                WeekViewEvent event = new WeekViewEvent();
                event.setId(termin.getId().hashCode());
                event.setName(termin.getSubject());
                event.setStartTime(dateToCalendar(termin.getDate(), 18, 0));
                event.setEndTime(dateToCalendar(termin.getDate(), 20, 0));
                mTermins.add(event);
            }

            mFixData = true;
        }
    }

    private Calendar dateToCalendar(Date date) {
        return dateToCalendar(date, 0, 0);
    }

    private Calendar dateToCalendar(Date date, int hour, int minute) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    private boolean containsId(List<WeekViewEvent> list, long id) {
        for (WeekViewEvent event :
                list) {
            if (event.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
