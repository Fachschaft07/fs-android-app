package edu.hm.cs.fs.app.database.memory;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

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
import rx.Observable;

/**
 * Created by FHellman on 12.01.2016.
 */
public class MemoryService implements IDataService {
    @Inject
    public MemoryService(SharedPreferences prefs) {

    }

    @Override
    public Observable<BlackboardEntry> blackboardEntries(@NonNull Boolean refresh) {
        return null;
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSinceYesterday() {
        return null;
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesSince(@NonNull Long timestamp) {
        return null;
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntrieById(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull String search) {
        return null;
    }

    @Override
    public Observable<Holiday> holidays() {
        return null;
    }

    @Override
    public Observable<Holiday> nextHolidays() {
        return null;
    }

    @Override
    public Observable<Exam> exams(@NonNull Boolean refresh) {
        return null;
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
        return null;
    }

    @Override
    public Observable<News> fsNews(@NonNull Boolean refresh) {
        return null;
    }

    @Override
    public Observable<News> fsNewsByTitle(@NonNull String title) {
        return null;
    }

    @Override
    public Observable<News> fsNewsTop3() {
        return null;
    }

    @Override
    public Observable<SimpleJob> jobs(@NonNull Boolean refresh) {
        return null;
    }

    @Override
    public Observable<SimpleJob> jobByTitle(@NonNull String title) {
        return null;
    }

    @Override
    public Observable<LostFound> lostfound() {
        return null;
    }

    @Override
    public Observable<Meal> meals(@NonNull Boolean refresh) {
        return null;
    }

    @Override
    public Observable<Meal> mealsOfToday() {
        return null;
    }

    @Override
    public Observable<Module> moduleById(@NonNull String id) {
        return null;
    }

    @Override
    public Observable<PublicTransport> publicTransportPasing() {
        return null;
    }

    @Override
    public Observable<PublicTransport> publicTransportLothstrasse() {
        return null;
    }

    @Override
    public Observable<SimpleRoom> freeRooms(@NonNull Day day, @NonNull Time time) {
        return null;
    }

    @Override
    public Observable<Lesson> timetable(@NonNull Boolean refresh) {
        return null;
    }

    @Override
    public Observable<LessonGroup> lessonsByGroup(@NonNull Group group) {
        return null;
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
