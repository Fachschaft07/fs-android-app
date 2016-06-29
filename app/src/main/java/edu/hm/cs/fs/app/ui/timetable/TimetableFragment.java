package edu.hm.cs.fs.app.ui.timetable;

import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.model.EventWrapper;
import edu.hm.cs.fs.app.presenter.TimetablePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.view.ITimetableView;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public class TimetableFragment extends BaseFragment<TimetablePresenter> implements ITimetableView,
        Toolbar.OnMenuItemClickListener, MonthLoader.MonthChangeListener, WeekView.EventClickListener {

    private static final int NUMBER_OF_VISIBLE_DAYS_WEEK = 5;
    private static final int NUMBER_OF_VISIBLE_DAYS_TWO_DAYS = 2;
    public static final String TIMETABLE_VISIBLE_DAYS = "timetable_visible_days";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.weekView)
    WeekView mWeekView;

    private List<EventWrapper> mEventContent = new ArrayList<>();

    private SharedPreferences mPrefs;
    private int mVisibleDays;
    private boolean refresh = true;

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

        setPresenter(new TimetablePresenter(getActivity(), this));

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
        if (mToolbar.getMenu().findItem(R.id.menu_week_view) != null) {
            // YES! --> Portrait Mode
            if (mVisibleDays == NUMBER_OF_VISIBLE_DAYS_WEEK) {
                mToolbar.getMenu().findItem(R.id.menu_two_day_view).setChecked(false);
                mToolbar.getMenu().findItem(R.id.menu_week_view).setChecked(true);
            } else {
                mToolbar.getMenu().findItem(R.id.menu_two_day_view).setChecked(true);
                mToolbar.getMenu().findItem(R.id.menu_week_view).setChecked(false);
            }
        }
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
    public void showContent(List<EventWrapper> content) {
        mEventContent = content;
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public void clearContent() {
        mEventContent.clear();
        EventWrapper.resetCounter();
    }

    @Override
    public void onRefresh() {
        refresh = true;
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        getPresenter().loadTimetable(refresh, newYear, newMonth);
        refresh = false;

        final List<WeekViewEvent> result = new ArrayList<>();
        final List<EventWrapper> holidays = EventWrapper.filter(mEventContent, EventWrapper.Type.HOLIDAY);

        for (EventWrapper event : mEventContent) {
            if (event.getType() == EventWrapper.Type.LESSON && EventWrapper.existsOnSameDate(holidays, event)) {
                continue;
            }
            final WeekViewEvent weekViewEvent = new WeekViewEvent(event.getId(), event.getName(), event.getPlace(), event.getStart(), event.getEnd());
            weekViewEvent.setColor(getResources().getColor(event.getColor()));
            result.add(weekViewEvent);
        }

        return result;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        if (event.getId() < 0) {
            return;
        }

        final EventWrapper wrapper = EventWrapper.search(mEventContent, event.getId());
        if (wrapper != null) {
            switch (wrapper.getType()) {
                case LESSON:
                    onLessonClick((Lesson) wrapper.getRaw());
                    break;
                case EXAM:
                    onExamClick((Exam) wrapper.getRaw());
                    break;
            }
            return;
        }

        Snackbar.make(mWeekView, R.string.no_detail_subject_info, Snackbar.LENGTH_SHORT);
    }

    private void onLessonClick(@NonNull final Lesson lesson) {
        Bundle arguments = new Bundle();
        arguments.putString(TimetableLessonFragment.ARG_MODULE_ID, lesson.getModule().getId());
        if (lesson.getTeacher() != null) {
            arguments.putString(TimetableLessonFragment.ARG_TEACHER_ID, lesson.getTeacher().getId());
        }

        final TimetableLessonFragment fragment = new TimetableLessonFragment();
        fragment.setArguments(arguments);

        MainActivity.getNavigator().goTo(fragment);
    }

    private void onExamClick(@NonNull final Exam exam) {
        // TODO Exam information
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
