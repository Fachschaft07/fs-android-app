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
import rx.Observable;

/**
 * The data service provides all the data. The data service decides where to load the data from
 * by priority:
 * <ol>
 *     <li>Memory</li>
 *     <li>Disk</li>
 *     <li>Cloud</li>
 * </ol>
 * The data with priority 1 will be loaded more faster then from priority 3.<br>
 * Most of the data service methods has the possibility to explicitly sync the data with the cloud
 * to get the actual data set.
 * <br><b>WARN:</b> This shouldn't been use to often due to the speed of loading and the internet
 * band width.
 *
 * @author Fabio
 */
public interface IDataService {
    // #####################################################
    // # Blackboard                                        #
    // #####################################################

    /**
     * Get all blackboard entries.
     *
     * @param refresh Set to <code>true</code> if the data should be updated.
     * @return the entries.
     */
    Observable<BlackboardEntry> blackboardEntries(@NonNull final Boolean refresh);

    /**
     * Get all blackboard entries which get present since yesterday.
     *
     * @param refresh Set to <code>true</code> if the data should be updated.
     * @return the entries.
     */
    Observable<BlackboardEntry> blackboardEntriesSinceYesterday(@NonNull final Boolean refresh);

    Observable<BlackboardEntry> blackboardEntriesSince(@NonNull final Boolean refresh,
                                                       @NonNull final Long timestamp);

    Observable<BlackboardEntry> blackboardEntrieById(@NonNull final Boolean refresh,
                                                     @NonNull final String id);

    Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull final Boolean refresh,
                                                                @NonNull final String search);

    // #####################################################
    // # Calendar                                          #
    // #####################################################

    Observable<Holiday> holidays(@NonNull final Boolean refresh);

    Observable<Holiday> nextHolidays(@NonNull final Boolean refresh);

    // #####################################################
    // # Exam                                              #
    // #####################################################

    Observable<Exam> exams(@NonNull final Boolean refresh);

    Observable<Exam> exams(@NonNull final Boolean refresh,
                           @NonNull final String search);

    Observable<Exam> examsOfUser();

    Observable<Exam> examsByTimetable();

    Observable<Boolean> isExamPined(@NonNull final Exam exam);

    Observable<Boolean> pinExam(@NonNull final Exam exam);

    // #####################################################
    // # FS                                                #
    // #####################################################

    Observable<Presence> fsPresence();

    Observable<News> fsNews(@NonNull final Boolean refresh);

    Observable<News> fsNewsByTitle(@NonNull final Boolean refresh,
                                   @NonNull final String title);

    Observable<News> fsNewsTop3(@NonNull final Boolean refresh);

    // #####################################################
    // # Job                                               #
    // #####################################################

    Observable<SimpleJob> jobs(@NonNull final Boolean refresh);

    Observable<SimpleJob> jobByTitle(@NonNull final Boolean refresh,
                                     @NonNull final String title);

    // #####################################################
    // # Lost & Found                                      #
    // #####################################################

    Observable<LostFound> lostfound(@NonNull final Boolean refresh);

    // #####################################################
    // # Meal                                              #
    // #####################################################

    Observable<Meal> meals(@NonNull final Boolean refresh);

    Observable<Meal> mealsOfToday(@NonNull final Boolean refresh);

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
