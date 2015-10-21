package edu.hm.cs.fs.app.ui.timetable.structure;

import com.fk07.R;

import java.util.Locale;

import edu.hm.cs.fs.app.ui.timetable.TimetableAdapter;
import edu.hm.cs.fs.common.constant.Time;

/**
 * @author Fabio
 */
public class TimeCell extends Cell {
    private static final String TIME_FORMAT = "%1$tH:%1$tM%n-%n%2$tH:%2$tM";
    private final Time mTime;

    public TimeCell(int row, int column, Time time) {
        super(row, column, R.drawable.listitem_timetable_time_border);
        mTime = time;
    }

    public Time getTime() {
        return mTime;
    }

    @Override
    public void render(TimetableAdapter.ViewHolder holder) {
        super.render(holder);
        final Time time = Time.values()[getRow() - 1];
        setText(holder.mRoom,
                String.format(Locale.getDefault(), TIME_FORMAT, time.getStart(), time.getEnd()));
    }
}
