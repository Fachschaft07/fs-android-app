package edu.hm.cs.fs.app.database.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.common.model.Person;
import edu.hm.cs.fs.restclient.typeadapter.DateTypeAdapter;
import edu.hm.cs.fs.restclient.typeadapter.GroupTypeAdapter;

/**
 * @author Fabio
 */
public class TimetableModel extends CachedModel<Lesson> implements IModel {

    private static final String SHARED_PREFERENCES = "edu.hm.cs.fs.app.ui.timetable";

    private static final String CACHE_FILE = "timetable.cache";

    private static final String TAG_MODULE = "_module";

    private static final String TAG_TEACHER = "_teacher";

    private static final String TAG_PK = "_pk";

    private static final String TAG_SAVED = "_saved";

    private static final String CHARSET = "UTF-8";

    private static final int DAYS_OF_WEEK = 7;

    private static final int BREAK_TIME_LENGTH_MINUTES = 15;

    private final Context mContext;

    private final SharedPreferences mPrefs;

    private final Gson mGson;

    public TimetableModel(@NonNull final Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mGson = new GsonBuilder().registerTypeAdapter(Group.class, new GroupTypeAdapter()).registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
    }

    private static Lesson create(Day day, Time time, String room, String suffix, String moduleName) {
        Module module = new Module();
        module.setName(moduleName);

        Person person = new Person();
        person.setTitle("Prof. Dr.");
        person.setFirstName("Max");
        person.setLastName("Mustermann");

        Lesson lesson = new Lesson();
        lesson.setDay(day);
        lesson.setTime(time);
        lesson.setRoom(room);
        lesson.setSuffix(suffix);
        lesson.setModule(module);
        lesson.setTeacher(person);

        return lesson;
    }

    public void getLessonsByGroup(@NonNull final Group group, @NonNull final ICallback<List<Object>> callback) {
        // TODO Rest Connection
    }

    @NonNull
    public List<Lesson> getLessons(@NonNull final String moduleId, @NonNull final String teacherId, final int pk) {
        return new ArrayList<>(); // TODO Rest Connection
    }

    /**
     * Checks whether the item is offline available or not.
     */
    public boolean contains(@NonNull final String moduleId, @NonNull final String teacherId, final int pk) {
        // If one of this key-words exists, then the others will also exists.
        return mPrefs.contains(moduleId + teacherId + Integer.toString(pk) + TAG_MODULE);
    }

    /**
     * Adds the item offline. <b>IMPORTANT:</b> If {@link #sync()} wasn't called afterwards, the
     * data is not offline saved.
     */
    public void add(@NonNull final String moduleId, @NonNull final String teacherId, final int pk) {
        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(moduleId + teacherId + Integer.toString(pk) + TAG_MODULE, moduleId);
        edit.putString(moduleId + teacherId + Integer.toString(pk) + TAG_TEACHER, teacherId);
        edit.putInt(moduleId + teacherId + Integer.toString(pk) + TAG_PK, pk);
        edit.putBoolean(moduleId + teacherId + Integer.toString(pk) + TAG_SAVED, false);
        edit.apply();
    }

