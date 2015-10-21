package edu.hm.cs.fs.app.ui.timetable.structure;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;

import com.fk07.R;

import java.util.Calendar;
import java.util.Locale;

import edu.hm.cs.fs.app.ui.timetable.TimetableAdapter;
import edu.hm.cs.fs.common.constant.Day;

/**
 * @author Fabio
 */
public class DayCell extends Cell {
    private static final String DAY_LARGE_FORMAT = "%1$tA";
    private final Day mDay;

    public DayCell(int row, int column, Day day) {
        super(row, column, R.drawable.listitem_timetable_day_border);
        mDay = day;
    }

    public Day getDay() {
        return mDay;
    }

    @Override
    public void render(TimetableAdapter.ViewHolder holder) {
        super.render(holder);
        final Calendar current = Calendar.getInstance();
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, mDay.getCalendarId());

        holder.mLayoutSubject.setVisibility(View.VISIBLE);
        setText(holder.mSubject, String.format(Locale.getDefault(), DAY_LARGE_FORMAT, calendar));
        if (calendar.get(Calendar.DAY_OF_WEEK) == current.get(Calendar.DAY_OF_WEEK)) {
            // Underline current day
            SpannableString content = new SpannableString(holder.mSubject.getText());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            holder.mSubject.setText(content);
        }
    }
}
