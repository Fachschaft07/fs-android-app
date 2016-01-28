package edu.hm.cs.fs.domain.cloud;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

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
import edu.hm.cs.fs.domain.AbstractService;
import edu.hm.cs.fs.domain.helper.GroupTypeAdapter;
import edu.hm.cs.fs.domain.helper.LessonGroupSaver;
import edu.hm.cs.fs.restclient.RestClient;
import edu.hm.cs.fs.restclient.typeadapter.DateTypeAdapter;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import rx.Observable;

/**
 * @author Fabio
 */
public class RestApiService extends AbstractService {
    @NonNull
    private final SharedPreferences mPrefs;
    private final Gson mGson;
    private final RestClient mRestClient;

    @Inject
    public RestApiService(@NonNull final SharedPreferences prefs) {
        mPrefs = prefs;
        mGson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        mRestClient = new RestClient.Builder()
                .setErrorHandler(cause -> {
                    cause.printStackTrace();
                    return cause;
                }).build();
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntries(@NonNull final Boolean refresh) {
        return mRestClient.getEntries().flatMap(Observable::from);
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSince(@NonNull final Boolean refresh,
                                                              @NonNull final Long timestamp) {
        return mRestClient.getEntries("", null, timestamp, 0).flatMap(Observable::from);
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull final Boolean refresh,
                                                                       @NonNull final String search) {
        return mRestClient.getEntries(search).flatMap(Observable::from);
    }

    @Override
    public Observable<Holiday> holidays(@NonNull final Boolean refresh) {
        return mRestClient.getHolidays().flatMap(Observable::from);
    }

    @Override
    public Observable<Exam> exams(@NonNull final Boolean refresh) {
        return mRestClient.getExams().flatMap(Observable::from);
    }

    @Override
    public Observable<Exam> examsOfUser() {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Exam> examsByTimetable() {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Boolean> isExamPined(@NonNull Exam exam) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Boolean> pinExam(@NonNull Exam exam) {
        throw new UnsupportedOperationException("Method is not supported by the rest api");
    }

    @Override
    public Observable<Presence> fsPresence() {
        return mRestClient.getPresence().flatMap(Observable::from);
    }

    @Override
    public Observable<News> fsNews(@NonNull final Boolean refresh) {
        return mRestClient.getNews().flatMap(Observable::from);
    }

    @Override
    public Observable<SimpleJob> jobs(@NonNull final Boolean refresh) {
        return mRestClient.getJobs().flatMap(Observable::from);
    }

    @Override
    public Observable<SimpleJob> jobByTitle(@NonNull final Boolean refresh, @NonNull final String title) {
        return mRestClient.getJobs(title).flatMap(Observable::from);
    }

    @Override
    public Observable<LostFound> lostfound(@NonNull final Boolean refresh) {
        return mRestClient.getLostAndFound().flatMap(Observable::from);
    }

    @Override
    public Observable<Meal> meals(@NonNull final Boolean refresh) {
        return mRestClient.getMeals(StudentWorkMunich.MENSA_LOTHSTRASSE).flatMap(Observable::from);
    }

    @Override
    public Observable<Module> moduleById(@NonNull final String id) {
        return mRestClient.getModuleById(id);
    }

    @Override
    public Observable<PublicTransport> publicTransportPasing() {
        return mRestClient.getPublicTransports(PublicTransportLocation.PASING).flatMap(Observable::from);
    }

    @Override
    public Observable<PublicTransport> publicTransportLothstrasse() {
        return mRestClient.getPublicTransports(PublicTransportLocation.LOTHSTR).flatMap(Observable::from);
    }

    @Override
    public Observable<SimpleRoom> freeRooms(@NonNull final Day day,
                                            @NonNull final Time time) {
        return mRestClient.getRoomByDateTime(
                RoomType.ALL,
                day,
                time.getStart().get(Calendar.HOUR_OF_DAY),
                time.getStart().get(Calendar.MINUTE)
        ).flatMap(Observable::from);
    }

    @Override
    public Observable<Lesson> timetable(@NonNull final Boolean refresh) {
        final String key = LessonGroupSaver.class.getSimpleName() + "_cache";

        if (!mPrefs.contains(key)) {
            return Observable.empty();
        }

        List<LessonGroupSaver> cache;
        try {
            cache = mGson.fromJson(mPrefs.getString(key, ""),
                    new TypeToken<List<LessonGroupSaver>>() {
                    }.getType());
            if (cache.isEmpty()) {
                return Observable.empty();
            }
        } catch (Exception e) {
            return Observable.error(e);
        }

        return Observable.from(cache)
                .filter(lessonGroupSaver -> !lessonGroupSaver.isManuelAdded())
                .flatMap(lessonGroupSaver -> {
                    if (lessonGroupSaver.hasPk()) {
                        return mRestClient.getLessons(lessonGroupSaver.getLessonGroup().getGroup(),
                                lessonGroupSaver.getLessonGroup().getModule().getId(),
                                lessonGroupSaver.getLessonGroup().getTeacher().getId(),
                                lessonGroupSaver.getSelectedPk()).flatMap(Observable::from);
                    } else {
                        return mRestClient.getLessons(lessonGroupSaver.getLessonGroup().getGroup(),
                                lessonGroupSaver.getLessonGroup().getModule().getId(),
                                lessonGroupSaver.getLessonGroup().getTeacher().getId()).flatMap(Observable::from);
                    }
                });
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull final Group group) {
        return mRestClient.getLessonGroups(group).flatMap(Observable::from);
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
