package edu.hm.cs.fs.app.ui.timetable.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fk07.R;
import edu.hm.cs.fs.app.ui.timetable.dialogs.CourseDialog;
import edu.hm.cs.fs.app.ui.timetable.dialogs.EditDialog;
import edu.hm.cs.fs.app.ui.timetable.dialogs.ModifyDialog;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Entry;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Time;

/**
 * Builds each row of the Listview.
 * Used for the TimetableDayActivity to Display all Lectures in the current ListView.
 * 
 * @author Rene
 */
public class DayAdapter extends BaseAdapter {

	/**
	 * Current Activity (TimetableDayActivity). 
	 */
	private final Activity activity;
	
	/**
	 * Contains all Times for the ListView.
	 */
	private final List<Time> times;

	/**
	 * Constructor.
	 * 
	 * @param activity
	 * @param times
	 */
	public DayAdapter(final Activity activity, final List<Time> times) {
		this.activity = activity;
		this.times = times;
	}

	/**
	 * Returns the size of the times.
	 */
	public int getCount() {
		return times.size();
	}

	/**
	 * Get Current Time Object for the current Row.
	 */
	public Object getItem(final int position) {
		return times.get(position);
	}

	/**
	 * Returns current Position.
	 */
	public long getItemId(final int position) {
		return position;
	}

	/**
	 * Builds the Current Row.
	 */
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		
		final Time time = times.get(position);
		boolean hasCourse = !time.getEntry().isEmpty();
		
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			
			if (!hasCourse) {
				view = inflater.inflate(R.layout.timetable_listrow_empty, null);
				view.setOnLongClickListener(new OnLongClickListener() {
					
					public boolean onLongClick(View v) {
						EditDialog editDialog = new EditDialog(activity, time, null, position, true);
						editDialog.show();
						return false;
					}
				});
			} else {
				final Entry entry = time.getEntry().get(time.getCurrentEntry());
				
				view = inflater.inflate(R.layout.timetable_listrow_day, null);
				final TextView course = (TextView) view.findViewById(R.id.course);
				course.setText(entry.getTitle());
				
				if (time.getEntry().size() > 1) {
					final TextView courseCount = (TextView) view.findViewById(R.id.courseCount);
					courseCount.setText("(Kurse: " + time.getEntry().size() + ")");
				}
				
				final TextView roomTV = (TextView) view.findViewById(R.id.room);
				roomTV.setText(entry.getRoom());
				
				view.setOnLongClickListener(new OnLongClickListener() {
					
					public boolean onLongClick(View v) {
						ModifyDialog modifyDialog = new ModifyDialog(activity, time, entry, position);
						modifyDialog.show();
						return false;
					}
				});
				
				view.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						CourseDialog courseDialog = new CourseDialog(activity, time, position);
						courseDialog.show();
					}
				});
			}
		}

		final TextView timeTextView = (TextView) view.findViewById(R.id.timeFrom);
		
		if (hasCourse && time.getEntry().get(time.getCurrentEntry()).getType().equals("FK10")) {
			timeTextView.setText(time.getEntry().get(time.getCurrentEntry()).getStartTime());
		} else {
			timeTextView.setText(getTime(position));
		}
		
		return view;
	}
	
	/**
	 * Get the Time for the Current Row.
	 * 
	 * @param position Current Row.
	 * @return The Time as String for the current Row.
	 */
	public String getTime(final int position) {
		switch (position) {
		case 0:
			return "08:15-09:45";
		case 1:
			return "10:00-11:30";
		case 2:
			return "11:45-13:15";
		case 3:
			return "13:30-15:00";
		case 4:
			return "15:15-16:45";
		case 5:
			return "17:00-18:30";
		case 6:
			return "18:45-20:15";
		default:
			return "00:00";
		}
	}
}
