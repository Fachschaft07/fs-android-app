package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Event;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.common.model.Person;
import edu.hm.cs.fs.restclient.CalendarController;
import edu.hm.cs.fs.restclient.Controllers;

/**
 * Created by FHellman on 11.08.2015.
 */
public class TimetableModel implements IModel {
    private static TimetableModel mInstance;

    private TimetableModel() {
    }

    public static TimetableModel getInstance() {
        if (mInstance == null) {
            mInstance = new TimetableModel();
        }
        return mInstance;
    }

    public void getTimetable(ICallback<List<Lesson>> callback) {
        // TODO getTimetable

        Module module = new Module();
        module.setName("Software Entwicklung I");

        Lesson lesson = new Lesson();
        lesson.setDay(Day.FRIDAY);
        lesson.setTime(Time.LESSON_3);
        lesson.setRoom("R3.023");
        lesson.setSuffix("Praktikum");
        lesson.setModule(module);

        callback.onSuccess(Arrays.asList(lesson));
    }
}
