package edu.hm.cs.fs.domain;

import android.support.annotation.NonNull;

import java.util.Calendar;

import edu.hm.cs.fs.common.constant.Study;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.News;
import edu.hm.cs.fs.common.model.simple.SimpleJob;
import edu.hm.cs.fs.domain.util.DateUtils;
import rx.Observable;

public abstract class AbstractService implements IDataService {

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSinceYesterday(@NonNull Boolean refresh) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);
        return blackboardEntriesSince(refresh, yesterday.getTimeInMillis());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSince(@NonNull Boolean refresh, @NonNull Long timestamp) {
        return blackboardEntries(refresh)
                .filter(item -> item.getPublish().getTime() >= timestamp);
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntrieById(@NonNull Boolean refresh, @NonNull final String id) {
        return blackboardEntries(refresh)
                .filter(blackboardEntry -> blackboardEntry.getId().equals(id))
                .first();
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull Boolean refresh, @NonNull String search) {
        return blackboardEntries(refresh)
                .filter(item -> item.getSubject().contains(search)
                        || item.getText().contains(search));
    }

    @Override
    public Observable<Holiday> nextHolidays(@NonNull Boolean refresh) {
        return holidays(refresh).toSortedList((lhs, rhs) -> {
            return lhs.getStart().compareTo(rhs.getStart());
        }).flatMap(Observable::from).first();
    }

    @Override
    public Observable<News> fsNewsByTitle(@NonNull Boolean refresh, @NonNull final String title) {
        return fsNews(refresh).filter(news -> news.getTitle().equalsIgnoreCase(title)).first();
    }

    @Override
    public Observable<News> fsNewsTop3(@NonNull Boolean refresh) {
        return fsNews(refresh).limit(3);
    }

    @Override
    public Observable<SimpleJob> jobByTitle(@NonNull Boolean refresh, @NonNull String title) {
        return jobs(refresh).filter(item -> item.getTitle().equalsIgnoreCase(title)).first();
    }

    @Override
    public Observable<Meal> mealsOfToday(@NonNull Boolean refresh) {
        return meals(refresh).filter(meal -> DateUtils.isToday(meal.getDate()));
    }

    @Override
    public Observable<Exam> exams(@NonNull Boolean refresh, @NonNull String search) {
        return exams(refresh).filter(exam -> exam.getStudy().toString().equalsIgnoreCase(search)
                || Study.of(search) == null && exam.getModule().getName().contains(search)
                || exam.getCode().equalsIgnoreCase(search));
    }

    @Override
    public Observable<Lesson> nextLesson() {
        return timetable(false)
                .toSortedList((lhs, rhs) -> toCalendar(lhs).compareTo(toCalendar(rhs)))
                .flatMap(Observable::from)
                .first();
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
}
