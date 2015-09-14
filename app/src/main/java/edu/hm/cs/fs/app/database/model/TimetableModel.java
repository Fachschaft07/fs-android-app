package edu.hm.cs.fs.app.database.model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.LessonGroup;
import edu.hm.cs.fs.restclient.FsRestClient;
import edu.hm.cs.fs.restclient.typeadapter.DateTypeAdapter;
import edu.hm.cs.fs.restclient.typeadapter.GroupTypeAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Fabio
 */
public class TimetableModel extends CachedModel<Lesson> implements IModel {

    private static final String TIMETABLE_FILE = "timetable.json";
    private static final String TIMETABLE_CONFIG_FILE = "timetable-config.json";

    private static final String CHARSET = "UTF-8";
    private static final int DAYS_OF_WEEK = 7;
    private static final int BREAK_TIME_LENGTH_MINUTES = 15;

    private final Context mContext;
    private final Gson mGson;

    private final Map<String, LessonGroupSaver> mLessonGroupMapping = new HashMap<>();

    public TimetableModel(@NonNull final Context context) {
        mContext = context;
        mGson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }

    public void getTimetable(final boolean refresh,
                             @NonNull final ICallback<List<Lesson>> callback) {
        getData(refresh, callback);
    }

    public void getLessonsByGroup(@NonNull final Group group,
                                  @NonNull final ICallback<List<LessonGroup>> callback) {
        FsRestClient.getV1("http://192.168.178.92:8080")
                .getLessonGroups(group, new Callback<List<LessonGroup>>() {
                    @Override
                    public void success(List<LessonGroup> lessonGroups, Response response) {
                        callback.onSuccess(lessonGroups);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onError(ErrorFactory.http(error));
                    }
                });
    }

    public void setSelected(@NonNull final LessonGroup lessonGroup, boolean selected) {
        final String id = lessonGroup.getModule().getId() + lessonGroup.getTeacher().getId();
        if (!mLessonGroupMapping.containsKey(id)) {
            mLessonGroupMapping.put(id, new LessonGroupSaver(lessonGroup));
        }
        if (!selected) {
            mLessonGroupMapping.remove(id);
        }
    }

    public void setPkSelected(@NonNull final LessonGroup lessonGroup, final int pk, final boolean selected) {
        final String id = lessonGroup.getModule().getId() + lessonGroup.getTeacher().getId();
        if (selected) {
            mLessonGroupMapping.get(id).mSelectedPk = pk;
        } else {
            mLessonGroupMapping.get(id).mSelectedPk = -1;
        }
    }

    public boolean isPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        final String id = lessonGroup.getModule().getId() + lessonGroup.getTeacher().getId();
        return mLessonGroupMapping.get(id).mSelectedPk == pk;
    }

    public void getNextLesson(@NonNull final ICallback<Lesson> callback) {
        getData(false, new ICallback<List<Lesson>>() {
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
                    if (lesson.getTime().getStart().get(Calendar.HOUR_OF_DAY)
                            >= foundTime.getStart().get(Calendar.HOUR_OF_DAY)
                            && lesson.getTime().getStart().get(Calendar.MINUTE)
                            >= foundTime.getStart().get(Calendar.MINUTE)) {
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

    @Override
    protected boolean hasOfflineData() {
        return true;
    }

    @Override
    protected void updateOffline(@NonNull final ICallback<List<Lesson>> callback) {
        try {
            callback.onSuccess(readTimetable());
        } catch (IOException e) {
            callback.onError(ErrorFactory.exception(e));
        }
    }

    @Override
    protected void updateOnline(@NonNull final ICallback<List<Lesson>> callback) {
        new AsyncTask<Void, Void, List<Lesson>>() {
            @Override
            protected List<Lesson> doInBackground(Void... params) {
                try {
                    final List<LessonGroupSaver> lessonGroupSavers = readTimetableConfig();
                    final List<Lesson> timetable = new ArrayList<>();
                    for (LessonGroupSaver lessonGroupSaver : lessonGroupSavers) {
                        final LessonGroup lessonGroup = lessonGroupSaver.mLessonGroup;
                        final List<Lesson> lessons = FsRestClient.getV1("http://192.168.178.92:8080")
                                .getLessons(lessonGroup.getGroup(), lessonGroup.getModule().getId(),
                                        lessonGroup.getTeacher().getId(), lessonGroupSaver.mSelectedPk);
                        timetable.addAll(lessons);
                    }
                    writeTimetable(timetable);
                    return timetable;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    return readTimetable(); // Save the old timetable...
                } catch (IOException e) {
                    return new ArrayList<>(); // The last possible case if everything else failed
                }
            }

            @Override
            protected void onPostExecute(List<Lesson> lessons) {
                callback.onSuccess(lessons);
            }
        }.execute();
    }

    public void revert() {
        mLessonGroupMapping.clear();
        cleanUp();
        mContext.deleteFile(TIMETABLE_CONFIG_FILE);
        mContext.deleteFile(TIMETABLE_FILE);
    }

    public void save() {
        writeTimetableConfig(new ArrayList<>(mLessonGroupMapping.values()));
        updateOnline(new ICallback<List<Lesson>>() {
            @Override
            public void onSuccess(final List<Lesson> data) {
                mLessonGroupMapping.clear();
            }

            @Override
            public void onError(@NonNull final IError error) {

            }
        });
    }

    @NonNull
    private List<LessonGroupSaver> readTimetableConfig() throws IOException {
        return read(new TypeToken<List<LessonGroupSaver>>() {
        }, TIMETABLE_CONFIG_FILE);
    }

    private void writeTimetableConfig(@NonNull final List<LessonGroupSaver> data) {
        write(data, TIMETABLE_CONFIG_FILE);
    }

    @NonNull
    private List<Lesson> readTimetable() throws IOException {
        return read(new TypeToken<List<Lesson>>() {
        }, TIMETABLE_FILE);
    }

    private void writeTimetable(@NonNull final List<Lesson> lessons) {
        write(lessons, TIMETABLE_FILE);
    }

    private <T> List<T> read(@NonNull final TypeToken<List<T>> typeToken,
                             @NonNull final String fileName) {
        FileInputStream inputStream = null;
        try {
            inputStream = mContext.openFileInput(fileName);
            final byte[] buffer = new byte[inputStream.available()];
            final int read = inputStream.read(buffer);
            return mGson.fromJson(new String(
                    buffer,
                    0,
                    read,
                    Charset.forName(CHARSET)
            ), typeToken.getType());
        } catch (IOException e) {
            return new ArrayList<>();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private <T> void write(@NonNull final T data, @NonNull final String fileName) {
        FileOutputStream outputStream = null;
        try {
            outputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(mGson.toJson(data).getBytes(Charset.forName(CHARSET)));
            outputStream.flush();
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

    private static class LessonGroupSaver {
        private LessonGroup mLessonGroup;
        private int mSelectedPk;

        public LessonGroupSaver(LessonGroup lessonGroup) {
            mLessonGroup = lessonGroup;
        }
    }
}
