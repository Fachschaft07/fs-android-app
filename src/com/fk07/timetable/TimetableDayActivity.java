package com.fk07.timetable;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.fk07.R;
import com.fk07.timetable.adapter.DayAdapter;
import com.fk07.timetable.adapter.PageAdapter;
import com.fk07.timetable.xml.TimetableHandler;

/**
 * The TimetableDayActivity display the Current Timetable as Day View.
 * 
 * @author Rene
 */
public class TimetableDayActivity extends TimetableActivity {
	
	/**
	 * Current Page Index. (0=Monday, 1=Tuesday, 2=Wednesday, 3=Thursday, 4=Friday)
	 */
	private int currentPage = 0;
	
	/**
	 * Name of the Week that is Displayed on the Top of the Page.
	 */
	private final String[] weekDays = new String[] { "MO", "DI", "MI", "DO", "FR" };
	
	/**
	 * ListView of each Page that Contains the Lectures.
	 */
	private List<ListView> listViewList = new ArrayList<ListView>();
	
	/**
	 * Adapter for the Pages.
	 */
	private PageAdapter pagerAdapter;
	
	/**
	 * The View of a Page.
	 */
	private ViewPager viewPager;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		menuID = R.menu.actionmenu_timetable_day;
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		timetableFile = new File(getFilesDir() + TimetableHandler.TIMETABLE_FILE);
		otherActivity = TimetableWeekActivity.class;
		Calendar calendar = Calendar.getInstance();
		
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		currentPage = (weekday == 1 || weekday == 7)? 0 : weekday - 2;
		
		if (timetableFile.exists()) {
			timetable = TimetableHandler.readTimetable(timetableFile);
			generateViews();
		} else {
			setContentView(R.layout.timetable_tutorial);
		}
		SharedPreferences timetablePreferences = getSharedPreferences("Timetable", 0);
		SharedPreferences.Editor editor = timetablePreferences.edit();
		editor.putBoolean("isDay", true);
		editor.commit();
		setProgressBarIndeterminateVisibility(false);
	}
	
	@Override
	public void generateViews() {
		final LayoutInflater inflater = getLayoutInflater();
		final List<View> pages = new ArrayList<View>();

		for (int i = 0; i < weekDays.length; i++) {
			final View page = inflater.inflate(R.layout.timetable_main_day, null);

			final TextView tv = (TextView) page.findViewById(R.id.weekDay);
			tv.setText(weekDays[i]);

			final TextView course = (TextView) page.findViewById(R.id.course);
			List<String> values = timetable.getValues();
			String value = "";
			for (int j = 0; j < values.size(); j++) {
				if (j == 0) {
					value += values.get(j);
				} else {
					value += ", " + values.get(j);
				}
			}
			course.setText(value);
			
			final DayAdapter adapter = new DayAdapter(this, timetable.getDay().get(i).getTime());
			final ListView listView = (ListView) page.findViewById(R.id.listView);
			listViewList.add(listView);
			listView.setTag(weekDays[i]);
			listView.setAdapter(adapter);
			pages.add(page);
		}

		pagerAdapter = new PageAdapter(pages);
		viewPager = new ViewPager(this);
		viewPager.setOverScrollMode(2);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				currentPage = position;
			}
			
			public void onPageScrollStateChanged(int arg0) {
			}
			public void onPageSelected(int arg0) {
			}
		});

		viewPager.setCurrentItem(currentPage);
		setContentView(viewPager);
	}
}
