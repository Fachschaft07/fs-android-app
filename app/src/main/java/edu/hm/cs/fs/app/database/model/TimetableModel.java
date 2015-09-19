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
import java.util.List;

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
public class TimetableModel implements IModel {

    private static final String TIMETABLE_FILE = "timetable.json";
    private static final String TIMETABLE_CONFIG_FILE = "timetable-config.json";

    private static final String CHARSET = "UTF-8";
    private static final int DAYS_OF_WEEK = 7;
    private static final int BREAK_TIME_LENGTH_MINUTES = 15;

    private final Context mContext;
    private final Gson mGson;

    public TimetableModel(@NonNull final Context context) {
        mContext = context;
        mGson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }

    public void getTimetable(final boolean refresh,
                             @NonNull final ICallback<List<Lesson>> callback) {
        try {
            if(isTimetableUpToDate() && !refresh) {
                callback.onSuccess(readTimetable());
            } else {
                updateTimetable(callback);
            }
        } catch (IOException e) {
            callback.onError(ErrorFactory.exception(e));
        }
    }

    private boolean isTimetableUpToDate() {
        return mContext.getFileStreamPath(TIMETABLE_FILE).lastModified()
                > mContext.getFileStreamPath(TIMETABLE_CONFIG_FILE).lastModified();
    }

    private void updateTimetable(@NonNull final ICallback<List<Lesson>> callback) {
        new AsyncTask<Void, Void, List<Lesson>>() {
            @Override
            protected List<Lesson> doInBackground(Void... params) {
                try {
                    final List<LessonGroupSaver> config = readTimetableConfig();
                    final List<Lesson> timetable = new ArrayList<>();
                    for (LessonGroupSaver lessonGroupSaver : config) {
                        final LessonGroup lessonGroup = lessonGroupSaver.mLessonGroup;
                        final List<Lesson> lessons = FsRestClient.getV1()
                                .getLessons(lessonGroup.getGroup(), lessonGroup.getModule().getId(),
                                        lessonGroup.getTeacher().getId(), lessonGroupSaver.mSelectedPk);
                        timetable.addAll(lessons);
                    }
                    writeTimetable(timetable);
                    return timetable;
                } catch (IOException e) {
                    callback.onError(ErrorFactory.exception(e));
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<Lesson> lessons) {
                if(lessons != null) {
                    callback.onSuccess(lessons);
                }
            }
        }.execute();
    }

    public void getLessonsByGroup(@NonNull final Group group,
                                  @NonNull final ICallback<List<LessonGroup>> callback) {
        FsRestClient.getV1()
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

    public void getNextLesson(@NonNull final ICallback<Lesson> callback) {
        getTimetable(false, new ICallback<List<Lesson>>() {
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

    public void save(@NonNull final LessonGroup lessonGroup, final boolean selected) {
        save(lessonGroup, 0, selected);
    }

    public void save(@NonNull final LessonGroup lessonGroup, final int pk, final boolean selected) {
        try {
            final List<LessonGroupSaver> lessonGroupSavers = readTimetableConfig();
            if(selected) {
                lessonGroupSavers.add(new LessonGroupSaver(lessonGroup, pk));
            } else {
                LessonGroupSaver saverToDelete = null;
                for(LessonGroupSaver saver : lessonGroupSavers) {
                    if(getLessonGroupId(saver.mLessonGroup).equals(getLessonGroupId(lessonGroup))) {
                        saverToDelete = saver;
                        break;
                    }
                }
                if(saverToDelete != null) {
                    lessonGroupSavers.remove(saverToDelete);
                }
            }
            writeTimetableConfig(lessonGroupSavers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLessonGroupId(@NonNull final LessonGroup lessonGroup) {
        return lessonGroup.getModule().getId() + lessonGroup.getTeacher().getId();
    }

    public void resetConfiguration() {
        mContext.deleteFile(TIMETABLE_CONFIG_FILE);
        mContext.deleteFile(TIMETABLE_FILE);
    }

    public boolean isSelected(@NonNull final LessonGroup lessonGroup) {
        return isPkSelected(lessonGroup, 0);
    }

    public boolean isPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        try {
            final List<LessonGroupSaver> lessonGroupSavers = readTimetableConfig();
            for (LessonGroupSaver saver : lessonGroupSavers) {
                if(getLessonGroupId(saver.mLessonGroup).equals(getLessonGroupId(lessonGroup))
                        && saver.mSelectedPk == pk) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    public static class LessonGroupSaver {
        private LessonGroup mLessonGroup;
        private int mSelectedPk;

        public LessonGroupSaver(LessonGroup lessonGroup, int pk) {
            mLessonGroup = lessonGroup;
            mSelectedPk = pk;
        }
    }
}
