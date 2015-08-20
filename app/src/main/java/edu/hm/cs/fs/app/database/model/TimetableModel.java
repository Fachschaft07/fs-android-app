package edu.hm.cs.fs.app.database.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.restclient.typeadapter.DateTypeAdapter;
import edu.hm.cs.fs.restclient.typeadapter.GroupTypeAdapter;

/**
 * @author Fabio
 */
public class TimetableModel implements IModel {
    private static final String SHARED_PREFERENCES = "edu.hm.cs.fs.app.ui.timetable";
    private static final String CACHE_FILE = "timetable.cache";
    private static final String TAG_MODULE = "_module";
    private static final String TAG_TEACHER = "_teacher";
    private static final String TAG_PK = "_pk";
    private static final String TAG_SAVED = "_saved";
    public static final String CHARSET = "UTF-8";
    private final Context mContext;
    private final SharedPreferences mPrefs;
    private final Gson mGson;

    /**
     * @param context
     */
    public TimetableModel(@NonNull final Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mGson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }

    /**
     * @param group
     * @param callback
     */
    public void getAllModules(@NonNull final Group group,
                              @NonNull final ICallback<List<Object>> callback) {
    }

    /**
     * @param moduleId
     * @param teacherId
     * @param pk
     * @return
     */
    public boolean containsModule(@NonNull final String moduleId,
                                  @NonNull final String teacherId, final int pk) {
        // If one of this key-words exists, then the others will also exists.
        return mPrefs.contains(moduleId + teacherId + Integer.toString(pk) + TAG_MODULE);
    }

    /**
     * @param moduleId
     * @param teacherId
     * @param pk
     */
    public void removeModule(@NonNull final String moduleId,
                             @NonNull final String teacherId, final int pk) {
        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_MODULE);
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_TEACHER);
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_PK);
        edit.remove(moduleId + teacherId + Integer.toString(pk) + TAG_SAVED);
        edit.apply();
    }

    /**
     * @param moduleId
     * @param teacherId
     * @param pk
     */
    public void addModule(@NonNull final String moduleId,
                          @NonNull final String teacherId, final int pk) {
        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(moduleId + teacherId + Integer.toString(pk) + TAG_MODULE, moduleId);
        edit.putString(moduleId + teacherId + Integer.toString(pk) + TAG_TEACHER, teacherId);
        edit.putInt(moduleId + teacherId + Integer.toString(pk) + TAG_PK, pk);
        edit.putBoolean(moduleId + teacherId + Integer.toString(pk) + TAG_SAVED, false);
        edit.apply();
    }

    public List<Lesson> getLessons(@NonNull final String moduleId,
                                   @NonNull final String teacherId, final int pkGroup) {
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
        return list;
    }

    /**
     * @param callback
     */
    public void getTimetable(@NonNull final ICallback<List<Lesson>> callback) {

    }

    /**
     * @param callback
     */
    public void getNextLessons(@NonNull final ICallback<List<Lesson>> callback) {

    }

    /**
     *
     */
    public void update() {
        new AsyncTask<Void, Void, List<Lesson>>() {
            @Override
            protected List<Lesson> doInBackground(Void... params) {
                final List<Lesson> dataToCache = new ArrayList<>();
                final SharedPreferences.Editor edit = mPrefs.edit();
                String moduleId = null;
                String teacherId = null;
                int pk = -1;
                for(String key : mPrefs.getAll().keySet()) {
                    if(key.endsWith(TAG_MODULE)) {
                        moduleId = mPrefs.getString(TAG_MODULE, null);
                    } else if(key.endsWith(TAG_TEACHER)) {
                        teacherId = mPrefs.getString(TAG_TEACHER, null);
                    } else if(key.endsWith(TAG_PK)) {
                        pk = mPrefs.getInt(TAG_PK, -1);
                    } else if(moduleId != null && teacherId != null && pk != -1){
                        dataToCache.addAll(getLessons(moduleId, teacherId, pk));
                        // Flag item as saved
                        edit.putBoolean(key, true);
                    }
                }
                edit.apply();
                return dataToCache;
            }

            @Override
            protected void onPostExecute(List<Lesson> lessons) {
                super.onPostExecute(lessons);
                FileOutputStream outputStream = null;
                try {
                    outputStream = mContext.openFileOutput(CACHE_FILE, Context.MODE_PRIVATE);
                    outputStream.write(mGson.toJson(lessons).getBytes(Charset.forName(CHARSET)));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.execute();
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
