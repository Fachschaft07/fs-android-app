package edu.hm.cs.fs.app.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardFragment;
import edu.hm.cs.fs.app.ui.fs.news.FsNewsFragment;
import edu.hm.cs.fs.app.ui.home.HomeFragment;
import edu.hm.cs.fs.app.ui.info.InfoFragment;
import edu.hm.cs.fs.app.ui.job.JobFragment;
import edu.hm.cs.fs.app.ui.lostfound.LostFoundFragment;
import edu.hm.cs.fs.app.ui.meal.MealFragment;
import edu.hm.cs.fs.app.ui.publictransport.PublicTransportTabFragment;
import edu.hm.cs.fs.app.ui.roomsearch.RoomSearchFragment;
import edu.hm.cs.fs.app.ui.timetable.TimetableFragment;

/**
 * @author Fabio
 */
public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener {
    public static final String NAV_ITEM_ID = "navItemId";

    private static Navigator mNavigator;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    @IdRes
    private int mCurrentMenuItem;

    private static void resetNavigator() {
        mNavigator = null;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupNavigationDrawer();
        initNavigator();

        // load saved navigation state if present
        final int currentMenuItem;
        if (null == savedInstanceState) {
            currentMenuItem = R.id.menu_home;
        } else {
            currentMenuItem = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        onNavigationItemSelected(mNavigationView.getMenu().findItem(currentMenuItem));
        mCurrentMenuItem = currentMenuItem;
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void setupNavigationDrawer() {
        mDrawerLayout.setDrawerListener(this);
        //TODO look at documantation => homepage do I really need like that?
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initNavigator() {
        mNavigator = new Navigator(this, getSupportFragmentManager(), R.id.container, R.id.container_detail);
    }

    private void setNewRootFragment(BaseFragment fragment) {
        mNavigator.setRootFragment(fragment);
        mDrawerLayout.closeDrawers();
    }

    public Navigator getNavigator() {
        return mNavigator;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        mDrawerToggle.onDrawerClosed(drawerView);
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        mDrawerToggle.onDrawerStateChanged(newState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        @IdRes int id = menuItem.getItemId();
        if (id == mCurrentMenuItem) {
            mDrawerLayout.closeDrawers();
            return false;
        }
        switch (id) {
            // My study
            case R.id.menu_home:
                setNewRootFragment(new HomeFragment());
                break;

            case R.id.menu_blackboard:
                setNewRootFragment(new BlackBoardFragment());
                break;

            case R.id.menu_timetable:
                setNewRootFragment(new TimetableFragment());
                break;

            case R.id.menu_roomsearch:
                setNewRootFragment(new RoomSearchFragment());
                break;

            case R.id.menu_lostfound:
                setNewRootFragment(new LostFoundFragment());
                break;

            // Student council
            case R.id.menu_news:
                setNewRootFragment(new FsNewsFragment());
                break;

            /*
            case R.id.menu_presence:
                setNewRootFragment(new PresenceFragment());
                break;
                */

            // Offers
            case R.id.menu_food:
                setNewRootFragment(new MealFragment());
                break;

            case R.id.menu_jobs:
                setNewRootFragment(new JobFragment());
                break;

            case R.id.menu_mvv:
                setNewRootFragment(new PublicTransportTabFragment());
                break;

            // Others
            case R.id.menu_info:
                setNewRootFragment(new InfoFragment());
                break;

            case R.id.menu_feedback:
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException ignored) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="
                                    + appPackageName)));
                }
                return false;

            case R.id.menu_help:
                final Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"app@fs.cs.hm.edu"});
                startActivity(intent);
                return false;
        }
        mCurrentMenuItem = id;
        menuItem.setChecked(true);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        outState.putInt(NAV_ITEM_ID, mCurrentMenuItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void finish() {
        resetNavigator();
        super.finish();
    }
}
