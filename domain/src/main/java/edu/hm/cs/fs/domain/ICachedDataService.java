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

/**
 * The cached data service automatically loads requested items from the cache if
 * they were available. Otherwise nothing/emptiness will be returned.
 *
 * @author Fabio
 */
public interface ICachedDataService extends IDataService {

    /**
     * Add an item to the cache.
     *
     * @param item to add to.
     * @param <T>  is the generic type of the item.
     * @return the item inside an Observable.
     */
    <T> Observable<T> addToCache(@NonNull final T item);

    /**
     * Remove all items of one type from the cache.
     *
     * @param classType to remove.
     * @param <T>       is the generic type of the items.
     * @return an Observable.
     */
    <T> Observable<Void> cleanCache(@NonNull final Class<T> classType);

    /**
     * Get a list with items, or an empty one, from the cache.
     *
     * @param classType of the items to get.
     * @param <T>       is the generic type of the items.
     * @return a list with all the found items inside.
     */
    <T> List<T> getFromCache(@NonNull final Class<T> classType);

    default Observable<BlackboardEntry> blackboardEntries(@NonNull Boolean refresh) {
        final List<BlackboardEntry> cache = getFromCache(BlackboardEntry.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<Holiday> holidays(@NonNull Boolean refresh) {
        final List<Holiday> cache = getFromCache(Holiday.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<Exam> exams(@NonNull Boolean refresh) {
        final List<Exam> cache = getFromCache(Exam.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<Presence> fsPresence() {
        return Observable.never();
    }

    default Observable<News> fsNews(@NonNull Boolean refresh) {
        final List<News> cache = getFromCache(News.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<SimpleJob> jobs(@NonNull Boolean refresh) {
        final List<SimpleJob> cache = getFromCache(SimpleJob.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<LostFound> lostfound(@NonNull Boolean refresh) {
        final List<LostFound> cache = getFromCache(LostFound.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<Meal> meals(@NonNull Boolean refresh) {
        final List<Meal> cache = getFromCache(Meal.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<Lesson> timetable(@NonNull Boolean refresh) {
        final List<Lesson> cache = getFromCache(Lesson.class);
        if (refresh || cache.isEmpty()) {
            return Observable.never();
        }
        return Observable.from(cache);
    }

    default Observable<Void> resetTimetable() {
        cleanCache(Lesson.class);
        return Observable.never();
    }
}
