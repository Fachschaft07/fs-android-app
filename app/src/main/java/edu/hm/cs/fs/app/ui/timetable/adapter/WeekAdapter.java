package edu.hm.cs.fs.app.ui.timetable.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fk07.R;
import edu.hm.cs.fs.app.ui.timetable.dialogs.CourseDialog;
import edu.hm.cs.fs.app.ui.timetable.dialogs.EditDialog;
import edu.hm.cs.fs.app.ui.timetable.dialogs.ModifyDialog;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Day;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Entry;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Time;

/**
 * The Adapter for the ListView of the TimetableWeekActivity.
 * 
 * @author Rene
 */
public class WeekAdapter extends BaseAdapter {

	/**
	 * The courseView from layout/timetable_listrow_week
	 */
	private final int[] courseViews = {R.id.courseMon, R.id.courseTue, R.id.courseWed, R.id.courseThu, R.id.courseFri};
	
	/**
	 * The linearView from layout/timetable_listrow_week
	 */
	private final int[] linearViews = {R.id.linearMon, R.id.linearTue, R.id.linearWed, R.id.linearThu, R.id.linearFri};
	
	/**
	 * Current Activity.
	 */
	private final Activity activity;
	
	/**
	 * All days from current Timetable.
	 */
	private final List<Day> days;

	/**
	 * Constructor.
	 * 
	 * @param activity TimetableWeekActivity.
	 * @param days All days from current Timetable.
	 */
	public WeekAdapter(final Activity activity, final List<Day> days) {
		this.activity = activity;
		this.days = days;
	}

	/**
	 * Number of days.
	 */
	public int getCount() {
		return days.size();
	}

	/**
	 * @return The Day for the given position.
	 */
	public Object getItem(final int position) {
		return days.get(position);
	}

	/**
	 * @return Current position.
	 */
	public long getItemId(final int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View view = convertView;
		
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			view = inflater.inflate(R.layout.timetable_listrow_week, null);
			
			LinearLayout linearLayout;
			TextView course;
			
			for (int i = 0; i < 5; i++) {
				course = (TextView) view.findViewById(courseViews[i]);
				final Time time = days.get(i).getTime().get(position);
				linearLayout = (LinearLayout) view.findViewById(linearViews[i]);
				
				if (time.getEntry().isEmpty()) {
					linearLayout.setOnLongClickListener(new OnLongClickListener() {
						public boolean onLongClick(View v) {
							EditDialog editDialog = new EditDialog(activity, time, null, position, true);
							editDialog.show();
							return false;
						}
					});
				} else {
					final Entry entry = time.getEntry().get(time.getCurrentEntry());
					course.setText(entry.getTitle());
					
					linearLayout.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.list_selector));
					
					linearLayout.setOnLongClickListener(new OnLongClickListener() {
						
						public boolean onLongClick(View v) {
							ModifyDialog modifyDialog = new ModifyDialog(activity, time, entry, position);
							modifyDialog.show();
							return false;
						}
					});
					
					linearLayout.setOnClickListener(new OnClickListener() {
						
						public void onClick(View v) {
							CourseDialog courseDialog = new CourseDialog(activity, time, position);
							courseDialog.show();
						}
					});
				}
			}
		}
		return view;
	}
}
