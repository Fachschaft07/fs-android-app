package edu.hm.cs.fs.app.ui.timetable.dialogs;

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
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Timetable;
import edu.hm.cs.fs.app.ui.timetable.xml.timetablefk10.FK10Group;

/**
 * This is the Dialog that contains all Courses. The User can select one Course. 
 * The Selected Course will be downloaded.
 * 
 * @author Rene
 */
public class DownloadFK10Dialog {
	
	/**
	 * The Dialog Object.
	 */
	private Dialog dialog;
	
	/**
	 * The Builder for this Dialog.
	 */
	final AlertDialog.Builder builder;
	
	/**
	 * Constructor.
	 * 
	 * @param activity The current Activity.
	 * @param timetable	The current Timetable.
	 * @param groups 
	 */
	public DownloadFK10Dialog(final Activity activity, final Timetable timetable, final List<FK10Group> groups) {
		
		final boolean isNew = timetable == null;
		
		builder = new AlertDialog.Builder(activity);
		LayoutInflater inflater = activity.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.timetable_dialog_download, null);
        
        ListView lv = (ListView) layout.findViewById(R.id.listCourses);
        final DownloadFK10Adapter downloadFK10Adapter = new DownloadFK10Adapter(activity, R.layout.timetable_dialog_download_row, groups);
        lv.setAdapter(downloadFK10Adapter);

        Button positive = (Button) layout.findViewById(R.id.positiveButton);
        Button neutral = (Button) layout.findViewById(R.id.neutralButton);
        Button negative = (Button) layout.findViewById(R.id.negativeButton);
        
        positive.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FK10Group selectedGroup = downloadFK10Adapter.getSelectedGroup();
				if (selectedGroup != null) {
					SharedPreferences timetablePreferences = activity.getSharedPreferences("Timetable", 0);
					if (timetablePreferences.getBoolean("isDay", true)) {
						((TimetableDayActivity) activity).onGroupSelected(selectedGroup, isNew);
					} else {
						((TimetableWeekActivity) activity).onGroupSelected(selectedGroup, isNew);
					}
				}
				dialog.dismiss();
			}
		});
        
        neutral.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FK10Group selectedGroup = downloadFK10Adapter.getSelectedGroup();
				SharedPreferences timetablePreferences = activity.getSharedPreferences("Timetable", 0);
				if (selectedGroup != null) {
					if (timetablePreferences.getBoolean("isDay", true)) {
						((TimetableDayActivity) activity).onGroupSelected(selectedGroup, true);
					} else {
						((TimetableWeekActivity) activity).onGroupSelected(selectedGroup, true);
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
