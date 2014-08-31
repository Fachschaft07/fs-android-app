package com.fk07.timetable.dialogs;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fk07.R;
import com.fk07.timetable.xml.timetable.Entry;
import com.fk07.timetable.xml.timetable.Time;

/**
 * The Adapter for the CurseDialog.
 * 
 * @author Rene
 *
 */
public class CourseDialogAdapter extends BaseAdapter {
	
	/**
	 * Current Activity.
	 */
	final Activity activity;
	
	/**
	 * Time Object of this Timeslot.
	 */
	final Time time;
	
	/**
	 * Constructor.
	 * 
	 * @param activity
	 * @param time
	 */
	public CourseDialogAdapter(final Activity activity, final Time time) {
		this.activity = activity;
		this.time = time;
	}

	/**
	 * @return Number of Entries of the Time Object.
	 */
	public int getCount() {
		return time.getEntry().size();
	}

	/**
	 * @return Entry of the given Position.
	 */
	public Object getItem(int position) {
		return time.getEntry().get(position);
	}

	/**
	 * @return Current Position.
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * Builds a Row of the ListView.
	 */
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		
		View view = convertView;
		final Entry entry = time.getEntry().get(position);
		
		if (view == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			view = inflater.inflate(R.layout.timetable_dialog_courses_row, null);
		}
		
		final TextView title = (TextView) view.findViewById(R.id.course);
		title.setText(entry.getTitle());
		
		final TextView teacher = (TextView) view.findViewById(R.id.teacher);
		List<String> teacherList = entry.getTeacher();
		String teacherText = "";
		for (int i = 0; i < teacherList.size(); i++) {
			if (i == 0) {
				teacherText += teacherList.get(i);
			} else {
				teacherText += ", " + teacherList.get(i);
			}
		}
		
		teacher.setText(teacherText);
		
		final TextView room = (TextView) view.findViewById(R.id.roomText);
		room.setText(entry.getRoom());
		
		final TextView suffix = (TextView) view.findViewById(R.id.suffix);
		suffix.setText(entry.getSuffix());
		
		return view;
	}
}
