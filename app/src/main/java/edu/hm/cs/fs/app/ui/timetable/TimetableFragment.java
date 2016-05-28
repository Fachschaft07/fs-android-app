package edu.hm.cs.fs.app.ui.timetable;

import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.TimetablePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.view.ITimetableView;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public class TimetableFragment extends BaseFragment<TimetablePresenter> implements ITimetableView,
        Toolbar.OnMenuItemClickListener, MonthLoader.MonthChangeListener, WeekView.EventClickListener {

    private static final int NUMBER_OF_VISIBLE_DAYS_WEEK = 5;
    private static final int NUMBER_OF_VISIBLE_DAYS_TWO_DAYS = 2;
    @ColorRes
    private static final int[] SUBJECT_COLORS = {
            R.color.subject_color_0,
            R.color.subject_color_1,
            R.color.subject_color_2,
            R.color.subject_color_3,
            R.color.subject_color_4,
            R.color.subject_color_5,
            R.color.subject_color_6,
            R.color.subject_color_7,
            R.color.subject_color_8,
            R.color.subject_color_9
    };
    public static final String TIMETABLE_VISIBLE_DAYS = "timetable_visible_days";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.weekView)
    WeekView mWeekView;

    private List<Lesson> mLessons = new ArrayList<>();
    private List<Holiday> mHolidays = new ArrayList<>();
    private Map<Integer, Lesson> mContent = new HashMap<>();
    private Map<String, Integer> mSubjectColorMap = new HashMap<>();

    private SharedPreferences mPrefs;
    private int mVisibleDays;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });
        mToolbar.inflateMenu(R.menu.timetable);
        mToolbar.setOnMenuItemClickListener(this);

        mSwipeRefreshLayout.setEnabled(false);

        mWeekView.setMonthChangeListener(this);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setShowNowLine(true);
        mWeekView.setOverlappingEventGap(2);
        mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                return String.format(Locale.getDefault(), "%1$ta %1$td.%1$tm", date);
            }

            @Override
            public String interpretTime(int hour) {
                return String.format(Locale.getDefault(), "%2d", hour);
            }
        });

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mVisibleDays = mPrefs.getInt(TIMETABLE_VISIBLE_DAYS, NUMBER_OF_VISIBLE_DAYS_WEEK);
        mWeekView.setNumberOfVisibleDays(mVisibleDays);

        // is in portrait mode?
        if(mToolbar.getMenu().findItem(R.id.menu_week_view) != null) {
            // YES! --> Portrait Mode
            if (mVisibleDays == NUMBER_OF_VISIBLE_DAYS_WEEK) {
                mToolbar.getMenu().findItem(R.id.menu_two_day_view).setChecked(false);
                mToolbar.getMenu().findItem(R.id.menu_week_view).setChecked(true);
            } else {
                mToolbar.getMenu().findItem(R.id.menu_two_day_view).setChecked(true);
                mToolbar.getMenu().findItem(R.id.menu_week_view).setChecked(false);
            }
        }

        setPresenter(new TimetablePresenter(getActivity(), this));
        getPresenter().loadTimetable(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_timetable;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_today:
                mWeekView.goToToday();
                mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                return true;
            case R.id.menu_week_view:
                final Calendar date1 = mWeekView.getFirstVisibleDay();
                mVisibleDays = NUMBER_OF_VISIBLE_DAYS_WEEK;
                mPrefs.edit().putInt(TIMETABLE_VISIBLE_DAYS, mVisibleDays).apply();
                mWeekView.setNumberOfVisibleDays(mVisibleDays);
                mWeekView.goToDate(date1);
                mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                item.setChecked(true);
                return true;
            case R.id.menu_two_day_view:
                final Calendar date2 = mWeekView.getFirstVisibleDay();
                mVisibleDays = NUMBER_OF_VISIBLE_DAYS_TWO_DAYS;
                mPrefs.edit().putInt(TIMETABLE_VISIBLE_DAYS, mVisibleDays).apply();
                mWeekView.setNumberOfVisibleDays(mVisibleDays);
                mWeekView.goToDate(date2);
                mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                item.setChecked(true);
                return true;
            case R.id.menu_refresh:
                onRefresh();
                return true;
            case R.id.menu_lesson_selection:
                MainActivity.getNavigator().goTo(new TimetableEditorFragment());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void showContent(List<Lesson> content) {
        mLessons = content;
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public void showHolidays(List<Holiday> content) {
        mHolidays = content;
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public void clearContent() {
        mContent.clear();
        mSubjectColorMap.clear();
    }

    @Override
    public void onRefresh() {
        getPresenter().loadTimetable(true);
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        final List<WeekViewEvent> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(newYear, newMonth - 1, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        final int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int dayOfMonth = 1; dayOfMonth <= maxDaysOfMonth; dayOfMonth++) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            boolean skipLesson = false;

            for (Holiday holiday : mHolidays) {
                final Calendar timeRangeStart = Calendar.getInstance();
                timeRangeStart.setTime(holiday.getStart());
                final Calendar timeRangeEnd = Calendar.getInstance();
                timeRangeEnd.setTime(holiday.getEnd());
                timeRangeEnd.set(Calendar.HOUR_OF_DAY, 23);
                timeRangeEnd.set(Calendar.MINUTE, 59);

                if (timeRangeStart.before(calendar) && timeRangeEnd.after(calendar)) {
                    skipLesson = true;

                    final Calendar start = (Calendar) calendar.clone();
                    start.set(Calendar.HOUR_OF_DAY, 0);
                    start.set(Calendar.MINUTE, 0);
                    final Calendar end = (Calendar) calendar.clone();
                    end.set(Calendar.HOUR_OF_DAY, 23);
                    end.set(Calendar.MINUTE, 59);

                    final WeekViewEvent holidayEvent = new WeekViewEvent(-mContent.size(), holiday.getName(), start, end);
                    holidayEvent.setColor(getResources().getColor(R.color.holiday));
                    events.add(holidayEvent);
                    break;
                }
            }

            if (!skipLesson) {
                for (Lesson lesson : mLessons) {
                    if (lesson.getDay().getCalendarId() == calendar.get(Calendar.DAY_OF_WEEK)) {
                        Calendar start = Calendar.getInstance();
                        start.set(newYear, newMonth - 1, dayOfMonth, lesson.getHour(), lesson.getMinute(), 0);

                        Calendar end = Calendar.getInstance();
                        end.setTimeInMillis(start.getTimeInMillis());
                        end.add(Calendar.MINUTE, 90);

                        final String moduleId = lesson.getModule().getId();

                        final String name = lesson.getModule().getName();
                        final String roomAndType = lesson.getRoom() + (TextUtils.isEmpty(lesson.getSuffix()) ? "" : " - " + lesson.getSuffix());
                        final WeekViewEvent weekViewEvent = new WeekViewEvent(mContent.size(), name, roomAndType, start, end);

                        final int color;
                        if (mSubjectColorMap.containsKey(moduleId)) {
                            color = mSubjectColorMap.get(moduleId);
                        } else {
                            int indexOf = mSubjectColorMap.size();
                            while (indexOf >= SUBJECT_COLORS.length) {
                                indexOf -= SUBJECT_COLORS.length;
                            }
                            color = getResources().getColor(SUBJECT_COLORS[indexOf]);
                            mSubjectColorMap.put(moduleId, color);
                        }
                        weekViewEvent.setColor(color);
                        events.add(weekViewEvent);
                        mContent.put(mContent.size(), lesson);
                    }
                }
            }
        }

        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (event.getId() < 0) {
            return;
        }

        final Lesson foundLesson = mContent.get((int) event.getId());

        if (foundLesson == null || TextUtils.isEmpty(foundLesson.getModule().getId()) || foundLesson.getModule().getId().contains(" ")) {
            Snackbar.make(mWeekView, R.string.no_detail_subject_info, Snackbar.LENGTH_SHORT);
            return;
        }

        Bundle arguments = new Bundle();
        arguments.putString(TimetableLessonFragment.ARG_MODULE_ID, foundLesson.getModule().getId());
        if (foundLesson.getTeacher() != null) {
            arguments.putString(TimetableLessonFragment.ARG_TEACHER_ID, foundLesson.getTeacher().getId());
        }

        final TimetableLessonFragment fragment = new TimetableLessonFragment();
        fragment.setArguments(arguments);

        MainActivity.getNavigator().goTo(fragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
