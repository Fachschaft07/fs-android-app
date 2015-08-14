package edu.hm.cs.fs.app.ui.timetable;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.common.constant.Day;
import edu.hm.cs.fs.common.constant.Time;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.ViewHolder> {
    private static final String TIME_FORMAT = "%1$tH:%1$tM\n-\n%2$tH:%2$tM";
    private static final String DAY_LARGE_FORMAT = "%1$tA";
    private static final String DAY_SMALL_FORMAT = "%1$ta";
    private static final int DAY_ROW = 1;
    private static final int TIME_COLUMN = 1;
    private static final int DAYS_OF_WEEK = 7;
    private final List<Lesson> mData = new ArrayList<>();
    private final Context mContext;
    private final int mNumberOfDays;
    private OnItemClickListener mListener;

    public TimetableAdapter(@NonNull final Context context, final int numberOfDays) {
        mContext = context;
        mNumberOfDays = numberOfDays;
    }

    public void setData(@NonNull final List<Lesson> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_timetable, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        resetHolder(holder);

        if (position == 0) {
            onBindEmptyCell(holder);
        } else if (position < TIME_COLUMN + mNumberOfDays) {
            onBindDayCell(holder, position - TIME_COLUMN);
        } else if (position % (TIME_COLUMN + mNumberOfDays) == 0) {
            onBindTimeCell(holder, position / (TIME_COLUMN + mNumberOfDays) - DAY_ROW);
        } else {
            onBindLessonCell(holder,
                    position % (TIME_COLUMN + mNumberOfDays) - TIME_COLUMN,
                    position / (TIME_COLUMN + mNumberOfDays) - DAY_ROW);
        }
    }

    private void resetHolder(ViewHolder holder) {
        holder.mCell.setBackgroundColor(Color.TRANSPARENT);
        holder.mSubject.setVisibility(View.VISIBLE);
        holder.mRoom.setVisibility(View.VISIBLE);
        holder.mInfo.setVisibility(View.VISIBLE);
        holder.mListener = null;
        holder.mLesson = null;
        holder.mDay = null;
        holder.mTime = null;
    }

    private void onBindEmptyCell(ViewHolder holder) {
        holder.mSubject.setVisibility(View.GONE);
        holder.mRoom.setVisibility(View.GONE);
        holder.mInfo.setVisibility(View.GONE);
    }

    private void onBindTimeCell(ViewHolder holder, int row) {
        final Time time = Time.values()[row];
        holder.mSubject.setText(String.format(Locale.getDefault(), TIME_FORMAT,
                time.getStart(), time.getEnd()));

        holder.mRoom.setVisibility(View.GONE);
        holder.mInfo.setVisibility(View.GONE);
    }

    private void onBindDayCell(ViewHolder holder, int column) {
        final Day day = getDayByColumn(column);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day.getCalendarId());

        final String dayFormat;
        if(mNumberOfDays != DAYS_OF_WEEK) {
            dayFormat = DAY_LARGE_FORMAT;
        } else {
            dayFormat = DAY_SMALL_FORMAT;
        }
        holder.mSubject.setText(String.format(Locale.getDefault(), dayFormat, calendar));
        holder.mSubject.setGravity(Gravity.CENTER);
        holder.mRoom.setVisibility(View.GONE);
        holder.mInfo.setVisibility(View.GONE);
    }

    private void onBindLessonCell(ViewHolder holder, int column, int row) {
        final Day day = getDayByColumn(column);
        final Time time = Time.values()[row];

        final Lesson lesson = findLesson(day, time);

        holder.mListener = mListener;
        holder.mLesson = lesson;
        holder.mDay = day;
        holder.mTime = time;

        if (lesson != null) {
            holder.mCell.setBackgroundColor(Color.CYAN);
            holder.mSubject.setText(lesson.getModule().getName());
            holder.mRoom.setText(lesson.getRoom());
            holder.mInfo.setText(lesson.getSuffix());
        } else {
            onBindEmptyCell(holder);
        }
    }

    private Day getDayByColumn(final int column) {
        if (mNumberOfDays != DAYS_OF_WEEK) {
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + column;
            if(dayOfWeek > Calendar.SATURDAY) {
                dayOfWeek = dayOfWeek - Calendar.SATURDAY;
            }
            for (Day day : Day.values()) {
                if (day.getCalendarId() == dayOfWeek) {
                    return day;
                }
            }
        }
        return Day.values()[column];
    }

    private Lesson findLesson(final Day day, final Time time) {
        for (Lesson lesson : mData) {
            if (day == lesson.getDay() && time == lesson.getTime()) {
                return lesson;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return (TIME_COLUMN + mNumberOfDays) * (DAY_ROW + Time.values().length);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.cell)
        LinearLayout mCell;
        @Bind(R.id.textSubject)
        TextView mSubject;
        @Bind(R.id.textRoom)
        TextView mRoom;
        @Bind(R.id.textInfo)
        TextView mInfo;
        private Day mDay;
        private Time mTime;
        private Lesson mLesson;
        private OnItemClickListener mListener;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.cell, R.id.textSubject, R.id.textRoom, R.id.textInfo})
        public void onClick() {
            if (mListener != null) {
                if (mLesson != null) {
                    mListener.onItemClicked(mLesson);
                } else {
                    mListener.onEmptyClicked(mDay, mTime);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(@NonNull final Lesson lesson);

        void onEmptyClicked(@NonNull final Day day, @NonNull final Time time);
    }
}
