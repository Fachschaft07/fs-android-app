package edu.hm.cs.fs.domain;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.LostFound;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.News;
import edu.hm.cs.fs.common.model.Presence;
import edu.hm.cs.fs.common.model.simple.SimpleJob;
import rx.Observable;

public abstract class AbstractCacheService extends AbstractService {

    public abstract <T> Observable<T> addCache(@NonNull final T item);

    public abstract <T> Observable<Void> cleanCache(@NonNull final Class<T> classType);

    public abstract <T> List<T> getCache(@NonNull final Class<T> classType);

    @Override
    public Observable<BlackboardEntry> blackboardEntries(@NonNull Boolean refresh) {
        final List<BlackboardEntry> cache = getCache(BlackboardEntry.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<Holiday> holidays(@NonNull Boolean refresh) {
        final List<Holiday> cache = getCache(Holiday.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<Exam> exams(@NonNull Boolean refresh) {
        final List<Exam> cache = getCache(Exam.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<Presence> fsPresence() {
        return Observable.never();
    }

    @Override
    public Observable<News> fsNews(@NonNull Boolean refresh) {
        final List<News> cache = getCache(News.class);
        if(refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<SimpleJob> jobs(@NonNull Boolean refresh) {
        final List<SimpleJob> cache = getCache(SimpleJob.class);
        if(refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<LostFound> lostfound(@NonNull Boolean refresh) {
        final List<LostFound> cache = getCache(LostFound.class);
        if(refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<Meal> meals(@NonNull Boolean refresh) {
        final List<Meal> cache = getCache(Meal.class);
        if(refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<Exam> examsByTimetable() {
        // TODO Find the right algorithm!?
        /*
        timetable(false)
                .groupBy(lesson -> lesson.getModule().getId())
                .flatMap(lessonGroup -> lessonGroup.limit(1).flatMap(lessonGroup -> exams(false)
                        .filter(exam -> exam.getModule().getId().equals(lessonGroup.getKey()))
                        .filter(exam -> exam.getStudy() == lessonGroup.getTeacher().getId()))
                .filter(stringLessonGroupedObservable -> stringLessonGroupedObservable.filter(lesson1 -> isExamPined(lesson1)));
                */
        return Observable.never();
    }

    @Override
    public Observable<Lesson> timetable(@NonNull Boolean refresh) {
        final List<Lesson> cache = getCache(Lesson.class);
        if(refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    @Override
    public Observable<Void> resetTimetable() {
        cleanCache(Lesson.class);
        return Observable.never();
    }
}
