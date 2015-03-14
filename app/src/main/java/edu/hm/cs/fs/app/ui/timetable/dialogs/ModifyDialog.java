package edu.hm.cs.fs.app.ui.timetable.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fk07.R;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Entry;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Time;

/**
 * This Dialog is for choosing if a new Entry should be created,
 * the seleceted Entry should be Edited or Deleted.
 * 
 * @author Rene
 */
public class ModifyDialog {
	
	/**
	 * The Builder for this Dialog.
	 */
	private final AlertDialog.Builder builder;
	
	/**
	 * This Dialog.
	 */
	private final Dialog dialog;

	/**
	 * Constructor. 
	 * 
	 * @param activity
	 * @param time
	 * @param entry The selected Entry.
	 * @param position
	 */
	public ModifyDialog(final Activity activity, final Time time, final Entry entry, final int position) {

		builder = new AlertDialog.Builder(activity);

		LayoutInflater inflater = activity.getLayoutInflater();
		final View layout = inflater.inflate(R.layout.timetable_dialog_modify, null);
		
		final String[] items;
		
		items = new String[] {
				activity.getString(R.string.add),
				activity.getString(R.string.edit),
				activity.getString(R.string.delete)};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.timetable_dialog_modify_row, items);

		ListView lv = (ListView) layout.findViewById(R.id.listView);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					EditDialog addDialog = new EditDialog(activity, time, entry, position, true);
					addDialog.show();
					break;
				case 1:
					EditDialog editDialog = new EditDialog(activity, time, entry, position, false);
					editDialog.show();
					break;
				case 2:
					DeleteDialog deleteDialog = new DeleteDialog(activity, time, entry, position);
					deleteDialog.show();
					break;
				}
				dialog.dismiss();
			}
		});

		builder.setView(layout);
		dialog = builder.create();
	}

	/**
	 * Show thsi Dialog.
	 */
	public void show() {
		dialog.show();
	}
}
