package com.fk07.rooms;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.fk07.R;
import com.fk07.backend.parser.RoomParser;
import com.fk07.backend.utils.NetworkUtils;
import com.fk07.backend.utils.Urls;
import com.fk07.info.InfoActivity;

/**
 * @author Fabio
 * 
 */
public class RoomsActivity extends Activity {
	private RoomsAdapter adapter;
	private Spinner days;
	private Spinner times;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rooms);

		final ListView resultList = (ListView) findViewById(R.id.roomsList);
		adapter = new RoomsAdapter(this);
		resultList.setAdapter(adapter);

		days = (Spinner) findViewById(R.id.spinnerDays);
		days.setAdapter(ArrayAdapter.createFromResource(this, R.array.days, R.layout.rooms_spinnerline));
		days.setSelection(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);

		times = (Spinner) findViewById(R.id.spinnerTimes);
		times.setAdapter(ArrayAdapter.createFromResource(this, R.array.starttimes, R.layout.rooms_spinnerline));
		int index = getTimeIndex(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE));
		times.setSelection(index);

		final Button buttonFindRooms = (Button) findViewById(R.id.buttonFindRooms);
		
		buttonFindRooms.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				searchRooms(false);
				
			}
		});

		searchRooms(true);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;

			case R.id.menu_info:
				startActivity(new Intent(this, InfoActivity.class));
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void searchRooms(final boolean now) {
		final String day = days.getSelectedItem().toString().toLowerCase().substring(0, 2);
		final String time;
		if (now) {
			final DateFormat formatter = DateFormat.getTimeInstance(DateFormat.SHORT);
			time = formatter.format(new Date());
		} else {
			time = times.getSelectedItem().toString();
		}
		final String path = Urls.ROOMS + day + "&clockTime=" + time;

		if (NetworkUtils.isConnected(RoomsActivity.this)) {
			new RoomParser(RoomsActivity.this, adapter).execute(path);
		} else {
			Toast.makeText(RoomsActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
		}
	}
	
	private int getTimeIndex(final int hour, final int minute) {
		
		if (hour < 10) { // - 10:00
			return 0;
		} else if (hour < 11 || (hour < 12 && minute < 45)) { // - 11:45
			return 1;
		} else if (hour < 13 || (hour < 14 && minute < 30)) { // - 13:30
			return 2;
		} else if (hour < 15 || (hour < 16 && minute < 15)) { // - 15:15
			return 3;
		} else if (hour < 17) { // -17:00
			return 4;
		} else if (hour < 18 || (hour < 19 && minute < 45)) { // 16:15 - 17:59
			return 5;
		} else if (hour < 20 || (hour < 21 && minute < 15)) { // 18:00 - 19:59
			return 6;
		} else {
			return 0;
		}
	}
}
