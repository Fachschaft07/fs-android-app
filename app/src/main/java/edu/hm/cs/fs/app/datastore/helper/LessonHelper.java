package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.web.LessonFk07Fetcher;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractContentFetcher;
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
    private final Group group;

    LessonHelper(Context context, LessonImpl lesson) {
        super(context);
        day = Day.of(lesson.getDay());
        time = Time.of(lesson.getTime());
        teacher = PersonHelper.findById(context, lesson.getTeacherId());
        room = lesson.getRoom();
        module = ModuleHelper.findById(context, lesson.getModuleId());
        suffix = lesson.getSuffix();
        group = GroupImpl.of(lesson.getGroup());
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

    @Override
    public Group getGroup() {
        return group;
    }

    public static void getLessonsByModuleGroup(final Context context, final Faculty faculty, final List<Module> modules, final List<Group> groups, final Callback<List<Lesson>> callback) {
        listAll(context, faculty, new Callback<List<Lesson>>() {
            @Override
            public void onResult(final List<Lesson> result) {

            }
        });
    }

    public static void getLessonsByGroups(final Context context, final Faculty faculty, final List<Group> groups, final Callback<List<Lesson>> callback) {
        listAll(context, faculty, new Callback<List<Lesson>>() {
            @Override
            public void onResult(final List<Lesson> result) {
                List<Lesson> filtered = new ArrayList<Lesson>();
                for (Lesson lesson : result) {
                    if(groups.contains(lesson.getGroup())) {
                        filtered.add(lesson);
                    }
                }
                callback.onResult(filtered);
            }
        });
    }

    public static void getGroups(final Context context, final Faculty faculty, final Callback<List<Group>> callback) {
        listAll(context, faculty, new Callback<List<Lesson>>() {
            @Override
            public void onResult(final List<Lesson> result) {
                List<Group> groupList = new ArrayList<>();
                for (Lesson lesson : result) {
                    if(!groupList.contains(lesson.getGroup())) {
                        groupList.add(lesson.getGroup());
                    }
                }
                callback.onResult(groupList);
            }
        });
    }

    public static void listAll(final Context context, final Faculty faculty, final Group group, final Callback<List<Lesson>> callback) {
        listAll(context, faculty, new Callback<List<Lesson>>() {
            @Override
            public void onResult(final List<Lesson> result) {
                List<Lesson> lessons = new ArrayList<>();
                for (Lesson lesson : result) {
                    if (lesson.getGroup().equals(group) ||
                            lesson.getGroup().getStudy() == group.getStudy() &&
                                    lesson.getGroup().getSemester() == group.getSemester() ||
                            lesson.getGroup().getStudy() == group.getStudy()) {
                        lessons.add(lesson);
                    }
                }
                callback.onResult(lessons);
            }
        });
    }

    public static void listAll(final Context context, final Faculty faculty, final Callback<List<Lesson>> callback) {
        @SuppressWarnings("rawtypes")
        AbstractContentFetcher fetcher = null;
        switch (faculty) {
            case _07:
                fetcher = new LessonFk07Fetcher(context);
                break;
            default:
                throw new IllegalStateException("Faculty " + faculty.toString() + " is not supported yet");
        }

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
