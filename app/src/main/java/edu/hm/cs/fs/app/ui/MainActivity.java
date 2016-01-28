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

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.service.BlackboardNotificationService;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardListFragment;
import edu.hm.cs.fs.app.ui.exam.ExamListFragment;
import edu.hm.cs.fs.app.ui.fs.news.FsNewsListFragment;
import edu.hm.cs.fs.app.ui.home.HomeFragment;
import edu.hm.cs.fs.app.ui.info.InfoActivity;
import edu.hm.cs.fs.app.ui.job.JobListFragment;
import edu.hm.cs.fs.app.ui.lostfound.LostFoundListFragment;
import edu.hm.cs.fs.app.ui.meal.MealListFragment;
import edu.hm.cs.fs.app.ui.publictransport.PublicTransportTabFragment;
import edu.hm.cs.fs.app.ui.roomsearch.RoomSearchListFragment;
import edu.hm.cs.fs.app.ui.timetable.TimetableFragment;
import edu.hm.cs.fs.app.util.ServiceUtils;
import edu.hm.cs.fs.domain.DataServiceMigration;

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
    @Inject
    DataServiceMigration mMigration;
    private ActionBarDrawerToggle mDrawerToggle;
    @IdRes
    private int mCurrentMenuItem;

    private static void resetNavigator() {
        mNavigator = null;
    }

    public static Navigator getNavigator() {
        return mNavigator;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        App.getAppComponent(this).inject(this);

        mMigration.check();
        setupServices();
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

    private void setupServices() {
        if (!ServiceUtils.isServiceRunning(this, BlackboardNotificationService.class)) {
            startService(new Intent(this, BlackboardNotificationService.class));
        }
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
                setNewRootFragment(new BlackBoardListFragment());
                break;

            case R.id.menu_timetable:
                setNewRootFragment(new TimetableFragment());
                break;

            case R.id.menu_exams:
                setNewRootFragment(new ExamListFragment());
                break;

            case R.id.menu_roomsearch:
                setNewRootFragment(new RoomSearchListFragment());
                break;

            case R.id.menu_lostfound:
                setNewRootFragment(new LostFoundListFragment());
                break;

            // Student council
            case R.id.menu_news:
                setNewRootFragment(new FsNewsListFragment());
                break;

            /*
            case R.id.menu_presence:
                setNewRootFragment(new PresenceListFragment());
                break;
                */

            // Offers
            case R.id.menu_food:
                setNewRootFragment(new MealListFragment());
                break;

            case R.id.menu_jobs:
                setNewRootFragment(new JobListFragment());
                break;

            case R.id.menu_mvv:
                setNewRootFragment(new PublicTransportTabFragment());
                break;

            // Others
            case R.id.menu_info:
                startActivity(new Intent(this, InfoActivity.class));
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
        final MenuItem item = mNavigationView.getMenu().findItem(mCurrentMenuItem);
        if (item != null) {
            item.setChecked(false);
        }
        mCurrentMenuItem = id;
        menuItem.setChecked(true);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (!mNavigator.isEmpty()) {
            mNavigator.goOneBack();
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
