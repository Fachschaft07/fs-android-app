package com.fk07.timetable.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fk07.R;
import com.fk07.timetable.xml.groups.FK07Group;

/**
 * The Adapter that builds each Row of the DownloadDialog.
 * 
 * @author Rene
 */
public class DownloadAdapter extends ArrayAdapter<FK07Group> {
	
	/**
	 * Current Activity Context.
	 */
	private final Context context;
	
	/**
	 * Contains all courses that will be displayed in the ListView.
	 */
	private final List<FK07Group> groups;
	
	/**
	 * This is the Map for the RadioButtons. It contains the Key if the RadioButton is selected.
	 */
	private final Map<String, Boolean> groupMap = new HashMap<String, Boolean>();
	
	/**
	 * The RadioButton
	 */
	private RadioButton selectedButton;
	
	/**
	 * Currently selected Course.
	 */
	private FK07Group selectedGroup;

	public DownloadAdapter(final Context context, final int textViewResourceId, final List<FK07Group> groups) {
		super(context, textViewResourceId, groups);
		this.context = context;
		this.groups = groups;
		for (FK07Group group : groups) {
			groupMap.put(group.toString(), false);
		}
	}
	
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			view = inflater.inflate(R.layout.timetable_dialog_download_row, parent, false);
		}
		
		final FK07Group group = groups.get(position);
		
		final TextView courseView = (TextView) view.findViewById(R.id.textCourse);
		courseView.setText(group.toString());
		
		final RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton);
		radioButton.setChecked(groupMap.get(group.toString()));
		radioButton.setTag(group.toString());
		
		view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	groupMap.put(group.toString(), true);
            	if (selectedGroup != null) {
            		groupMap.put(selectedGroup.toString(), false);
				}
            	radioButton.setChecked(true);
            	selectedGroup = group;
            	if (selectedButton != null && !group.toString().equals(selectedButton.getTag())) {
					selectedButton.setChecked(false);
				}
            	selectedButton = radioButton;
            }
        });
		
		radioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	groupMap.put(group.toString(), true);
            	if (selectedGroup != null) {
            		groupMap.put(selectedGroup.toString(), false);
				}
            	radioButton.setChecked(true);
            	selectedGroup = group;
            	if (selectedButton != null && !group.toString().equals(selectedButton.getTag())) {
					selectedButton.setChecked(false);
				}
            	selectedButton = radioButton;
            }
        });
		return view;
	}
	
	/**
	 * @return The selctedCourse.
	 */
	public FK07Group getSelectedCourse() {
		return selectedGroup;
	}
}
