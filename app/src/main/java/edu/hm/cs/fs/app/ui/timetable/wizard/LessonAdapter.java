package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Fabio on 31.03.2015.
 */
public class LessonAdapter extends ArrayAdapter<Lesson> implements StickyListHeadersAdapter {
    private List<Lesson> mSelectedLessons = new ArrayList<>();

    public LessonAdapter(final Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_wizard_lesson, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Lesson item = getItem(position);

        holder.moduleTitle.setText(item.getModule().getName());
        holder.time.setText(String.format(Locale.getDefault(), "%1$tH:%1$tM - %2$tH:%2$tM", item.getTime().getStart(), item.getTime().getEnd()));
        if(TextUtils.isEmpty(item.getSuffix())) {
            holder.suffix.setVisibility(View.GONE);
        } else {
            holder.suffix.setVisibility(View.VISIBLE);
            holder.suffix.setText(item.getSuffix());
        }
        holder.checkBox.setChecked(mSelectedLessons.contains(item));

        return convertView;
    }

    @Override
    public View getHeaderView(final int position, View convertView, final ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_sticky_header, viewGroup, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.text.setText(String.format(Locale.getDefault(), "%1$tA", getDate(getItem(position))));

        return convertView;
    }

    @Override
    public long getHeaderId(final int position) {
        return String.format(Locale.getDefault(), "%1$tA", getDate(getItem(position))).hashCode();
    }

    private Calendar getDate(Lesson lesson) {
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 2, 30); // This is a monday

        int weekDay = lesson.getDay().getId();
        switch (weekDay) {
            case Calendar.TUESDAY:
                cal.add(Calendar.DATE, 1);
                break;
            case Calendar.WEDNESDAY:
                cal.add(Calendar.DATE, 2);
                break;
            case Calendar.THURSDAY:
                cal.add(Calendar.DATE, 3);
                break;
            case Calendar.FRIDAY:
                cal.add(Calendar.DATE, 4);
                break;
            case Calendar.SATURDAY:
                cal.add(Calendar.DATE, 5);
                break;
            case Calendar.SUNDAY:
                cal.add(Calendar.DATE, 6);
                break;
        }
        return cal;
    }

    public void setSelections(final List<Lesson> selectedLessons) {
        mSelectedLessons = selectedLessons;
    }

    static class HeaderViewHolder {
        @InjectView(android.R.id.text1)
        TextView text;

        public HeaderViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolder {
        @InjectView(R.id.moduleTitle)
        TextView moduleTitle;
        @InjectView(R.id.time)
        TextView time;
        @InjectView(R.id.suffix)
        TextView suffix;
        @InjectView(R.id.checkBox)
        CheckBox checkBox;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
