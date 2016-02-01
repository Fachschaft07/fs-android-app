package edu.hm.cs.fs.domain;

import android.support.annotation.NonNull;

import java.util.Calendar;

import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Study;
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
import edu.hm.cs.fs.domain.util.DateUtils;
import rx.Observable;
import rx.functions.Func2;

/**
 * The data service provides all the data. The data service decides where to load the data from by
 * priority: <ol> <li>Memory</li> <li>Disk</li> <li>Cloud</li> </ol> The data with priority 1 will
 * be loaded more faster then from priority 3.<br> Most of the data service methods has the
 * possibility to explicitly sync the data with the cloud to get the actual data set.
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
    default Observable<BlackboardEntry> blackboardEntriesSinceYesterday(@NonNull Boolean refresh) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        yesterday.set(Calendar.MILLISECOND, 0);
        return blackboardEntriesSince(refresh, yesterday.getTimeInMillis());
    }

    default Observable<BlackboardEntry> blackboardEntriesSince(@NonNull Boolean refresh,
                                                               @NonNull Long timestamp) {
        return blackboardEntries(refresh)
                .filter(item -> item.getPublish().getTime() >= timestamp);
    }

    default Observable<BlackboardEntry> blackboardEntrieById(@NonNull Boolean refresh,
                                                             @NonNull final String id) {
        return blackboardEntries(refresh)
                .filter(blackboardEntry -> blackboardEntry.getId().equals(id))
                .first();
    }

    default Observable<BlackboardEntry> blackboardEntriesBySearchString(@NonNull Boolean refresh,
                                                                        @NonNull String search) {
        return blackboardEntries(refresh)
                .filter(item -> item.getSubject().contains(search)
                        || item.getText().contains(search));
    }

    // #####################################################
    // # Calendar                                          #
    // #####################################################

    Observable<Holiday> holidays(@NonNull final Boolean refresh);

    default Observable<Holiday> nextHolidays(@NonNull Boolean refresh) {
        return holidays(refresh).toSortedList((lhs, rhs) -> {
            return lhs.getStart().compareTo(rhs.getStart());
        }).flatMap(Observable::from).first();
    }

    // #####################################################
    // # Exam                                              #
    // #####################################################

    Observable<Exam> exams(@NonNull final Boolean refresh);

    default Observable<Exam> exams(@NonNull Boolean refresh, @NonNull String search) {
        return exams(refresh).filter(exam -> exam.getStudy().toString().equalsIgnoreCase(search)
                || Study.of(search) == null && exam.getModule().getName().contains(search)
                || exam.getCode().equalsIgnoreCase(search));
    }

    Observable<Exam> examsOfUser();

    default Observable<Exam> examsByTimetable() {
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

    default Observable<Boolean> isExamPined(@NonNull Exam exam) {
        return examsOfUser()
                .exists(exam1 -> exam1.getCode().equals(exam.getCode())
                        && exam1.getStudy() == exam.getStudy());
    }

    Observable<Boolean> pinExam(@NonNull final Exam exam);

    // #####################################################
    // # FS                                                #
    // #####################################################

    Observable<Presence> fsPresence();

    Observable<News> fsNews(@NonNull final Boolean refresh);

    default Observable<News> fsNewsByTitle(@NonNull Boolean refresh, @NonNull final String title) {
        return fsNews(refresh).filter(news -> news.getTitle().equalsIgnoreCase(title)).first();
    }

    default Observable<News> fsNewsTop3(@NonNull Boolean refresh) {
        return fsNews(refresh).limit(3);
    }

    // #####################################################
    // # Job                                               #
    // #####################################################

    Observable<SimpleJob> jobs(@NonNull final Boolean refresh);

    default Observable<SimpleJob> jobByTitle(@NonNull Boolean refresh, @NonNull String title) {
        return jobs(refresh).filter(item -> item.getTitle().equalsIgnoreCase(title)).first();
    }

    // #####################################################
    // # Lost & Found                                      #
    // #####################################################

    Observable<LostFound> lostfound(@NonNull final Boolean refresh);

    // #####################################################
    // # Meal                                              #
    // #####################################################

    Observable<Meal> meals(@NonNull final Boolean refresh);

    default Observable<Meal> mealsOfToday(@NonNull Boolean refresh) {
        return meals(refresh).filter(meal -> DateUtils.isToday(meal.getDate()));
    }

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

    default Observable<Lesson> nextLesson() {
        return timetable(false)
                .toSortedList(new Func2<Lesson, Lesson, Integer>() {
                    @Override
                    public Integer call(Lesson lesson, Lesson lesson2) {
                        return toCalendar(lesson).compareTo(toCalendar(lesson2));
                    }

                    private Calendar toCalendar(Lesson lesson) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, lesson.getHour());
                        calendar.set(Calendar.MINUTE, lesson.getMinute());

                        if (lesson.getDay().getCalendarId() == calendar.get(Calendar.DAY_OF_WEEK)) {
                            final Calendar temp = Calendar.getInstance();
                            temp.add(Calendar.MINUTE, -75);
                            if (temp.after(calendar)) {
                                calendar.add(Calendar.DATE, 7); // next week
                            }
                        } else {
                            do {
                                calendar.add(Calendar.DATE, 1);
                            }
                            while (lesson.getDay().getCalendarId() != calendar.get(Calendar.DAY_OF_WEEK));
                        }

                        return calendar;
                    }
                })
                .flatMap(Observable::from)
                .first();
    }

    default Observable<Void> save(@NonNull LessonGroup lessonGroup, @NonNull Boolean selected) {
        return save(lessonGroup, -1, selected);
    }

    Observable<Void> save(@NonNull final LessonGroup lessonGroup,
                          @NonNull final Integer pk,
                          @NonNull final Boolean selected);

    Observable<Boolean> isPkSelected(@NonNull final LessonGroup lessonGroup,
                                     @NonNull final Integer pk);

    Observable<Boolean> isModuleSelected(@NonNull final LessonGroup lessonGroup);

    Observable<Void> resetTimetable();
}
