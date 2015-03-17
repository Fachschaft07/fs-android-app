package edu.hm.cs.fs.app.ui.timetable;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Date;
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

    private final List<WeekViewEvent> mBaseEvents = new ArrayList<>();

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
        mWeekView.goToHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        TerminHelper.listAllHolidays(getActivity(), new Callback<List<Holiday>>() {
            @Override
            public void onResult(final List<Holiday> result) {
                for (Holiday holiday : result) {
                    WeekViewEvent event = new WeekViewEvent();
                    event.setId(holiday.getName().hashCode());
                    event.setName(holiday.getName());

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(holiday.getStart());
                    event.setStartTime(cal);
                    cal.setTime(holiday.getEnd());
                    event.setEndTime(cal);

                    event.setColor(Color.BLUE);
                    mBaseEvents.add(event);
                    mWeekView.notifyDatasetChanged();
                }
            }
        });
        TerminHelper.listAllEvents(getActivity(), new Callback<List<Termin>>() {
            @Override
            public void onResult(final List<Termin> result) {
                for (Termin termin : result) {
                    WeekViewEvent event = new WeekViewEvent();
                    event.setId(termin.getSubject().hashCode());
                    event.setName(termin.getSubject());

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(termin.getDate());
                    cal.set(Calendar.HOUR_OF_DAY, 1);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    event.setStartTime(cal);
                    cal.add(Calendar.HOUR_OF_DAY, 22);
                    event.setEndTime(cal);

                    event.setColor(Color.MAGENTA);
                    mBaseEvents.add(event);
                    mWeekView.notifyDatasetChanged();
                }
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
        //Date minDate = getMinDate(year, month);
        //Date maxDate = getMaxDate(year, month);

        //List<WeekViewEvent> events = new ArrayList<>();

        return mBaseEvents;
    }

    private Date getMaxDate(final int year, final int month) {
        return null;
    }

    private Date getMinDate(final int year, final int month) {
        return null;
    }
}
