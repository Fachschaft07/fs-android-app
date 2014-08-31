package com.fk07;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fk07.backend.utils.NetworkUtils;
import com.fk07.bb.BBActivity;
import com.fk07.info.InfoActivity;
import com.fk07.mensa.MensaActivity;
import com.fk07.mvv.MvvActivity;
import com.fk07.news.NewsActivity;
import com.fk07.presence.PresenceActivity;
import com.fk07.presence.PresenceHandler;
import com.fk07.rooms.RoomsActivity;
import com.fk07.timetable.TimetableDayActivity;
import com.fk07.timetable.TimetableWeekActivity;

/**
 * Die Fachschaftsapp Main Activity
 * 
 * 
 * @author Alex
 * 
 */
public class Fachschaft07 extends Activity {
	
	private Menu menu;
	/**
	 * Wird bei erstem Starten der Activity aufgerufen
	 * 
	 * Generiert die Tabs und referenziert die Activities die zu den Tabs geh�ren.
	 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		loadHotNews(new View(this));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadPresence();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		final Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_info:
			intent = new Intent(this, InfoActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_presence:
			if (NetworkUtils.isConnected(this)) {
				intent = new Intent(this, PresenceActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.actionmenu, menu);
		return true;
	}

	public void startModule(final View view) {
		final Intent intent;
		switch (view.getId()) {
			case R.id.newsButton:
				if (Build.VERSION.SDK_INT >= 11) {
					intent = new Intent(this, NewsActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(this, R.string.apiFail, Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.bbButton:
				intent = new Intent(this, BBActivity.class);
				startActivity(intent);
				break;

			case R.id.timetableButton:
				final SharedPreferences timetablePreferences = getSharedPreferences("Timetable", 0);
				if (timetablePreferences.getBoolean("isDay", true)) {
					intent = new Intent(this, TimetableDayActivity.class);
				} else {
					intent = new Intent(this, TimetableWeekActivity.class);
				}
				startActivity(intent);
				break;

			case R.id.roomsButton:
				if (Build.VERSION.SDK_INT >= 11) {
					intent = new Intent(this, RoomsActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(this, R.string.apiFail, Toast.LENGTH_SHORT).show();
				}
				
				break;

			case R.id.mvvButton:
				if (Build.VERSION.SDK_INT >= 11) {
					intent = new Intent(this, MvvActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(this, R.string.apiFail, Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.foodButton:
				if (Build.VERSION.SDK_INT >= 11) {
					intent = new Intent(this, MensaActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(this, R.string.apiFail, Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	public void loadHotNews(final View view) {
		new DownloadHotNews().execute();
	}

	private void setHotNews(final String news) {
		final TextView tv = (TextView) findViewById(R.id.textViewHotNews);
		try {
			final Pattern p = Pattern.compile("(\\d)(\\s)(.*)");
			final Matcher m = p.matcher(news);

			if (m.matches()) {
				tv.setText(m.group(3));

				switch (Integer.parseInt(m.group(1))) {
					case 1:
						tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hotnews_1, 0, 0, 0);
						break;
					case 2:
						tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hotnews_2, 0, 0, 0);
						break;
					case 3:
					case 4:
						tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hotnews_3, 0, 0, 0);
						break;
					default:
						break;
				}
			} else {
				tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hotnews_3, 0, 0, 0);
				tv.setText("21 ist nur die halbe Wahrheit");
			}
		} catch (final NumberFormatException e) {
			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hotnews_1, 0, 0, 0);
			tv.setText("4242424242");
		}
	}

	private class DownloadHotNews extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected void onPostExecute(final String content) {
			setHotNews(content);
		}

		@Override
		protected String doInBackground(final String... params) {
			String line = "";
			try {
				final URL url = new URL("http://fs.cs.hm.edu/index.php?option=com_fsappnews&format=raw");
				final URLConnection con = url.openConnection();
				final BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
				line = read.readLine();
			} catch (final IOException e) {
				line = "3 Leider konnte keine Verbindung hergestellt werden";
				e.printStackTrace();
			}

			return line;
		}
	}
	
	private void loadPresence() {
		if (NetworkUtils.isConnected(this)) {
			DownloadPresenceTask presenceTask = new DownloadPresenceTask();
			presenceTask.execute();
		}
	}
	
	private void updatePresenceItem(final Map<String, String> presenceMap) {
		runOnUiThread(new Runnable() {
			public void run() {
				MenuItem presenceItem = menu.findItem(R.id.menu_presence);
				boolean present = false;
				for (String name : presenceMap.keySet()) {
					if (!presenceMap.get(name).equals("Busy")) {
						present = true;
					}
				}
				if (!present) {
					presenceItem.setIcon(R.drawable.circle_yellow_light);
				} else {
					presenceItem.setIcon(R.drawable.circle_green_dark);
				}
			}
		});
	}

	private class DownloadPresenceTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			PresenceHandler presenceHandler = new PresenceHandler();
			updatePresenceItem(presenceHandler.downloadPresence());
			return null;
		}
	}
}
