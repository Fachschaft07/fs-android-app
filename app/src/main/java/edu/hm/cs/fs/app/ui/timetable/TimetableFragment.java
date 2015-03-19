package edu.hm.cs.fs.app.ui.timetable;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.TerminHelper;
import edu.hm.cs.fs.app.datastore.model.Holiday;
import edu.hm.cs.fs.app.datastore.model.Termin;

/**
 * Created by Fabio on 15.03.2015.
 */
public class TimetableFragment extends Fragment implements WeekView.MonthChangeListener {
    @InjectView(R.id.weekView)
    WeekView mWeekView;

    private final List<WeekViewEvent> mHolidayEvents = new ArrayList<>();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(final Calendar calendar) {
                return String.format(Locale.getDefault(), "%1$ta %1$td.%1$tm.%1$ty", calendar);
            }

            @Override
            public String interpretTime(final int hour) {
                return String.format("%02d:00", hour);
            }
        });
        mWeekView.setMonthChangeListener(this);

        // TODO This is a bug of the library which should be fixed with the next update
        //mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        TerminHelper.listAllHolidays(getActivity(), new Callback<List<Holiday>>() {
            @Override
            public void onResult(final List<Holiday> result) {
                mHolidayEvents.clear();
                for (Holiday holiday : result) {
                    Log.d("Timetable", holiday.getName() + " from " + holiday.getStart() + " to " + holiday.getEnd());

                    Calendar start = Calendar.getInstance();
                    start.setTime(holiday.getStart());
                    start.set(Calendar.HOUR_OF_DAY, 0);
                    start.set(Calendar.MINUTE, 0);
                    start.set(Calendar.SECOND, 0);
                    start.set(Calendar.MILLISECOND, 0);

                    Calendar end = Calendar.getInstance();
                    end.setTime(holiday.getEnd());
                    end.set(Calendar.HOUR_OF_DAY, 23);
                    end.set(Calendar.MINUTE, 59);
                    end.set(Calendar.SECOND, 59);
                    end.set(Calendar.MILLISECOND, 999);

                    WeekViewEvent event = new WeekViewEvent();
                    event.setId(holiday.getName().hashCode());
                    event.setName(holiday.getName());
                    event.setStartTime(start);
                    event.setEndTime(end);
                    event.setColor(getResources().getColor(R.color.holiday));

                    mHolidayEvents.add(event);
                }
                mWeekView.notifyDatasetChanged();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.timetable, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_today:
                mWeekView.goToToday();
                // TODO This is a bug of the library which should be fixed with the next update
                //mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public List<WeekViewEvent> onMonthChange(final int year, final int month) {
        List<WeekViewEvent> events = new ArrayList<>();
        events.addAll(mHolidayEvents);

        return events;
    }
}
