package edu.hm.cs.fs.app.ui.timetable.dialogs;

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
import edu.hm.cs.fs.app.ui.timetable.xml.timetablefk10.FK10Group;

/**
 * The Adapter of the DownloadFK10Dialog.
 * 
 * @author Rene
 */
public class DownloadFK10Adapter extends ArrayAdapter<FK10Group> {

	/**
	 * The current Activity Context.
	 */
	private final Context context;
	
	/**
	 * The List of all FK10Groups.
	 */
	private final List<FK10Group> groups;
	
	/**
	 * This is the Map for the RadioButtons. It contains the Key if the RadioButton is selected.
	 */
	private final Map<String, Boolean> groupMap = new HashMap<String, Boolean>();
	
	/**
	 * Currently Selected Button.
	 */
	private RadioButton selectedButton;
	
	/**
	 * The selected Group.
	 */
	private FK10Group selectedGroup;

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param groups
	 */
	public DownloadFK10Adapter(final Context context, final int textViewResourceId, final List<FK10Group> groups) {
		super(context, textViewResourceId, groups);
		this.context = context;
		this.groups = groups;
		for (FK10Group group : groups) {
			groupMap.put(group.getGroupName(), false);
		}
	}
	
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		
		View view = convertView;
		
		if (view == null) {
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			view = inflater.inflate(R.layout.timetable_dialog_download_row, parent, false);
		}
		
		final FK10Group group = groups.get(position);
		
		final TextView courseView = (TextView) view.findViewById(R.id.textCourse);
		courseView.setText(group.getGroupName());
		
		final RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton);
		radioButton.setChecked(groupMap.get(group.getGroupName()));
		radioButton.setTag(group.getGroupName());
		
		view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	groupMap.put(group.getGroupName(), true);
            	if (selectedGroup != null) {
            		groupMap.put(selectedGroup.getGroupName(), false);
				}
            	radioButton.setChecked(true);
            	selectedGroup = group;
            	if (selectedButton != null && !group.getGroupName().equals(selectedButton.getTag())) {
					selectedButton.setChecked(false);
				}
            	selectedButton = radioButton;
            }
        });
		
		radioButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	groupMap.put(group.getGroupName(), true);
            	if (selectedGroup != null) {
            		groupMap.put(selectedGroup.getGroupName(), false);
				}
            	radioButton.setChecked(true);
            	selectedGroup = group;
            	if (selectedButton != null && !group.getGroupName().equals(selectedButton.getTag())) {
					selectedButton.setChecked(false);
				}
            	selectedButton = radioButton;
            }
        });
		return view;
	}
	
	/**
	 * @return The selected Group.
	 */
	public FK10Group getSelectedGroup() {
		return selectedGroup;
	}
}
