package com.fk07.timetable.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fk07.R;
import com.fk07.timetable.TimetableDayActivity;
import com.fk07.timetable.TimetableWeekActivity;
import com.fk07.timetable.xml.timetable.Entry;
import com.fk07.timetable.xml.timetable.Time;

/**
 * This Dialog is for asking the User if the selected Entry should be really deleted from the Timetable.
 * 
 * @author Rene
 */
public class DeleteDialog {
	
	/**
	 * This Dialog.
	 */
	private final Dialog dialog;
	
	/**
	 * Construtor.
	 * 
	 * @param activity Current Activity.
	 * @param time The Time Object of that Contains the selected Entry.
	 * @param entry The selected Entry that should be deleted.
	 * @param position The Position of the Timeslot.
	 */
	public DeleteDialog(final Activity activity, final Time time, final Entry entry, final int position) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.timetable_dialog_delete, null);
        
        builder.setView(layout);
        
        Button positive = (Button) layout.findViewById(R.id.positiveButton);
        Button negative = (Button) layout.findViewById(R.id.negativeButton);
        
        positive.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				time.getEntry().remove(time.getCurrentEntry());
				time.setCurrentEntry(0);
				
				SharedPreferences timetablePreferences = activity.getSharedPreferences("Timetable", 0);
				if (timetablePreferences.getBoolean("isDay", true)) {
					((TimetableDayActivity) activity).refreshViews();
				} else {
					((TimetableWeekActivity) activity).refreshViews();
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
