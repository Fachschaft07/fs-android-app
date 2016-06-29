package edu.hm.cs.fs.app.model;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.fk07.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * Created by Fabio Hellmann on 29.06.2016.
 */
public class EventWrapper {
    private static long idCounter = 0;
    private static final Map<String, Integer> colorMap = new HashMap<>();
    @ColorRes
    private static final int[] SUBJECT_COLORS = {
            R.color.subject_color_0,
            R.color.subject_color_1,
            R.color.subject_color_2,
            R.color.subject_color_3,
            R.color.subject_color_4,
            R.color.subject_color_5,
            R.color.subject_color_6,
            R.color.subject_color_7,
            R.color.subject_color_8,
            R.color.subject_color_9
    };
    private final long id;
    private final String name;
    private final String place;
    private final Calendar start;
    private final Calendar end;
    private final Type type;
    private final Object raw;
    @ColorRes
    private int color;

    private EventWrapper(String name, String place, Calendar start, Calendar end, Type type, Object raw) {
        this.id = idCounter++;
        this.name = name;
        this.place = place;
        this.start = start;
        this.end = end;
        this.type = type;
        this.raw = raw;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public Calendar getStart() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start.getTimeInMillis());
        return calendar;
    }

    public Calendar getEnd() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(end.getTimeInMillis());
        return calendar;
    }

    public Type getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof EventWrapper)) {
            return false;
        }
        final EventWrapper wrapper = (EventWrapper) o;
        return wrapper.id == id;
    }

    public Object getRaw() {
        return raw;
    }

    public enum Type {
        LESSON, EXAM, HOLIDAY
    }

    public static <T> List<EventWrapper> wrap(@NonNull final List<T> content) {
        final List<EventWrapper> result = new ArrayList<>();
        for (T data : content) {
            if (data instanceof Exam) {
                result.addAll(wrap((Exam) data));
            } else if (data instanceof Holiday) {
                result.addAll(wrap((Holiday) data));
            }
        }
        return result;
    }

    public static List<EventWrapper> wrap(@NonNull final List<Lesson> content, final int year, final int month) {
        final List<EventWrapper> result = new ArrayList<>();

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        final int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int dayOfMonth = 1; dayOfMonth <= maxDaysOfMonth; dayOfMonth++) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            for (Lesson lesson : content) {
                if(lesson.getDay().getCalendarId() == calendar.get(Calendar.DAY_OF_WEEK)) {
                    result.add(EventWrapper.wrap(lesson, year, month, dayOfMonth));
                }
            }
        }
        return result;
    }

    public static EventWrapper wrap(@NonNull final Lesson lesson, int year, int month, int day) {
        final Calendar start = Calendar.getInstance();
        start.set(year, month - 1, day, lesson.getHour(), lesson.getMinute(), 0);

        final Calendar end = Calendar.getInstance();
        end.setTimeInMillis(start.getTimeInMillis());
        end.add(Calendar.MINUTE, 90);

        final String name = lesson.getModule().getName();
        final String roomAndType = lesson.getRoom() + (TextUtils.isEmpty(lesson.getSuffix()) ? "" : " - " + lesson.getSuffix());

        // Get color for the subject
        final int color;
        if (colorMap.containsKey(lesson.getModule().getId())) {
            color = colorMap.get(lesson.getModule().getId());
        } else {
            int indexOf = colorMap.size();
            while (indexOf >= SUBJECT_COLORS.length) {
                indexOf -= SUBJECT_COLORS.length;
            }
            color = SUBJECT_COLORS[indexOf];
            colorMap.put(lesson.getModule().getId(), color);
        }

        final EventWrapper wrapper = new EventWrapper(name, roomAndType, start, end, Type.LESSON, lesson);
        wrapper.setColor(color);
        return wrapper;
    }

    public static List<EventWrapper> wrap(@NonNull final Exam exam) {
        final Calendar start = Calendar.getInstance();
        start.setTime(exam.getDate());

        final Calendar end = (Calendar) start.clone();
        switch (exam.getType()) {
            case WRITTEN_EXAMINATION_60:
            case WRITTEN_EXAMINATION_60_ESSAY:
                end.add(Calendar.MINUTE, 60);
                break;
            default:
                end.add(Calendar.MINUTE, 90);
        }

        // Create one string out of all rooms
        final StringBuilder rooms = new StringBuilder();
        for (int i = 0; i < exam.getRooms().size(); i++) {
            if (i > 0) {
                rooms.append(", ");
            }
            rooms.append(exam.getRooms().get(i));
        }

        final EventWrapper wrapper = new EventWrapper("Prüfung: " + exam.getModule().getName(), rooms.toString(), start, end, Type.EXAM, exam);
        wrapper.setColor(R.color.exam);
        return Collections.singletonList(wrapper);
    }

    public static List<EventWrapper> wrap(@NonNull final Holiday holiday) {
        final Calendar timeRangeStart = Calendar.getInstance();
        timeRangeStart.setTime(holiday.getStart());
        final Calendar timeRangeEnd = Calendar.getInstance();
        timeRangeEnd.setTime(holiday.getEnd());

        final List<EventWrapper> result = new ArrayList<>();

        final Calendar tempStart = Calendar.getInstance();
        tempStart.setTimeInMillis(timeRangeStart.getTimeInMillis());

        do {
            final Calendar start = (Calendar) tempStart.clone();
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            final Calendar end = (Calendar) tempStart.clone();
            end.set(Calendar.HOUR_OF_DAY, 23);
            end.set(Calendar.MINUTE, 59);
            final EventWrapper wrapper = new EventWrapper(holiday.getName(), "", start, end, Type.HOLIDAY, holiday);
            wrapper.setColor(R.color.holiday);
            result.add(wrapper);

            tempStart.add(Calendar.DATE, 1);
        } while (tempStart.get(Calendar.DATE) < timeRangeEnd.get(Calendar.DATE));

        return result;
    }

    @Nullable
    public static EventWrapper search(@NonNull final List<EventWrapper> wrappers, @NonNull final Long id) {
        for (EventWrapper wrapper : wrappers) {
            if (wrapper.getId() == id) {
                return wrapper;
            }
        }
        return null;
    }

    public static List<EventWrapper> filter(@NonNull final List<EventWrapper> wrappers, @NonNull final Type... types) {
        final List<EventWrapper> result = new ArrayList<>();
        final List<Type> filters = Arrays.asList(types);
        for (EventWrapper wrapper : wrappers) {
            if (filters.contains(wrapper.getType())) {
                result.add(wrapper);
            }
        }
        return result;
    }

    public static boolean existsOnSameDate(@NonNull final List<EventWrapper> wrappers, @NonNull final EventWrapper wrapper) {
        boolean exists = false;
        for (EventWrapper event : wrappers) {
            if (event.getStart().get(Calendar.DATE) == wrapper.getStart().get(Calendar.DATE)) {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public static void resetCounter() {
        idCounter = 0;
    }
}
