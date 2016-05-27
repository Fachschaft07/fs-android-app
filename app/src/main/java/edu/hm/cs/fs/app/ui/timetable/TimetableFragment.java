package edu.hm.cs.fs.app.ui.timetable;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.LinearLayout;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.TimetablePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.view.ITimetableView;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public class TimetableFragment extends BaseFragment<TimetablePresenter> implements ITimetableView,
        Toolbar.OnMenuItemClickListener, MonthLoader.MonthChangeListener, WeekView.EventClickListener {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.weekView)
    WeekView mWeekView;

    private List<Lesson> mLessons;
    private Map<Integer, Lesson> mContent = new HashMap<>();

    private int mVisibleDays = 2;

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
        mWeekView.setNumberOfVisibleDays(mVisibleDays);
        mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {

            }
        });

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
                mVisibleDays = 5;
                mWeekView.setNumberOfVisibleDays(mVisibleDays);
                item.setChecked(true);
                return true;
            case R.id.menu_two_day_view:
                mVisibleDays = 2;
                mWeekView.setNumberOfVisibleDays(mVisibleDays);
                item.setChecked(true);
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
        mContent.clear();
        mWeekView.notifyDatasetChanged();
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

        final int maxDaysOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int dayOfMonth = 1; dayOfMonth <= maxDaysOfMonth; dayOfMonth++) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            for (Lesson lesson : mLessons) {
                if(lesson.getDay().getCalendarId() == calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar start = Calendar.getInstance();
                    start.set(newYear, newMonth - 1, dayOfMonth, lesson.getHour(), lesson.getMinute(), 0);

                    Calendar end = Calendar.getInstance();
                    end.setTimeInMillis(start.getTimeInMillis());
                    end.add(Calendar.MINUTE, 90);

                    final WeekViewEvent weekViewEvent = new WeekViewEvent(mContent.size(), lesson.getModule().getName(), lesson.getRoom(), start, end);
                    events.add(weekViewEvent);
                    mContent.put(mContent.size(), lesson);
                }
            }
        }

        return events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        final Lesson foundLesson = mContent.get((int) event.getId());

        if(foundLesson == null || TextUtils.isEmpty(foundLesson.getModule().getId()) || foundLesson.getModule().getId().contains(" ")) {
            Snackbar.make(mWeekView, R.string.no_detail_subject_info, Snackbar.LENGTH_SHORT);
            return;
        }

        Bundle arguments = new Bundle();
        arguments.putString(TimetableLessonFragment.ARG_MODULE_ID, foundLesson.getModule().getId());
        if(foundLesson.getTeacher() != null) {
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
