package edu.hm.cs.fs.app.database;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.LessonGroup;
import edu.hm.cs.fs.restclient.RestClient;
import edu.hm.cs.fs.restclient.typeadapter.DateTypeAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Fabio
 */
public class TimetableModel implements IModel {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    private static final String TIMETABLE_FILE = "timetable.json";
    private static final String TIMETABLE_CONFIG_FILE = "timetable-config.json";

    private static final String CHARSET = "UTF-8";

    private final Context mContext;
    private final Gson mGson;

    public TimetableModel(@NonNull final Context context) {
        mContext = context;
        mGson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }

    /**
     * @param callback
     */
    public void getHolidays(@NonNull final ICallback<List<Holiday>> callback) {
        REST_CLIENT.getHolidays().enqueue(new Callback<List<Holiday>>() {
            @Override
            public void onResponse(Call<List<Holiday>> call, Response<List<Holiday>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Holiday>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getTimetable(final boolean refresh,
                             @NonNull final ICallback<List<Lesson>> callback) {
        try {
            if (isTimetableUpToDate() && !refresh) {
                callback.onSuccess(readTimetable());
            } else {
                updateTimetable(callback);
            }
        } catch (IOException e) {
            //e.printStackTrace();
            callback.onError(e);
        }
    }

    private boolean isTimetableUpToDate() {
        return mContext.getFileStreamPath(TIMETABLE_FILE).lastModified()
                > mContext.getFileStreamPath(TIMETABLE_CONFIG_FILE).lastModified();
    }

    private void updateTimetable(@NonNull final ICallback<List<Lesson>> callback) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                try {
                    final List<LessonGroupSaver> config = readTimetableConfig();
                    final List<Lesson> timetable = new ArrayList<>();
                    for (LessonGroupSaver lessonGroupSaver : config) {
                        final LessonGroup lessonGroup = lessonGroupSaver.mLessonGroup;

                        final Group group = lessonGroup.getGroup();
                        final String moduleId = lessonGroup.getModule().getId();
                        final String teacherId = lessonGroup.getTeacher() != null
                                ? lessonGroup.getTeacher().getId() : "";

                        if (lessonGroupSaver.mLessonGroup.getGroups().isEmpty()) {
                            final Response<List<Lesson>> response = REST_CLIENT
                                    .getLessons(group, moduleId, teacherId).execute();
                            if (response.isSuccessful()) {
                                timetable.addAll(response.body());
                            }
                        } else {
                            final Response<List<Lesson>> response = REST_CLIENT
                                    .getLessons(group, moduleId, teacherId,
                                            lessonGroupSaver.mSelectedPk).execute();
                            if (response.isSuccessful()) {
                                timetable.addAll(response.body());
                            }
                        }
                    }
                    writeTimetable(timetable);
                    return timetable;
                } catch (Exception e) {
                    return e;
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void onPostExecute(Object data) {
                if (data instanceof List) {
                    callback.onSuccess((List<Lesson>) data);
                } else {
                    final Exception exception = (Exception) data;
                    //exception.printStackTrace();
                    callback.onError(exception);
                }
            }
        }.execute();
    }

    public void getLessonsByGroup(@NonNull final Group group,
                                  @NonNull final ICallback<List<LessonGroup>> callback) {
        REST_CLIENT
                .getLessonGroups(group).enqueue(new Callback<List<LessonGroup>>() {
            @Override
            public void onResponse(Call<List<LessonGroup>> call, Response<List<LessonGroup>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<LessonGroup>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getNextLesson(@NonNull final ICallback<Lesson> callback) {
        getTimetable(false, new ICallback<List<Lesson>>() {
            @Override
            public void onSuccess(@NonNull List<Lesson> data) {
                if (data.isEmpty()) {
                    callback.onSuccess(null);
                } else {
                    Collections.sort(data, new Comparator<Lesson>() {
                        @Override
                        public int compare(Lesson lhs, Lesson rhs) {
                            return toCalendar(lhs).compareTo(toCalendar(rhs));
                        }

                        private Calendar toCalendar(Lesson lesson) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY, lesson.getHour());
                            calendar.set(Calendar.MINUTE, lesson.getMinute());

                            if (lesson.getDay().getCalendarId() == calendar.get(Calendar.DAY_OF_WEEK)) {
                                final Calendar temp = Calendar.getInstance();
                                temp.add(Calendar.MINUTE, -75);
                                if (temp.after(calendar)) {
                                    calendar.add(Calendar.DATE, 7); // next week
                                }
                            } else {
                                do {
                                    calendar.add(Calendar.DATE, 1);
                                }
                                while (lesson.getDay().getCalendarId() != calendar.get(Calendar.DAY_OF_WEEK));
                            }

                            return calendar;
                        }
                    });
                    callback.onSuccess(data.get(0));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                callback.onError(e);
            }
        });
    }

    public void save(@NonNull final LessonGroup lessonGroup, final boolean selected) {
        save(lessonGroup, -1, selected);
    }

    public void save(@NonNull final LessonGroup lessonGroup, final int pk, final boolean selected) {
        try {
            final List<LessonGroupSaver> lessonGroupSavers = readTimetableConfig();
            boolean changed = false;
            if (selected) {
                final LessonGroupSaver saver = getToUpdate(lessonGroupSavers, lessonGroup);
                if (saver == null) {
                    lessonGroupSavers.add(new LessonGroupSaver(lessonGroup, pk));
                    changed = true;
                } else if (pk != -1) {
                    saver.mSelectedPk = pk;
                    changed = true;
                }
            } else {
                LessonGroupSaver saverToDelete = null;
                for (LessonGroupSaver saver : lessonGroupSavers) {
                    if (getLessonGroupId(saver.mLessonGroup).equals(getLessonGroupId(lessonGroup))) {
                        saverToDelete = saver;
                        break;
                    }
                }
                if (saverToDelete != null) {
                    lessonGroupSavers.remove(saverToDelete);
                    changed = true;
                }
            }

            if (changed) { // Only update, if something changed
                writeTimetableConfig(lessonGroupSavers);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LessonGroupSaver getToUpdate(final List<LessonGroupSaver> lessonGroupSavers,
                                         final LessonGroup lessonGroup) {
        for (LessonGroupSaver saver : lessonGroupSavers) {
            if (getLessonGroupId(saver.mLessonGroup).equals(getLessonGroupId(lessonGroup))) {
                return saver;
            }
        }
        return null;
    }

    private String getLessonGroupId(@NonNull final LessonGroup lessonGroup) {
        StringBuilder lessonGroupId = new StringBuilder();
        if (lessonGroup.getGroup() != null) {
            lessonGroupId.append(lessonGroup.getGroup().toString());
        }
        if (lessonGroup.getModule() != null) {
            lessonGroupId.append(lessonGroup.getModule().getId());
        }
        if (lessonGroup.getTeacher() != null) {
            lessonGroupId.append(lessonGroup.getTeacher().getId());
        }
        return lessonGroupId.toString();
    }

    public void resetConfiguration() {
        mContext.deleteFile(TIMETABLE_CONFIG_FILE);
        mContext.deleteFile(TIMETABLE_FILE);
    }

    public boolean isModuleSelected(@NonNull final LessonGroup lessonGroup) {
        try {
            final List<LessonGroupSaver> lessonGroupSavers = readTimetableConfig();
            for (LessonGroupSaver saver : lessonGroupSavers) {
                if (getLessonGroupId(saver.mLessonGroup).equals(getLessonGroupId(lessonGroup))) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPkSelected(@NonNull final LessonGroup lessonGroup, final int pk) {
        try {
            final List<LessonGroupSaver> lessonGroupSavers = readTimetableConfig();
            for (LessonGroupSaver saver : lessonGroupSavers) {
                if (getLessonGroupId(saver.mLessonGroup).equals(getLessonGroupId(lessonGroup))
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
            final String json = new String(
                    buffer,
                    0,
                    read,
                    Charset.forName(CHARSET)
            );
            return mGson.fromJson(json, typeToken.getType());
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

    private static class GroupTypeAdapter extends TypeAdapter<Group> {
        @Override
        public void write(final JsonWriter out, final Group value) throws IOException {
            out.value(value.toString());
        }

        @Override
        public Group read(final JsonReader in) throws IOException {
            return Group.of(in.nextString());
        }
    }

    public static class LessonGroupSaver {
        private LessonGroup mLessonGroup;
        private int mSelectedPk;
        private boolean manuelAdded;

        public LessonGroupSaver(LessonGroup lessonGroup, int pk) {
            mLessonGroup = lessonGroup;
            mSelectedPk = pk;
        }
    }
}
