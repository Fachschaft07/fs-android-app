package edu.hm.cs.fs.app.ui.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import edu.hm.cs.fs.app.datastore.helper.TimetableHelper;
import edu.hm.cs.fs.app.datastore.model.Holiday;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.Timetable;
import edu.hm.cs.fs.app.datastore.model.constants.Day;

/**
 * Created by Fabio on 15.03.2015.
 */
public class TimetableFragment extends Fragment implements WeekView.MonthChangeListener {
    @InjectView(R.id.weekView)
    WeekView mWeekView;

    private final List<WeekViewEvent> mHolidayEvents = new ArrayList<>();
    private Timetable mTimetable;

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

                    Calendar tmp = Calendar.getInstance();
                    tmp.setTimeInMillis(start.getTimeInMillis());

                    do {
                        WeekViewEvent event = new WeekViewEvent();
                        event.setId(holiday.getName().hashCode());
                        event.setName(holiday.getName());
                        event.setStartTime((Calendar) tmp.clone());

                        tmp.add(Calendar.HOUR_OF_DAY, 23);
                        tmp.add(Calendar.MINUTE, 59);
                        tmp.add(Calendar.SECOND, 59);
                        tmp.add(Calendar.MILLISECOND, 999);
                        event.setEndTime((Calendar) tmp.clone());
                        event.setColor(getResources().getColor(R.color.holiday));

                        mHolidayEvents.add(event);

                        tmp.add(Calendar.MILLISECOND, 1);
                    } while(end.after(tmp));
                }
                mWeekView.notifyDatasetChanged();
            }
        });

        TimetableHelper.getTimetable(getActivity(), new Callback<Timetable>() {
            @Override
            public void onResult(final Timetable result) {
                mTimetable = result;
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
            case R.id.menu_configure:
                startActivity(new Intent(getActivity(), TimetableConfiguratorActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public List<WeekViewEvent> onMonthChange(final int year, final int month) {
        List<WeekViewEvent> result = new ArrayList<>();

        // Add holidays
        result.addAll(mHolidayEvents);

        // Add lessons
        if(mTimetable != null) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, 1);
            int maxDaysThisMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

            while(cal.get(Calendar.DAY_OF_MONTH) <= maxDaysThisMonth) {
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                final Day day = Day.byCalendar(dayOfWeek);

                final List<Lesson> lessons = mTimetable.getLessons(day);
                for (Lesson lesson : lessons) {
                    cal.set(Calendar.HOUR_OF_DAY, lesson.getTime().getHour());
                    cal.set(Calendar.MINUTE, lesson.getTime().getMinute());

                    StringBuilder name = new StringBuilder();
                    name.append(lesson.getModule().getName()).append("\n");
                    name.append(lesson.getRoom()).append("\n");
                    if(!TextUtils.isEmpty(lesson.getSuffix())) {
                        name.append(lesson.getSuffix()).append("\n");
                    }
                    name.append(lesson.getTeacher().getTitle())
                            .append(" ")
                            .append(lesson.getTeacher().getLastName());

                    WeekViewEvent event = new WeekViewEvent();
                    event.setName(name.toString());
                    event.setStartTime((Calendar) cal.clone());
                    cal.add(Calendar.MINUTE, 90);
                    event.setEndTime((Calendar) cal.clone());

                    result.add(event);
                }

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        return result;
    }
}
