package edu.hm.cs.fs.app.util.multipane;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Fabio on 06.01.2015.
 */
public class ActivityMultiPaneDetail extends ActionBarActivity {
	private static Fragment mFragment;

	@InjectView(R.id.toolbar)
	Toolbar mToolbar;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		ButterKnife.inject(this);

        String title = ((OnMultiPaneDetailSegment<?>) mFragment).getTitle();

		MaterialMenuDrawable materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
		materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
        mToolbar.setTitle(title);
		mToolbar.setNavigationIcon(materialMenu);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);

		final FragmentManager supportFragmentManager = getSupportFragmentManager();
		final FragmentTransaction transaction = supportFragmentManager.beginTransaction();
		transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.replace(R.id.frame_container, mFragment);
		transaction.disallowAddToBackStack();
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (android.R.id.home == item.getItemId()) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mFragment = null;
	}

	public static void setDetailFragment(final Fragment fragment) {
		ActivityMultiPaneDetail.mFragment = fragment;
	}
}
