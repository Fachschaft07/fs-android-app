package edu.hm.cs.fs.app.ui.timetable.dialogs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.fk07.R;
import edu.hm.cs.fs.app.ui.timetable.TimetableDayActivity;
import edu.hm.cs.fs.app.ui.timetable.TimetableWeekActivity;
import edu.hm.cs.fs.app.ui.timetable.xml.groups.FK07Group;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Timetable;

/**
 * This is the Dialog that contains all Courses. The USer can select on Course. 
 * The Selected Course will be downloaded.
 * 
 * @author Rene
 */
public class DownloadDialog {
	
	/**
	 * Contains all courses.
	 */
	private final String[] courses;
	
	/**
	 * The courseList.
	 */
	private final ArrayList<String> coursesList;
	
	/**
	 * The current Dialog.
	 */
	private Dialog dialog;
	
	/**
	 * The Builder for this Dialog.
	 */
	final AlertDialog.Builder builder;
	
	/**
	 * Constructor.
	 * 
	 * @param activity Current Activity.
	 * @param timetable Current Timetable.
	 */
	public DownloadDialog(final Activity activity, final Timetable timetable, final List<FK07Group> groups) {
		courses = activity.getResources().getStringArray(R.array.list);
		coursesList = new ArrayList<String>();
		
		final boolean isNew = timetable == null;
		
		for (int i = 0; i < courses.length; i++) {
			coursesList.add(courses[i]);
		}
		
		builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = activity.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.timetable_dialog_download, null);
        
        ListView lv = (ListView) layout.findViewById(R.id.listCourses);
        final DownloadAdapter downloadAdapter = new DownloadAdapter(activity, R.layout.timetable_dialog_download_row, groups);
        lv.setAdapter(downloadAdapter);
        lv.setSelector(R.drawable.holo_light_red_list_selector_holo_light);
        
        Button positive = (Button) layout.findViewById(R.id.positiveButton);
        Button neutral = (Button) layout.findViewById(R.id.neutralButton);
        Button negative = (Button) layout.findViewById(R.id.negativeButton);
        
        positive.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FK07Group selectedGroup = downloadAdapter.getSelectedCourse();
				if (selectedGroup != null) {
					SharedPreferences timetablePreferences = activity.getSharedPreferences("Timetable", 0);
					if (timetablePreferences.getBoolean("isDay", true)) {
						((TimetableDayActivity) activity).onCourseSelected(selectedGroup.getDownloadTag(), isNew);
					} else {
						((TimetableWeekActivity) activity).onCourseSelected(selectedGroup.getDownloadTag(), isNew);
					}
				}
				dialog.dismiss();
			}
		});
        
        neutral.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FK07Group selectedGroup = downloadAdapter.getSelectedCourse();
				SharedPreferences timetablePreferences = activity.getSharedPreferences("Timetable", 0);
				if (selectedGroup != null) {
					if (timetablePreferences.getBoolean("isDay", true)) {
						((TimetableDayActivity) activity).onCourseSelected(selectedGroup.getDownloadTag(), true);
					} else {
						((TimetableWeekActivity) activity).onCourseSelected(selectedGroup.getDownloadTag(), true);
					}
				}
				dialog.dismiss();
			}
		});
        
        negative.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        
        builder.setView(layout);
        
        dialog = builder.create();
	}
	
	/**
	 * Show this Dialog.
	 */
	public void show() {
		dialog.show();
	}
}
