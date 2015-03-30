package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.web.LessonFk07Fetcher;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractContentFetcher;
import edu.hm.cs.fs.app.util.PrefUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 27.03.2015.
 */
public class LessonHelper extends BaseHelper implements Lesson {
    private final Day day;
    private final Time time;
    private final Person teacher;
    private final String room;
    private final Module module;
    private final String suffix;

    LessonHelper(Context context, LessonImpl lesson) {
        super(context);
        day = Day.of(lesson.getDay());
        time = Time.of(lesson.getTime());
        teacher = PersonHelper.findById(context, lesson.getTeacherId());
        room = lesson.getRoom();
        module = ModuleHelper.findById(context, lesson.getModuleId());
        suffix = lesson.getSuffix();
    }

    @Override
    public Day getDay() {
        return day;
    }

    @Override
    public Time getTime() {
        return time;
    }

    @Override
    public Person getTeacher() {
        return teacher;
    }

    @Override
    public String getRoom() {
        return room;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    public static void listAll(final Context context, final Faculty faculty, final Group group, final Callback<List<Lesson>> callback) {
        @SuppressWarnings("rawtypes")
        AbstractContentFetcher fetcher = null;
        switch (faculty) {
            case _07:
                fetcher = new LessonFk07Fetcher(context, group);
                break;
            default:
                throw new IllegalStateException("Faculty " + faculty.toString() + " is not supported yet");
        }

        PrefUtils.setUpdateInterval(context, fetcher.getClass(), TimeUnit.MILLISECONDS.convert(1l, TimeUnit.MILLISECONDS));

        listAll(context, fetcher, LessonImpl.class, callback, new OnHelperCallback<Lesson, LessonImpl>() {
            @Override
            public Lesson createHelper(Context context, LessonImpl lesson) {
                return new LessonHelper(context, lesson);
            }

            @Override
            public void copyToRealmOrUpdate(Realm realm, LessonImpl lesson) {
                realm.copyToRealm(lesson);
            }
        });
    }
}
