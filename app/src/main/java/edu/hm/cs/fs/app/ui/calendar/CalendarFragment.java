package edu.hm.cs.fs.app.ui.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.CalendarPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.ICalendarView;

/**
 * Created by FHellman on 11.08.2015.
 */
public class CalendarFragment extends BaseFragment<CalendarPresenter> implements ICalendarView, WeekView.MonthChangeListener {
    @Bind(R.id.weekView)
    WeekView mWeekView;

    private final List<WeekViewEvent> mData = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mWeekView.setMonthChangeListener(this);
        mWeekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                getPresenter().loadEvents(newFirstVisibleDay.get(Calendar.YEAR),
                        newFirstVisibleDay.get(Calendar.DAY_OF_MONTH));
            }
        });

        setPresenter(new CalendarPresenter(this));
        /*
        final Calendar firstVisibleDay = mWeekView.getFirstVisibleDay();
        getPresenter().loadEvents(firstVisibleDay.get(Calendar.YEAR),
                firstVisibleDay.get(Calendar.DAY_OF_MONTH));
                */
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_calendar;
    }

    @Override
    public void showContent(List<WeekViewEvent> content) {
        mData.clear();
        mData.addAll(content);
        mWeekView.notifyDatasetChanged();
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int year, int month) {
        return mData;
    }

    @Override
    public void showError(@NonNull String error) {
        Snackbar.make(mWeekView, error, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar firstVisibleDay = mWeekView.getFirstVisibleDay();
                        getPresenter().loadEvents(firstVisibleDay.get(Calendar.YEAR),
                                firstVisibleDay.get(Calendar.DAY_OF_MONTH));
                    }
                }).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
