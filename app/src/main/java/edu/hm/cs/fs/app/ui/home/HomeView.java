package edu.hm.cs.fs.app.ui.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import edu.hm.cs.fs.app.ui.IView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public interface HomeView extends IView {

    void showNextLesson(@Nullable final Lesson lesson);

    void showBlackboardNews(@NonNull final List<BlackboardEntry> news);

    void showMealsOfToday(@NonNull final List<Meal> meals);

    void showLostAndFound(@NonNull final Integer amountOfLostFound);

    void showNextHoliday(@Nullable final Holiday holiday);

    void showFsNews(@NonNull final List<News> news);

    void showExams(@NonNull final List<Exam> exams);

    /**
     * Removes all cards from the home screen.
     */
    void clear();
}
