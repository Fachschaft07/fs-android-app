package com.fk07.timetable;

import java.io.File;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.fk07.R;
import com.fk07.timetable.adapter.WeekAdapter;
import com.fk07.timetable.xml.TimetableHandler;

/**
 * The TimetableWeekActivity display the Current Timetable as Week View.
 * 
 * @author Rene
 */
public class TimetableWeekActivity extends TimetableActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		menuID = R.menu.actionmenu_timetable_week;
		setContentView(R.layout.timetable_main_week);
		
		otherActivity = TimetableDayActivity.class;
		
		timetableFile = new File(getFilesDir() + TimetableHandler.TIMETABLE_FILE);
		
		if (timetableFile.exists()) {
			timetable = TimetableHandler.readTimetable(timetableFile);
			if (timetable != null) {
				generateViews();
			} else {
				Toast.makeText(this, getString(R.string.loadTimetableError), Toast.LENGTH_LONG).show();
			}
		}
		
		SharedPreferences timetablePreferences = getSharedPreferences("Timetable", 0);
		SharedPreferences.Editor editor = timetablePreferences.edit();
		editor.putBoolean("isDay", false);
		editor.commit();
		
		setProgressBarIndeterminateVisibility(false);
	}
	
	
	
	@Override
	public void generateViews() {
		ListView listView = (ListView) findViewById(R.id.listOverview);
		WeekAdapter overviewAdapter = new WeekAdapter(this, timetable.getDay());
		listView.setAdapter(overviewAdapter);
	}
}