    /**
     * Removes the item from the offline ones. <b>IMPORTANT:</b> If {@link #sync()} wasn't called
     * afterwards, the data is not deleted.
     */
    public void remove(@NonNull final String moduleId, @NonNull final String teacherId, final int pk) {
        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_MODULE);
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_TEACHER);
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_PK);
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_SAVED);
        edit.apply();
    }

    public void getOfflineLessons(@NonNull final ICallback<List<Lesson>> callback) {
        // TODO getData(false, callback);

        // ONLY FOR TESTING!
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

    public void getNextLesson(@NonNull final ICallback<Lesson> callback) {
        getOfflineLessons(new ICallback<List<Lesson>>() {
            @Override
            public void onSuccess(@NonNull List<Lesson> data) {
                final Calendar calendar = Calendar.getInstance();
                int index = 0;
                Lesson nextLesson = null;
                while (nextLesson == null && index < DAYS_OF_WEEK) {
                    nextLesson = getNextLesson(calendar, data);
                    index++;
                    calendar.set(Calendar.HOUR_OF_DAY, Time.LESSON_1.getStart().get(Calendar.HOUR_OF_DAY));
                    calendar.set(Calendar.MINUTE, Time.LESSON_1.getStart().get(Calendar.MINUTE));
                    calendar.add(Calendar.DATE, 1);
                }
                callback.onSuccess(nextLesson);
            }

            private Lesson getNextLesson(Calendar calendar, List<Lesson> data) {
                List<Lesson> lessonsToday = new ArrayList<>();
                // Filter the lessons for the day which the calendar specifies
                for (Lesson lesson : data) {
                    if (lesson.getDay().getCalendarId() == calendar.get(Calendar.DAY_OF_WEEK)) {
                        lessonsToday.add(lesson);
                    }
                }

                // If nothing was found for this day, return null
                if (lessonsToday.isEmpty()) {
                    return null;
                }

                // Get the current time
                Time foundTime = null;
                for (Time time : Time.values()) {
                    final Calendar start = time.getStart();
                    // Remove the break time
                    start.add(Calendar.MINUTE, -BREAK_TIME_LENGTH_MINUTES);
                    if (start.before(calendar) && time.getEnd().after(calendar)) {
                        foundTime = time;
                        break;
                    }
                }

                if (foundTime == null) {
                    return null;
                } else {
                    // Current time found --> Next lesson
                    List<Time> times = Arrays.asList(Time.values());
                    final int currentTimeIndex = times.indexOf(foundTime) + 1;
                    if (currentTimeIndex < times.size()) {
                        foundTime = times.get(currentTimeIndex);
                    }
                }

                // Search for the lesson at the current time
                for (Lesson lesson : lessonsToday) {
                    if (lesson.getTime().getStart().get(Calendar.HOUR_OF_DAY) >= foundTime.getStart().get(Calendar.HOUR_OF_DAY) && lesson.getTime().getStart().get(Calendar.MINUTE) >= foundTime.getStart().get(Calendar.MINUTE)) {
                        return lesson;
                    }
                }

                // If everything failed... return null
                return null;
            }

            @Override
            public void onError(@NonNull IError error) {
                callback.onError(error);
            }
        });
    }

    private void save(@NonNull final List<Lesson> lessons) {
        FileOutputStream outputStream = null;
        try {
            outputStream = mContext.openFileOutput(CACHE_FILE, Context.MODE_PRIVATE);
            outputStream.write(mGson.toJson(lessons).getBytes(Charset.forName(CHARSET)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void revert() {
        // Remove all non saved entities
        for (String key : getAllKeys()) {
            final String moduleId = mPrefs.getString(key + TAG_MODULE, null);
            final String teacherId = mPrefs.getString(key + TAG_TEACHER, null);
            final int pk = mPrefs.getInt(key + TAG_PK, -1);
            final boolean saved = mPrefs.getBoolean(key + TAG_SAVED, false);
            if (!saved && moduleId != null && teacherId != null && pk != -1) {
                remove(moduleId, teacherId, pk);
            }
        }
    }

    public void sync() {

    }

    @Override
    protected void updateOnline(@NonNull final ICallback<List<Lesson>> callback) {
        new AsyncTask<Void, Void, List<Lesson>>() {
            @Override
            protected List<Lesson> doInBackground(Void... params) {
                final List<Lesson> dataToSave = new ArrayList<>();
                for (String key : getAllKeys()) {
                    final String moduleId = getModuleId(key);
                    final String teacherId = getTeacherId(key);
                    final int pk = getPk(key);
                    if (moduleId != null && teacherId != null && pk != -1) {
                        dataToSave.addAll(getLessons(moduleId, teacherId, pk));
                        // Flag item as saved
                        setSaved(key);
                    }
                }
                return dataToSave;
            }

            @Override
            protected void onPostExecute(List<Lesson> lessons) {
                callback.onSuccess(lessons);
            }
        }.execute();
    }

    @Override
    protected void updateOffline(@NonNull ICallback<List<Lesson>> callback) {
        super.updateOffline(callback);
            /*
            // TODO getTimetable
            FileInputStream inputStream = null;
            try {
                inputStream = mContext.openFileInput(CACHE_FILE);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                final List<Lesson> data = mGson.fromJson(new String(
                        buffer,
                        Charset.forName(CHARSET)
                ), new TypeToken<List<Lesson>>() {
                }.getType());
                callback.onSuccess(data);
            } catch (Exception e) {
                callback.onError(ErrorFactory.exception(e));
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            */
    }

    @Nullable
    private String getModuleId(@NonNull final String key) {
        return mPrefs.getString(TAG_MODULE, null);
    }

    @Nullable
    private String getTeacherId(@NonNull final String key) {
        return mPrefs.getString(TAG_TEACHER, null);
    }

    private int getPk(@NonNull final String key) {
        return mPrefs.getInt(TAG_PK, -1);
    }

    private List<String> getAllKeys() {
        // Find all non saved entities
        Set<String> mPrefKeys = new HashSet<>();
        for (String key : mPrefs.getAll().keySet()) {
            if (key.endsWith(TAG_MODULE)) {
                mPrefKeys.add(key.substring(0, key.indexOf(TAG_MODULE)));
            } else if (key.endsWith(TAG_TEACHER)) {
                mPrefKeys.add(key.substring(0, key.indexOf(TAG_TEACHER)));
            } else if (key.endsWith(TAG_PK)) {
                mPrefKeys.add(key.substring(0, key.indexOf(TAG_PK)));
            }
        }
        return new ArrayList<>(mPrefKeys);
    }

    private void setSaved(String key) {
        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.putBoolean(key + TAG_SAVED, true);
        edit.apply();
    }
}
