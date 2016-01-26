package edu.hm.cs.fs.app.ui.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.app.ui.timetable.structure.Cell;
import edu.hm.cs.fs.app.ui.timetable.structure.DayCell;
import edu.hm.cs.fs.app.ui.timetable.structure.LessonCell;
import edu.hm.cs.fs.app.ui.timetable.structure.TimeCell;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {

    private static final int DAY_ROW = 1;
    private static final int TIME_COLUMN = 1;
    private static final int DAYS_OF_WEEK = 5;

    private final GridManager mGridManager = new GridManager();

    private final Context mContext;

    private final int mNumberOfDays;

    private OnItemClickListener mListener;

    private Calendar mToday;

    public TimetableAdapter(@NonNull final Context context, final int numberOfDays) {
        mContext = context;
        mNumberOfDays = numberOfDays;
        mToday = Calendar.getInstance();
        skipWeekend();
    }

    public void setData(@NonNull final List<Lesson> data) {
        mGridManager.update(data);
        notifyDataSetChanged();
    }

    public void clear() {
        notifyItemRangeRemoved(0, mGridManager.mCells.size());
        mGridManager.clear();
    }

    public void add(Lesson item) {
        mGridManager.update(item);
        notifyItemInserted(mGridManager.mCells.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_timetable, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // reset
        holder.mCellInfo = null;
        holder.mListener = null;

        // render
        final Cell cell = mGridManager.getCell(position, mToday);
        cell.render(holder);
        holder.mCellInfo = cell;
        holder.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return (TIME_COLUMN + mNumberOfDays) * (DAY_ROW + Time.values().length);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void nextDay() {
        mToday.add(Calendar.DATE, 1);
        if (mToday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            mToday.add(Calendar.DATE, 2);
        }
        notifyDataSetChanged();
    }

    public void prevDay() {
        mToday.add(Calendar.DATE, -1);
        if (mToday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            mToday.add(Calendar.DATE, -2);
        }
        notifyDataSetChanged();
    }

    public void today() {
        mToday.setTimeInMillis(System.currentTimeMillis());
        skipWeekend();
        notifyDataSetChanged();
    }

    private void skipWeekend() {
        switch (mToday.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                mToday.add(Calendar.DATE, 2);
                break;
            case Calendar.SUNDAY:
                mToday.add(Calendar.DATE, 1);
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(@NonNull final Lesson lesson);

        void onEmptyClicked(@NonNull final Day day, @NonNull final Time time);

        void onLessonSelection(@NonNull final Lesson lesson);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final Context mContext;
        @Bind(R.id.cell)
        public LinearLayout mCell;
        @Bind(R.id.layoutSubject)
        public RelativeLayout mLayoutSubject;
        @Bind(R.id.multipleLessons)
        public ImageView mCount;
        @Bind(R.id.textSubject)
        public TextView mSubject;
        @Bind(R.id.textRoom)
        public TextView mRoom;
        @Bind(R.id.textInfo)
        public TextView mInfo;
        public OnItemClickListener mListener;

        private Cell mCellInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }

        @OnClick(R.id.multipleLessons)
        public void onCountClick() {
            if (mCellInfo instanceof LessonCell && ((LessonCell) mCellInfo).getLessons().size() > 1) {
                final LessonCell lessonCell = (LessonCell) mCellInfo;

                final ArrayAdapter<Lesson> adapter = new ArrayAdapter<Lesson>(mContext,
                        android.R.layout.simple_list_item_1, lessonCell.getLessons()) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        final View view = super.getView(position, convertView, parent);
                        final TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setText(getItem(position).getModule().getName());
                        return view;
                    }
                };

                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.choose_subject)
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                lessonCell.setSelection(which);
                                if (mListener != null) {
                                    mListener.onLessonSelection(adapter.getItem(which));
                                }
                            }
                        })
                        .create()
                        .show();
            } else {
                onClick();
            }
        }

        @OnClick({R.id.cell, R.id.textSubject, R.id.textRoom, R.id.textInfo})
        public void onClick() {
            if (mListener != null && mCellInfo instanceof LessonCell) {
                final LessonCell lessonCell = (LessonCell) mCellInfo;
                if (!lessonCell.getLessons().isEmpty()) {
                    mListener.onItemClicked(lessonCell.getSelectedLesson());
                } else {
                    mListener.onEmptyClicked(lessonCell.getDay(), lessonCell.getTime());
                }
            }
        }
    }

    private final class GridManager {
        private final List<Cell> mCells = new ArrayList<>();

        public GridManager() {
            // Add empty left upper corner
            mCells.add(new Cell(0, 0, R.drawable.listitem_timetable_day_border));

            // Add days
            int dayIndex = 1; // skip first column
            for (Day day : Day.values()) {
                mCells.add(new DayCell(0, dayIndex++, day));
            }

            // Add times
            int timeIndex = 1; // skip first row
            for (Time time : Time.values()) {
                mCells.add(new TimeCell(timeIndex++, 0, time));
            }

            // Add default empty lessons
            for (int col = 1; col < Day.values().length - 2; col++) {
                for (int row = 1; row < Time.values().length; row++) {
                    mCells.add(new LessonCell(row, col));
                }
            }
        }

        public void clear() {
            // Reset everything
            for (Cell cell : mCells) {
                if(cell instanceof LessonCell) {
                    LessonCell lessonCell = (LessonCell) cell;
                    lessonCell.getLessons().clear();
                }
            }
        }

        public void update(@NonNull final Lesson lesson) {
            final LessonCell lessonCell = findBy(lesson.getDay(), lesson.getHour(), lesson.getMinute());
            if (lessonCell == null) {
                final int column = Arrays.asList(Day.values())
                        .indexOf(lesson.getDay()) + TIME_COLUMN;
                final int row = Arrays.asList(Time.values())
                        .indexOf(getTimeByInt(lesson.getHour(), lesson.getMinute())) + DAY_ROW;
                mCells.add(new LessonCell(row, column, lesson));
            } else {
                lessonCell.getLessons().add(lesson);
            }
        }

        public void update(@NonNull final List<Lesson> lessons) {
            // Add empty left upper corner
            mCells.add(new Cell(0, 0, R.drawable.listitem_timetable_day_border));

            // Add days
            int dayIndex = 1; // skip first column
            for (Day day : Day.values()) {
                mCells.add(new DayCell(0, dayIndex++, day));
            }

            // Add times
            int timeIndex = 1; // skip first row
            for (Time time : Time.values()) {
                mCells.add(new TimeCell(timeIndex++, 0, time));
            }

            // Add lessons
            for (Lesson lesson : lessons) {
                final LessonCell lessonCell = findBy(lesson.getDay(), lesson.getHour(), lesson.getMinute());
                if (lessonCell == null) {
                    final int column = Arrays.asList(Day.values())
                            .indexOf(lesson.getDay()) + TIME_COLUMN;
                    final int row = Arrays.asList(Time.values())
                            .indexOf(getTimeByInt(lesson.getHour(), lesson.getMinute())) + DAY_ROW;
                    mCells.add(new LessonCell(row, column, lesson));
                } else {
                    lessonCell.getLessons().add(lesson);
                }
            }
        }

        @Nullable
        private Time getTimeByInt(final int hour, final int minute) {
            for (Time time : Time.values()) {
                if (time.getStart().get(Calendar.HOUR_OF_DAY) == hour
                        && time.getStart().get(Calendar.MINUTE) == minute) {
                    return time;
                }
            }
            return null;
        }

        @NonNull
        public Cell getCell(final int position, final Calendar selectedDay) {
            int row = position / (mNumberOfDays + DAY_ROW);
            int column = position % (mNumberOfDays + TIME_COLUMN);

            // BugFix: If the timetable configurator was opened, the cells haven't been updated yet
            if (mCells.isEmpty()) {
                return new Cell(row, column, R.drawable.listitem_timetable_lesson_border);
            }

            if (mNumberOfDays != DAYS_OF_WEEK && column != 0) { // skip the time column
                column += selectedDay.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
                if (column >= Calendar.SATURDAY - 1) {
                    column = 1;
                }
            }

            final Cell cell = findBy(row, column);
            return cell != null ? cell : new LessonCell(row, column);
        }

        @Nullable
        private LessonCell findBy(@NonNull final Day day, final int hour, final int minute) {
            for (Cell cell : mCells) {
                if (cell instanceof LessonCell) {
                    final LessonCell lessonCell = (LessonCell) cell;
                    final Time time = lessonCell.getTime();
                    if (lessonCell.getDay() == day
                            && time.getStart().get(Calendar.HOUR_OF_DAY) == hour
                            && time.getStart().get(Calendar.MINUTE) == minute) {
                        return lessonCell;
                    }
                }
            }
            return null;
        }

        @Nullable
        private Cell findBy(final int row, final int col) {
            for (Cell cell : mCells) {
                if (cell.getRow() == row && cell.getColumn() == col) {
                    return cell;
                }
            }
            return null;
        }
    }
}
