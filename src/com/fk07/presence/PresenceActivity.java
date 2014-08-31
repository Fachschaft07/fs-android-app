package com.fk07.presence;

import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fk07.R;
import com.fk07.backend.utils.NetworkUtils;

public class PresenceActivity extends Activity {

	private final String tag = "PresenceActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_presence);
		downloadPresence();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.actionmenu_presence, menu);
		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_reload:
			downloadPresence();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void downloadPresence() {
		if (NetworkUtils.isConnected(this)) {
			setProgressBarIndeterminateVisibility(true);
			DownloadPresenceTask presenceTask = new DownloadPresenceTask();
			presenceTask.execute();
		} else {
			Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void updatePresence(final Map<String, String> presenceMap) {

		runOnUiThread(new Runnable() {
			public void run() {
				setProgressBarIndeterminateVisibility(false);
				TextView presenceEmptyText = (TextView) findViewById(R.id.presenceEmpty);
				if (!presenceMap.isEmpty()) {
					presenceEmptyText.setVisibility(View.INVISIBLE);
					ListView listView = (ListView) findViewById(R.id.presenceListView);
					PresenceAdapter presenceAdapter = new PresenceAdapter(
							PresenceActivity.this, presenceMap);
					listView.setAdapter(presenceAdapter);
					listView.setDivider(null);
				} else {
					presenceEmptyText.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private class DownloadPresenceTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			PresenceHandler presenceHandler = new PresenceHandler();
			updatePresence(presenceHandler.downloadPresence());
			return null;
		}
	}

}
