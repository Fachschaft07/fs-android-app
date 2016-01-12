package edu.hm.cs.fs.app.database.cloud;

import android.support.annotation.NonNull;

import java.util.Calendar;

import javax.inject.Inject;

import edu.hm.cs.fs.app.domain.IDataService;
import edu.hm.cs.fs.app.util.DateUtils;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.PublicTransportLocation;
import edu.hm.cs.fs.common.constant.RoomType;
import edu.hm.cs.fs.common.constant.StudentWorkMunich;
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
import edu.hm.cs.fs.restclient.FsRestClient;
import edu.hm.cs.fs.restclient.RestClientV1;
import rx.Observable;

/**
 * @author Fabio
 */
public class RestApiService implements IDataService {
    private final RestClientV1 mRestClient;

    @Inject
    public RestApiService() {
        mRestClient = FsRestClient.getV1("http://10.128.12.158:8080");
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntries(@NonNull final Boolean refresh) {
        return mRestClient.getEntries();
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSinceYesterday() {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);
        return blackboardEntriesSince(yesterday.getTimeInMillis());
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSince(@NonNull final Long timestamp) {
        return mRestClient.getEntries("", null, timestamp, 0);
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntrieById(@NonNull final String id) {
        return mRestClient.getEntries().filter(blackboardEntry -> blackboardEntry.getId().equals(id));
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull final String search) {
        return mRestClient.getEntries(search);
    }

    @Override
    public Observable<Holiday> holidays() {
        return mRestClient.getHolidays();
    }

    @Override
    public Observable<Holiday> nextHolidays() {
        return mRestClient.getHolidays().toSortedList((lhs, rhs) -> {
            return lhs.getStart().compareTo(rhs.getStart());
        }).flatMap(Observable::from).first();
    }

    @Override
    public Observable<Exam> exams(@NonNull final Boolean refresh) {
        return mRestClient.getExams();
    }

    @Override
    public Observable<Exam> examsOfUser() {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Void> pinExam(@NonNull Exam exam) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Void> unpinExam(@NonNull Exam exam) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Presence> fsPresence() {
        return mRestClient.getPresence();
    }

    @Override
    public Observable<News> fsNews(@NonNull final Boolean refresh) {
        return mRestClient.getNews();
    }

    @Override
    public Observable<News> fsNewsByTitle(@NonNull final String title) {
        return mRestClient.getNews().filter(news -> news.getTitle().equalsIgnoreCase(title));
    }

    @Override
    public Observable<News> fsNewsTop3() {
        return mRestClient.getNews().limit(3);
    }

    @Override
    public Observable<SimpleJob> jobs(@NonNull final Boolean refresh) {
        return mRestClient.getJobs();
    }

    @Override
    public Observable<SimpleJob> jobByTitle(@NonNull final String title) {
        return mRestClient.getJobs(title);
    }

    @Override
    public Observable<LostFound> lostfound() {
        return mRestClient.getLostAndFound();
    }

    @Override
    public Observable<Meal> meals(@NonNull final Boolean refresh) {
        return mRestClient.getMeals(StudentWorkMunich.MENSA_LOTHSTRASSE);
    }

    @Override
    public Observable<Meal> mealsOfToday() {
        return meals(false).filter(meal -> DateUtils.isToday(meal.getDate()));
    }

    @Override
    public Observable<Module> moduleById(@NonNull final String id) {
        return mRestClient.getModuleById(id);
    }

    @Override
    public Observable<PublicTransport> publicTransportPasing() {
        return mRestClient.getPublicTransports(PublicTransportLocation.PASING);
    }

    @Override
    public Observable<PublicTransport> publicTransportLothstrasse() {
        return mRestClient.getPublicTransports(PublicTransportLocation.LOTHSTR);
    }

    @Override
    public Observable<SimpleRoom> freeRooms(@NonNull final Day day,
                                            @NonNull final Time time) {
        return mRestClient.getRoomByDateTime(
                RoomType.ALL,
                day,
                time.getStart().get(Calendar.HOUR_OF_DAY),
                time.getStart().get(Calendar.MINUTE)
        );
    }

    @Override
    public Observable<Lesson> timetable(@NonNull final Boolean refresh) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull final Group group) {
        return mRestClient.getLessonGroups(group);
    }

    @Override
    public Observable<Lesson> nextLesson() {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Void> save(@NonNull final LessonGroup lessonGroup,
                                 @NonNull final Boolean selected) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Void> save(@NonNull final LessonGroup lessonGroup,
                                 @NonNull final Integer pk,
                                 @NonNull final Boolean selected) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Boolean> isPkSelected(@NonNull LessonGroup lessonGroup, @NonNull Integer pk) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Boolean> isModuleSelected(@NonNull LessonGroup lessonGroup) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Void> resetTimetable() {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }
}
