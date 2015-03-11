package edu.hm.cs.fs.app.util.multipane;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fk07.R;

/**
 * This activity displays the detail fragment if there is no multi-pane available.
 */
public class ActivityMultiPaneDetail extends ActionBarActivity {
	private static Fragment mDetailFragment;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);

		final FragmentManager supportFragmentManager = getSupportFragmentManager();
		final FragmentTransaction transaction = supportFragmentManager.beginTransaction();
		transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.replace(R.id.frame_container, mDetailFragment);
		transaction.disallowAddToBackStack();
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if(android.R.id.home == item.getItemId()) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// If the user rotate to landscape -> finish the activity and let the
			// MultiPaneFragment handle this
			finish();
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mDetailFragment = null;
	}

	protected static void setDetailFragment(final Fragment fragment) {
		ActivityMultiPaneDetail.mDetailFragment = fragment;
	}
}
