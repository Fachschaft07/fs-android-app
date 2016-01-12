package edu.hm.cs.fs.app.database.disk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Date;

import javax.inject.Inject;

import edu.hm.cs.fs.app.domain.IDataService;
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
import edu.hm.cs.fs.restclient.typeadapter.DateTypeAdapter;
import edu.hm.cs.fs.restclient.typeadapter.GroupTypeAdapter;
import rx.Observable;

/**
 * @author Fabio
 */
public class DiskService implements IDataService {
    private static final String EXAM_FILE = "exams.json";
    private static final String TIMETABLE_FILE = "timetable.json";
    private static final String TIMETABLE_CONFIG_FILE = "timetable-config.json";

    private static final String CHARSET = "UTF-8";

    @NonNull
    private final Context mContext;
    private final Gson mGson;

    @Inject
    public DiskService(@NonNull final Context context) {
        mContext = context;
        mGson = new GsonBuilder()
                .registerTypeAdapter(Group.class, new GroupTypeAdapter())
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntries(@NonNull Boolean refresh) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSinceYesterday() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSince(@NonNull Long timestamp) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntrieById(@NonNull String id) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull String search) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Holiday> holidays() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Holiday> nextHolidays() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Exam> exams(@NonNull Boolean refresh) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Exam> examsOfUser() {
        return null;
    }

    @Override
    public Observable<Void> pinExam(@NonNull Exam exam) {
        return null;
    }

    @Override
    public Observable<Void> unpinExam(@NonNull Exam exam) {
        return null;
    }

    @Override
    public Observable<Presence> fsPresence() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<News> fsNews(@NonNull Boolean refresh) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<News> fsNewsByTitle(@NonNull String title) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<News> fsNewsTop3() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<SimpleJob> jobs(@NonNull Boolean refresh) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<SimpleJob> jobByTitle(@NonNull String title) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<LostFound> lostfound() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Meal> meals(@NonNull Boolean refresh) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Meal> mealsOfToday() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Module> moduleById(@NonNull String id) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<PublicTransport> publicTransportPasing() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<PublicTransport> publicTransportLothstrasse() {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<SimpleRoom> freeRooms(@NonNull Day day, @NonNull Time time) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Lesson> timetable(@NonNull Boolean refresh) {
        return null;
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull Group group) {
        throw new UnsupportedOperationException("Method is not supported by the disk service");
    }

    @Override
    public Observable<Lesson> nextLesson() {
        return null;
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
