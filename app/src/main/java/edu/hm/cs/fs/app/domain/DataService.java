package edu.hm.cs.fs.app.domain;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.cloud.RestApiService;
import edu.hm.cs.fs.app.database.disk.DiskService;
import edu.hm.cs.fs.app.database.memory.MemoryService;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.LessonGroup;
import edu.hm.cs.fs.common.model.LostFound;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.common.model.News;
import edu.hm.cs.fs.common.model.Presence;
import edu.hm.cs.fs.common.model.PublicTransport;
import edu.hm.cs.fs.common.model.simple.SimpleJob;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;
import rx.Observable;

/**
 * @author Fabio
 */
public class DataService implements IDataService {
    @NonNull
    private final SchedulerProvider mSchedulerProvider;
    @NonNull
    private final MemoryService mMemoryCacheService;
    @NonNull
    private final DiskService mDiskCacheService;
    @NonNull
    private final RestApiService mCloudCacheService;

    public DataService(@NonNull final SchedulerProvider schedulerProvider,
                       @NonNull final MemoryService memoryCacheService,
                       @NonNull final DiskService diskCacheService,
                       @NonNull final RestApiService cloudCacheService) {
        mSchedulerProvider = schedulerProvider;
        mMemoryCacheService = memoryCacheService;
        mDiskCacheService = diskCacheService;
        mCloudCacheService = cloudCacheService;
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntries(@NonNull Boolean refresh) {
        return mCloudCacheService.blackboardEntries(refresh)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSinceYesterday() {
        return mCloudCacheService.blackboardEntriesSinceYesterday()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSince(@NonNull Long timestamp) {
        return mCloudCacheService.blackboardEntriesSince(timestamp)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntrieById(@NonNull String id) {
        return mCloudCacheService.blackboardEntrieById(id)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull String search) {
        return mCloudCacheService.blackboardEntriesBySearchString(search)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Holiday> holidays() {
        return mCloudCacheService.holidays()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Holiday> nextHolidays() {
        return mCloudCacheService.nextHolidays()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Exam> exams(@NonNull Boolean refresh) {
        return mCloudCacheService.exams(refresh)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Exam> examsOfUser() {
        return mCloudCacheService.examsOfUser()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Void> pinExam(@NonNull Exam exam) {
        return mCloudCacheService.pinExam(exam)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Void> unpinExam(@NonNull Exam exam) {
        return mCloudCacheService.unpinExam(exam)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Presence> fsPresence() {
        return mCloudCacheService.fsPresence()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<News> fsNews(@NonNull Boolean refresh) {
        return mCloudCacheService.fsNews(refresh)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<News> fsNewsByTitle(@NonNull String title) {
        return mCloudCacheService.fsNewsByTitle(title)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<News> fsNewsTop3() {
        return mCloudCacheService.fsNewsTop3()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<SimpleJob> jobs(@NonNull Boolean refresh) {
        return mCloudCacheService.jobs(refresh)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<SimpleJob> jobByTitle(@NonNull String title) {
        return mCloudCacheService.jobByTitle(title)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<LostFound> lostfound() {
        return mCloudCacheService.lostfound()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Meal> meals(@NonNull Boolean refresh) {
        return mCloudCacheService.meals(refresh)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Meal> mealsOfToday() {
        return mCloudCacheService.mealsOfToday()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Module> moduleById(@NonNull String id) {
        return mCloudCacheService.moduleById(id)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<PublicTransport> publicTransportPasing() {
        return mCloudCacheService.publicTransportPasing()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<PublicTransport> publicTransportLothstrasse() {
        return mCloudCacheService.publicTransportLothstrasse()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<SimpleRoom> freeRooms(@NonNull Day day, @NonNull Time time) {
        return mCloudCacheService.freeRooms(day, time)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Lesson> timetable(@NonNull Boolean refresh) {
        return mCloudCacheService.timetable(refresh)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull Group group) {
        return mCloudCacheService.lessonsByGroup(group)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Lesson> nextLesson() {
        return mCloudCacheService.nextLesson()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Boolean selected) {
        return null;
    }

    @Override
    public Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Integer pk, @NonNull Boolean selected) {
        return null;
    }

    @Override
    public Observable<Boolean> isPkSelected(@NonNull LessonGroup lessonGroup, @NonNull Integer pk) {
        return null;
    }

    @Override
    public Observable<Boolean> isModuleSelected(@NonNull LessonGroup lessonGroup) {
        return null;
    }

    @Override
    public Observable<Void> resetTimetable() {
        return null;
    }
}
