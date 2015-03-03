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
import android.widget.EditText;
import android.widget.TextView;

import com.fk07.R;
import edu.hm.cs.fs.app.ui.timetable.TimetableDayActivity;
import edu.hm.cs.fs.app.ui.timetable.TimetableWeekActivity;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Entry;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Time;

/**
 * This Dialog is for Editing an Entry.
 * 
 * @author Rene
 */
public class EditDialog {
	
	/**
	 * This Dialog.
	 */
	private final Dialog dialog;
	
	/**
	 * Constructor.
	 * 
	 * @param activity The current ACtivity.
	 * @param time
	 * @param entry Current Entry.
	 * @param position
	 * @param isAdding True, if a new Entry should be created. False, if the current Entry should be edited.
	 */
	public EditDialog(final Activity activity, final Time time, final Entry entry, final int position, final boolean isAdding) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		LayoutInflater inflater = activity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.timetable_dialog_edit, null);
        
        TextView header = (TextView) layout.findViewById(R.id.header);
        final EditText room = (EditText) layout.findViewById(R.id.editRoom);
		final EditText title = (EditText) layout.findViewById(R.id.editTitel);
		final EditText teacher = (EditText) layout.findViewById(R.id.editProf);
		final EditText suffix = (EditText) layout.findViewById(R.id.editSuffix);
        
        if (!isAdding) {
			header.setText(R.string.edit);
			room.setText(entry.getRoom());
			title.setText(entry.getTitle());
			
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
			suffix.setText(entry.getSuffix());
		} else {
			header.setText(R.string.add);
		}
        
        Button positive = (Button) layout.findViewById(R.id.positiveButton);
        Button negative = (Button) layout.findViewById(R.id.negativeButton);
        
        positive.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				List<String> rooms = new ArrayList<String>();
				rooms.add(room.getText().toString());
				List<String> teachers = new ArrayList<String>();
				teachers.add(teacher.getText().toString());
				if (isAdding) {
					Entry newEntry = new Entry();
					newEntry.setRoom(rooms);
					String[] startTime = activity.getResources().getStringArray(R.array.starttimes);
					String[] endTime = activity.getResources().getStringArray(R.array.endtimes);
					newEntry.setStartTime(startTime[position]);
					newEntry.setStopTime(endTime[position]);
					newEntry.setSuffix(suffix.getText().toString());
					newEntry.setTitle(title.getText().toString());
					newEntry.setTeacher(teachers);
					newEntry.setType("course");
					time.getEntry().add(newEntry);
				} else {
					entry.setRoom(rooms);
					entry.setSuffix(suffix.getText().toString());
					entry.setTitle(title.getText().toString());
					entry.setTeacher(teachers);
				}
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
