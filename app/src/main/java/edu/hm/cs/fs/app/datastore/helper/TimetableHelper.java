package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hm.cs.fs.app.datastore.model.Course;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.Module;
import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.Timetable;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.model.impl.CourseImpl;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.impl.LessonImpl;
import edu.hm.cs.fs.app.datastore.model.impl.TimetableImpl;
import edu.hm.cs.fs.app.datastore.web.TimetableFk07Fetcher;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractContentFetcher;
import io.realm.Realm;

public class TimetableHelper extends BaseHelper implements Timetable {
	private Map<Day, List<Lesson>> lessons;

	TimetableHelper(final Context context, TimetableImpl timetable) {
    	super(context);

        lessons = new HashMap<>();
        List<LessonImpl> tmpLessons = timetable.getLessons();
        for (final LessonImpl lessonImpl : tmpLessons) {
        	final Time time = Time.of(lessonImpl.getTime());
        	final Person teacher = PersonHelper.findById(context, lessonImpl.getTeacher());
        	final String room = lessonImpl.getRoom();
        	final Module module = ModuleHelper.findById(context, lessonImpl.getModule());
        	final Day day = Day.of(lessonImpl.getDay());
        	final List<Course> courses = new ArrayList<>();
        	List<CourseImpl> tmpCourses = lessonImpl.getCourses();
        	for (CourseImpl courseImpl : tmpCourses) {
				final Group group = GroupImpl.of(courseImpl.getGroup());
				// We already have this data from the lesson
				//final Module module = ModuleHelper.findById(context, courseImpl.getModule());
				//final Person teacher = PersonHelper.findById(context, courseImpl.getTeacher());
				courses.add(new Course() {
					@Override
					public Module getModule() {
						return module;
					}

					@Override
					public Group getGroup() {
						return group;
					}

					@Override
					public Person getTeacher() {
						return teacher;
					}
				});
			}
        	
        	if(!lessons.containsKey(day)) {
        		lessons.put(day, new ArrayList<Lesson>());
        	}
        	lessons.get(day).add(new Lesson() {
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
				public Day getDay() {
					return day;
				}
				
				@Override
				public List<Course> getCourses() {
					return courses;
				}
			});
		}
    }

	@Override
	public void save() {
		new RealmExecutor<Void>(getContext()) {
			@Override
			public Void run(Realm realm) {
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
				for (LessonImpl lessonImpl : timetableImpl.getLessons()) {
					for (CourseImpl courseImpl : lessonImpl.getCourses()) {
						courseImpl.removeFromRealm();
					}
					lessonImpl.removeFromRealm();
				}
				timetableImpl.removeFromRealm();

                realm.commitTransaction();
				return null;
			}
		}.execute();
	}

	@Override
	public List<Lesson> getLessons(Day day) {
		return lessons.get(day);
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
		listAllOffline(context, TimetableImpl.class,
				new Callback<List<Timetable>>() {
					@Override
					public void onResult(List<Timetable> result) {
						callback.onResult(result.isEmpty() ? null : result
								.get(0));
					}
				}, new OnHelperCallback<Timetable, TimetableImpl>() {
					@Override
					public Timetable createHelper(Context context,
							TimetableImpl impl) {
						return new TimetableHelper(context, impl);
					}

					@Override
					public void copyToRealmOrUpdate(Realm realm,
							TimetableImpl impl) {
					}
				});
	}

	public static void listAll(final Context context, Faculty faculty,
			final Callback<List<Timetable>> callback) {
		@SuppressWarnings("rawtypes")
		AbstractContentFetcher fetcher = null;
		switch (faculty) {
		case _07:
			fetcher = new TimetableFk07Fetcher(context);
			break;
		default:
			break;
		}
		
		if(fetcher != null) {
			listAllOnline(context, fetcher, TimetableImpl.class,
					callback, new OnHelperCallback<Timetable, TimetableImpl>() {
						@Override
						public Timetable createHelper(Context context,
								TimetableImpl impl) {
							return new TimetableHelper(context, impl);
						}
	
						@Override
						public void copyToRealmOrUpdate(Realm realm,
								TimetableImpl impl) {
						}
					});
		}
	}
}
