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

    public void getTimetable(final ICallback<List<Lesson>> callback) {
        // TODO getTimetable
        List<Lesson> list = new ArrayList<>();
        list.add(create(Day.MONDAY, Time.LESSON_2, "R0.011", null, "Analysis"));
        list.add(create(Day.MONDAY, Time.LESSON_3, "R0.011", null, "Analysis"));
        list.add(create(Day.MONDAY, Time.LESSON_5, "R1.006", null, "Lineare Algebra"));
        list.add(create(Day.MONDAY, Time.LESSON_6, "R3.023", "Praktikum", "Software Entwicklung I"));

        list.add(create(Day.TUESDAY, Time.LESSON_3, "R1.009", null, "Technische Informatik I"));
        list.add(create(Day.TUESDAY, Time.LESSON_4, "R0.005", null, "Software Entwicklung I"));

        list.add(create(Day.WEDNESDAY, Time.LESSON_1, "R1.011", "Praktikum", "Technische Informatik I"));
        list.add(create(Day.WEDNESDAY, Time.LESSON_3, "R0.007", null, "Software Entwicklung I"));

        list.add(create(Day.THURSDAY, Time.LESSON_2, "R1.005", null, "IT-Systeme I"));
        list.add(create(Day.THURSDAY, Time.LESSON_3, "R1.005", null, "IT-Systeme I"));
        list.add(create(Day.THURSDAY, Time.LESSON_4, "R1.009", "Praktikum", "IT-Systeme I"));

        callback.onSuccess(list);
    }

    public void getModule(final String moduleId, final String teacherName,
                          final ICallback<Module> callback) {
        // TODO getModule
    }

    private Lesson create(Day day, Time time, String room, String suffix, String moduleName) {
        Module module = new Module();
        module.setName(moduleName);

        Lesson lesson = new Lesson();
        lesson.setDay(day);
        lesson.setTime(time);
        lesson.setRoom(room);
        lesson.setSuffix(suffix);
        lesson.setModule(module);

        return lesson;
    }
}
