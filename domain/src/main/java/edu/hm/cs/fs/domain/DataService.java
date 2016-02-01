package edu.hm.cs.fs.domain;

import android.support.annotation.NonNull;

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
import edu.hm.cs.fs.domain.cloud.RestApiService;
import edu.hm.cs.fs.domain.disk.DiskService;
import edu.hm.cs.fs.domain.memory.MemoryService;
import rx.Observable;

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
        return createObservableStream(BlackboardEntry.class,
                mMemoryCacheService.blackboardEntries(refresh),
                mDiskCacheService.blackboardEntries(refresh),
                mCloudCacheService.blackboardEntries(refresh))
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSince(@NonNull Boolean refresh, @NonNull Long timestamp) {
        return createObservableStream(BlackboardEntry.class,
                mMemoryCacheService.blackboardEntriesSince(refresh, timestamp),
                mDiskCacheService.blackboardEntriesSince(refresh, timestamp),
                mCloudCacheService.blackboardEntriesSince(refresh, timestamp))
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull Boolean refresh, @NonNull String search) {
        return createObservableStream(BlackboardEntry.class,
                mMemoryCacheService.blackboardEntriesBySearchString(refresh, search),
                mDiskCacheService.blackboardEntriesBySearchString(refresh, search),
                mCloudCacheService.blackboardEntriesBySearchString(refresh, search))
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Holiday> holidays(@NonNull Boolean refresh) {
        return createObservableStream(Holiday.class,
                mMemoryCacheService.holidays(refresh),
                mDiskCacheService.holidays(refresh),
                mCloudCacheService.holidays(refresh))
                .filter(holiday -> holiday.getStart().getTime() >= System.currentTimeMillis())
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Exam> exams(@NonNull Boolean refresh) {
        return createObservableStream(Exam.class,
                mMemoryCacheService.exams(refresh),
                mDiskCacheService.exams(refresh),
                mCloudCacheService.exams(refresh))
                .filter(exam -> exam.getDate().getTime() >= System.currentTimeMillis())
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Exam> examsOfUser() {
        return mDiskCacheService.examsOfUser()
                .filter(exam -> exam.getDate().getTime() >= System.currentTimeMillis())
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Exam> examsByTimetable() {
        return createObservableStream(Exam.class,
                mMemoryCacheService.examsByTimetable(),
                mDiskCacheService.examsByTimetable(),
                mCloudCacheService.examsByTimetable())
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Boolean> isExamPined(@NonNull Exam exam) {
        return mDiskCacheService.isExamPined(exam)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Boolean> pinExam(@NonNull Exam exam) {
        return mDiskCacheService.pinExam(exam)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Presence> fsPresence() {
        return mCloudCacheService.fsPresence()
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<News> fsNews(@NonNull Boolean refresh) {
        return createObservableStream(News.class,
                mMemoryCacheService.fsNews(refresh),
                mDiskCacheService.fsNews(refresh),
                mCloudCacheService.fsNews(refresh))
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<SimpleJob> jobs(@NonNull Boolean refresh) {
        return createObservableStream(SimpleJob.class,
                mMemoryCacheService.jobs(refresh),
                mDiskCacheService.jobs(refresh),
                mCloudCacheService.jobs(refresh))
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<SimpleJob> jobByTitle(@NonNull Boolean refresh, @NonNull String title) {
        return createObservableStream(SimpleJob.class,
                mMemoryCacheService.jobByTitle(refresh, title),
                mDiskCacheService.jobByTitle(refresh, title),
                mCloudCacheService.jobByTitle(refresh, title))
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<LostFound> lostfound(@NonNull Boolean refresh) {
        return createObservableStream(LostFound.class,
                mMemoryCacheService.lostfound(refresh),
                mDiskCacheService.lostfound(refresh),
                mCloudCacheService.lostfound(refresh))
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Meal> meals(@NonNull Boolean refresh) {
        return createObservableStream(Meal.class,
                mMemoryCacheService.meals(refresh),
                mDiskCacheService.meals(refresh),
                mCloudCacheService.meals(refresh))
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
                .toSortedList((r1, r2) -> r1.getName().compareTo(r2.getName()))
                .flatMap(Observable::from)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Lesson> timetable(@NonNull Boolean refresh) {
        return createObservableStream(Lesson.class,
                mMemoryCacheService.timetable(refresh),
                mDiskCacheService.timetable(refresh),
                mCloudCacheService.timetable(refresh)
        ).compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull Group group) {
        return mCloudCacheService.lessonsByGroup(group)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Boolean selected) {
        return mDiskCacheService.save(lessonGroup, selected)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Integer pk, @NonNull Boolean selected) {
        return mDiskCacheService.save(lessonGroup, pk, selected)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Boolean> isPkSelected(@NonNull LessonGroup lessonGroup, @NonNull Integer pk) {
        return mDiskCacheService.isPkSelected(lessonGroup, pk)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Boolean> isModuleSelected(@NonNull LessonGroup lessonGroup) {
        return mDiskCacheService.isModuleSelected(lessonGroup)
                .compose(mSchedulerProvider.applySchedulers());
    }

    @Override
    public Observable<Void> resetTimetable() {
        return mDiskCacheService.resetTimetable()
                .compose(mSchedulerProvider.applySchedulers());
    }

    private <T> Observable<T> createObservableStream(@NonNull final Class<T> type,
                                                     @NonNull final Observable<T> memory,
                                                     @NonNull final Observable<T> disk,
                                                     @NonNull final Observable<T> cloud) {
        return Observable.amb(
                // Try to get data from the memory - the fastest solution
                memory,
                // If the memory has no data, try the disk - the fast solution
                disk
                        .doOnSubscribe(() -> mMemoryCacheService.cleanCache(type))
                        .doOnNext(mMemoryCacheService::addToCache),
                // If nothing helps, ask the cloud for data - the slowest solution
                cloud
                        .doOnSubscribe(() -> {
                            mMemoryCacheService.cleanCache(type);
                            mDiskCacheService.cleanCache(type);
                        })
                        .doOnNext(mDiskCacheService::addToCache)
                        .doOnNext(mMemoryCacheService::addToCache)
        );
    }
}
