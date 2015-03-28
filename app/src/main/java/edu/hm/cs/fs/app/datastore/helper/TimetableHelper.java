package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.Timetable;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.model.impl.TimetableImpl;
import io.realm.Realm;

public class TimetableHelper extends BaseHelper implements Timetable {
	private Map<Day, List<Lesson>> lessons;

	TimetableHelper(final Context context, TimetableImpl timetable) {
    	super(context);

        lessons = new HashMap<>();
        final List<LessonImpl> tmpLessons = timetable.getLessons();
        for (final LessonImpl lessonImpl : tmpLessons) {
            final LessonHelper lesson = new LessonHelper(context, lessonImpl);
            final Day day = lesson.getDay();
            if(!lessons.containsKey(day)) {
        		lessons.put(day, new ArrayList<Lesson>());
        	}
        	lessons.get(day).add(lesson);
		}
    }

	@Override
	public void save() {
		new RealmExecutor<Void>(getContext()) {
			@Override
			public Void run(Realm realm) {
                TimetableImpl timetable = new TimetableImpl();
                timetable.setId("timetable");
				// TODO First convert TimetableHelper to TimetableImpl back...
				// realm.copyToRealmOrUpdate(impl);
				// for (LessonImpl lessonImpl : impl.getLessons()) {
				// realm.copyToRealmOrUpdate(lessonImpl);
				// for (CourseImpl courseImpl : lessonImpl.getCourses()) {
				// realm.copyToRealmOrUpdate(courseImpl);
				// }
				// }
				return null;
			}
		}.execute();
	}

	@Override
	public void delete() {
		new RealmExecutor<Void>(getContext()) {
			@Override
			public Void run(Realm realm) {
                realm.beginTransaction();

				TimetableImpl timetableImpl = realm.where(TimetableImpl.class).findFirst();
				timetableImpl.removeFromRealm();

                realm.commitTransaction();
				return null;
			}
		}.execute();
	}

	@Override
	public List<Lesson> getLessons(Day day) {
		if(lessons.containsKey(day)) {
            return lessons.get(day);
        }
        return new ArrayList<>();
	}

	@Override
	public void addLesson(Lesson lesson) {
		if (!lessons.containsKey(lesson.getDay())) {
			lessons.put(lesson.getDay(), new ArrayList<Lesson>());
		}
		lessons.get(lesson.getDay()).add(lesson);
	}

	@Override
	public void removeLesson(Lesson lesson) {
		lessons.get(lesson.getDay()).remove(lesson);
	}

	public static void getTimetable(final Context context,
			final Callback<Timetable> callback) {
        listAllOffline(context, TimetableImpl.class, new Callback<List<Timetable>>() {
            @Override
            public void onResult(List<Timetable> result) {

            }
        }, new OnHelperCallback<Timetable, TimetableImpl>() {
            @Override
            public Timetable createHelper(Context context, TimetableImpl timetable) {
                return null;
            }

            @Override
            public void copyToRealmOrUpdate(Realm realm, TimetableImpl timetable) {

            }
        });
	}
}
