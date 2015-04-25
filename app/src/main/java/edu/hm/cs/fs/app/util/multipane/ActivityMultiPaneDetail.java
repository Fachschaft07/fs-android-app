package edu.hm.cs.fs.app.util.multipane;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Fabio on 06.01.2015.
 */
public class ActivityMultiPaneDetail extends AppCompatActivity {
	private static Fragment mFragment;

    @InjectView(R.id.statusBar)
    ImageView mStatusBar;
	@InjectView(R.id.toolbar)
	Toolbar mToolbar;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		ButterKnife.inject(this);

        // if device is kitkat
        Resources.Theme theme = this.getTheme();
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            TypedArray windowTraslucentAttribute = theme.obtainStyledAttributes(new int[]{android.R.attr.windowTranslucentStatus});
            boolean kitkatTraslucentStatusbar = windowTraslucentAttribute.getBoolean(0, false);
            if(kitkatTraslucentStatusbar) {
                Window window = this.getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                RelativeLayout.LayoutParams statusParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.traslucentStatusMargin));
                mStatusBar.setLayoutParams(statusParams);
                mStatusBar.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Toolbar
		MaterialMenuDrawable materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
		materialMenu.setIconState(MaterialMenuDrawable.IconState.ARROW);
		mToolbar.setNavigationIcon(materialMenu);
		setSupportActionBar(mToolbar);
        getSupportActionBar().setElevation(10f);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(true);

        // Fragments
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
