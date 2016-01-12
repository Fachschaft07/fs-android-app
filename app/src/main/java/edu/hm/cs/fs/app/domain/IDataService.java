package edu.hm.cs.fs.app.domain;

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
import rx.Observable;

/**
 * @author Fabio
 */
public interface IDataService {
    // #####################################################
    // # Blackboard                                        #
    // #####################################################

    Observable<BlackboardEntry> blackboardEntries(@NonNull final Boolean refresh);

    Observable<BlackboardEntry> blackboardEntriesSinceYesterday();

    Observable<BlackboardEntry> blackboardEntriesSince(@NonNull final Long timestamp);

    Observable<BlackboardEntry> blackboardEntrieById(@NonNull final String id);

    Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull final String search);

    // #####################################################
    // # Calendar                                          #
    // #####################################################

    Observable<Holiday> holidays();

    Observable<Holiday> nextHolidays();

    // #####################################################
    // # Exam                                              #
    // #####################################################

    Observable<Exam> exams(@NonNull final Boolean refresh);

    Observable<Exam> examsOfUser();

    Observable<Void> pinExam(@NonNull final Exam exam);

    Observable<Void> unpinExam(@NonNull final Exam exam);

    // #####################################################
    // # FS                                                #
    // #####################################################

    Observable<Presence> fsPresence();

    Observable<News> fsNews(@NonNull final Boolean refresh);

    Observable<News> fsNewsByTitle(@NonNull final String title);

    Observable<News> fsNewsTop3();

    // #####################################################
    // # Job                                               #
    // #####################################################

    Observable<SimpleJob> jobs(@NonNull final Boolean refresh);

    Observable<SimpleJob> jobByTitle(@NonNull final String title);

    // #####################################################
    // # Lost & Found                                      #
    // #####################################################

    Observable<LostFound> lostfound();

    // #####################################################
    // # Meal                                              #
    // #####################################################

    Observable<Meal> meals(@NonNull final Boolean refresh);

    Observable<Meal> mealsOfToday();

    // #####################################################
    // # Module                                            #
    // #####################################################

    Observable<Module> moduleById(@NonNull final String id);

    // #####################################################
    // # Public Transport                                  #
    // #####################################################

    Observable<PublicTransport> publicTransportPasing();

    Observable<PublicTransport> publicTransportLothstrasse();

    // #####################################################
    // # Roomsearch                                        #
    // #####################################################

    Observable<SimpleRoom> freeRooms(@NonNull final Day day,
                                     @NonNull final Time time);

    // #####################################################
    // # Timetable                                         #
    // #####################################################

    Observable<Lesson> timetable(@NonNull final Boolean refresh);

    Observable<LessonGroup> lessonsByGroup(@NonNull final Group group);

    Observable<Lesson> nextLesson();

    Observable<Void> save(@NonNull final LessonGroup lessonGroup,
                          @NonNull final Boolean selected);

    Observable<Void> save(@NonNull final LessonGroup lessonGroup,
                          @NonNull final Integer pk,
                          @NonNull final Boolean selected);

    Observable<Boolean> isPkSelected(@NonNull final LessonGroup lessonGroup,
                                     @NonNull final Integer pk);

    Observable<Boolean> isModuleSelected(@NonNull final LessonGroup lessonGroup);

    Observable<Void> resetTimetable();
}
