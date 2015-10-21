package edu.hm.cs.fs.app.ui.timetable;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.LinearLayout;

import com.fk07.R;

import java.util.List;

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
        TimetableAdapter.OnItemClickListener, Toolbar.OnMenuItemClickListener {

    private static final int PORTRAIT_DAY_COUNT = 2;

    private static final int LANDSCAPE_DAY_COUNT = 5;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    private TimetableAdapter mAdapter;

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

        final int numberOfDays;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            numberOfDays = PORTRAIT_DAY_COUNT;
        } else {
            numberOfDays = LANDSCAPE_DAY_COUNT;
        }

        mAdapter = new TimetableAdapter(getActivity(), numberOfDays);
        mAdapter.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfDays + 1));

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
            case R.id.menu_prev_day:
                mAdapter.prevDay();
                return true;
            case R.id.menu_today:
                mAdapter.today();
                return true;
            case R.id.menu_next_day:
                mAdapter.nextDay();
                return true;
            case R.id.menu_week_view:
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                return true;
            case R.id.menu_two_day_view:
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        mAdapter.setData(content);
    }

    @Override
    public void onRefresh() {
        getPresenter().loadTimetable(true);
    }

    @Override
    public void onItemClicked(@NonNull Lesson lesson) {
        Bundle arguments = new Bundle();
        arguments.putString(TimetableLessonFragment.ARG_MODULE_ID, lesson.getModule().getId());
        if(lesson.getTeacher() != null) {
            arguments.putString(TimetableLessonFragment.ARG_TEACHER_ID, lesson.getTeacher().getId());
        }

        final TimetableLessonFragment fragment = new TimetableLessonFragment();
        fragment.setArguments(arguments);

        MainActivity.getNavigator().goTo(fragment);
    }

    @Override
    public void onEmptyClicked(@NonNull Day day, @NonNull Time time) {
        // TODO Add lessons manually
    }

    @Override
    public void onLessonSelection(@NonNull Lesson lesson) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
