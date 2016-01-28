package edu.hm.cs.fs.app.ui.timetable.structure;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.hm.cs.fs.app.ui.timetable.TimetableAdapter;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public class LessonCell extends Cell {
    private final List<Lesson> mLessons;
    private int mSelectedLessonPosition;
    private Day mDay;
    private Time mTime;

    public LessonCell(int row, int column, Lesson... lessons) {
        super(row, column, R.drawable.listitem_timetable_lesson_border);
        mLessons = new ArrayList<>(Arrays.asList(lessons));
        mDay = Day.values()[column - 1];
        mTime = Time.values()[row - 1];
        mSelectedLessonPosition = 0;
    }

    public Lesson getSelectedLesson() {
        return mLessons.get(mSelectedLessonPosition);
    }

    public void clearLessons() {
        mLessons.clear();
    }

    public void addLesson(@NonNull final Lesson lesson) {
        mLessons.add(lesson);
    }

    public List<Lesson> getLessons() {
        return mLessons;
    }

    public Day getDay() {
        return mDay;
    }

    public Time getTime() {
        return mTime;
    }

    public void setSelection(int selection) {
        mSelectedLessonPosition = selection;
    }

    @Override
    public void render(TimetableAdapter.ViewHolder holder) {
        super.render(holder);

        if (!mLessons.isEmpty()) {
            final Lesson lesson = mLessons.get(mSelectedLessonPosition);

            holder.mLayoutSubject.setVisibility(View.VISIBLE);
            setText(holder.mSubject, lesson.getModule().getName());
            setText(holder.mRoom, lesson.getRoom());
            setText(holder.mInfo, lesson.getSuffix());

            if (mLessons.size() > 1) {
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(2)
                        .fontSize(holder.mContext.getResources().getDimensionPixelSize(R.dimen.text_size_mvv_line))
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT_BOLD)
                        .endConfig()
                        .roundRect(10)
                        .build(Integer.toString(mLessons.size()), holder.mContext.getResources().getColor(R.color.lesson_count_background));
                holder.mCount.setImageDrawable(drawable);
                holder.mCount.setVisibility(View.VISIBLE);
            }
        }
    }
}
