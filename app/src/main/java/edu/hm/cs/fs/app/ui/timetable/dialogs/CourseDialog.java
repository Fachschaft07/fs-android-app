package edu.hm.cs.fs.app.ui.timetable.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.fk07.R;
import edu.hm.cs.fs.app.ui.timetable.TimetableDayActivity;
import edu.hm.cs.fs.app.ui.timetable.TimetableWeekActivity;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Time;

/**
 * The Dialog that displays all Courses for a Timeslot. The User can select a Course
 * from this Dialog that will be displayed.
 * 
 * @author Rene
 */
public class CourseDialog {
	
	/**
	 * This Dialog.
	 */
	private final Dialog dialog;
	
	/**
	 * Constructor.
	 * The Dialog will be Build here and can later be shown with show().
	 * 
	 * @param activity Current Activity.
	 * @param time The Time Object for the TimeSlot.
	 * @param position The Position of the TimeSlot.
	 */
	public CourseDialog(final Activity activity, final Time time, final int position) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.timetable_dialog_courses, null);
        
        TextView header = (TextView) layout.findViewById(R.id.header);
        header.setText(getTime(position));
        
        ListView listView = (ListView) layout.findViewById(R.id.listView);
        CourseDialogAdapter courseDialogAdapter = new CourseDialogAdapter(activity, time);
        listView.setAdapter(courseDialogAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int entry, long id) {
				time.setCurrentEntry(entry);
				SharedPreferences timetablePreferences = activity.getSharedPreferences("Timetable", 0);
				if (timetablePreferences.getBoolean("isDay", true)) {
					((TimetableDayActivity) activity).refreshViews();
				} else {
					((TimetableWeekActivity) activity).refreshViews();
				}
				dialog.dismiss();
			}
		});
        
		builder.setView(layout);
		dialog = builder.create();
	}
	
	/**
	 * Display this Dialog.
	 */
	public void show() {
		dialog.show();
	}

	/**
	 * Returns the Time for this Timeslot that will be shown at the Header.
	 * 
	 * @param position Position of the Timeslot.
	 * @return The Time of the Position.
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
