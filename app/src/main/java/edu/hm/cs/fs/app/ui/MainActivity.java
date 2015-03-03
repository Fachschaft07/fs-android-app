package edu.hm.cs.fs.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fk07.R;

import java.util.List;

import edu.hm.cs.fs.app.datastore.model.helper.Callback;
import edu.hm.cs.fs.app.datastore.web.PresenceFetcher;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardFragment;
import edu.hm.cs.fs.app.ui.info.InfoActivity;
import edu.hm.cs.fs.app.ui.presence.PresenceActivity;
import edu.hm.cs.fs.app.util.NetworkUtils;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class MainActivity extends MaterialNavigationDrawer<Fragment> {
	@Override
	public void init(final Bundle bundle) {
		addSection(newSection(getString(R.string.blackboard), new BlackBoardFragment()));
		addSection(newSection(getString(R.string.news), new Fragment()));
		addSection(newSection(getString(R.string.timetable), new Fragment()));
		addSection(newSection(getString(R.string.roomsearch), new Fragment()));
		addSection(newSection(getString(R.string.mvv), new Fragment()));
		addSection(newSection(getString(R.string.food), new Fragment()));
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.actionmenu, menu);
		DataSource.getInstance(this).getPresence(new Callback<PresenceFetcher>() {
			@Override
			public void onResult(final List<PresenceFetcher> result) {
				MenuItem presenceItem = menu.findItem(R.id.menu_presence);
				if (PresenceFetcher.isPresent(result)) {
					presenceItem.setIcon(R.drawable.circle_green_dark);
				} else {
					presenceItem.setIcon(R.drawable.circle_yellow_light);
				}
			}
		});
		return true;
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
}
