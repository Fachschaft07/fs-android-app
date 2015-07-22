package edu.hm.cs.fs.app.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.ui.mensa.MealFragment;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.util.Navigator;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener,
        NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view) NavigationView mNavigationView;
    @Bind(R.id.toolbar) Toolbar mToolbar;

    private static Navigator mNavigator;
    private ActionBarDrawerToggle mDrawerToggle;
    private @IdRes int mCurrentMenuItem;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupNavigationDrawer();
        initNavigator();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupNavigationDrawer() {
        mDrawerLayout.setDrawerListener(this);
        //TODO look at documantation => homepage do I really need like that?
        mDrawerToggle = new ActionBarDrawerToggle(this
                , mDrawerLayout
                , mToolbar
                , R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);

        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initNavigator() {
        if(mNavigator != null) {
            return;
        }
        mNavigator = new Navigator(getSupportFragmentManager(), R.id.container);
    }

    private void setNewRootFragment(BaseFragment fragment){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            if(fragment.hasCustomToolbar()){
                actionBar.hide();
            }else {
                actionBar.show();
            }
        }
        mNavigator.setRootFragment(fragment);
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        mDrawerToggle.onDrawerOpened(drawerView);
    }

    public void openDrawer(){
        mDrawerLayout.openDrawer(Gravity.LEFT);
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
        if(id == mCurrentMenuItem) {
            mDrawerLayout.closeDrawers();
            return false;
        }
        switch (id){
            case R.id.menu_blackboard:
                //setNewRootFragment(StandardAppBarFragment.newInstance());
                break;
            case R.id.menu_timetable:
                //setNewRootFragment(TabHolderFragment.newInstance());
                break;

            case R.id.menu_roomsearch:
                //setNewRootFragment(FlexibleSpaceWithImageFragment.newInstance());
                break;

            case R.id.menu_presence:
                //setNewRootFragment(FlexibleSpaceFragment.newInstance());
                break;

            case R.id.menu_food:
                setNewRootFragment(new MealFragment());
                break;

            case R.id.menu_jobs:
                //setNewRootFragment(FloatingActionButtonFragment.newInstance());
                break;

            case R.id.menu_mvv:
                //setNewRootFragment(new MvvFragment());
                break;

            case R.id.menu_info:
                //setNewRootFragment(FloatingActionButtonFragment.newInstance());
                break;

            case R.id.menu_feedback:
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"app@fs.cs.hm.edu"});
                startActivity(intent);
                return false;
        }
        mCurrentMenuItem = id;
        menuItem.setChecked(true);
        return false;
    }

    @Override
    public void finish() {
        mNavigator = null;
        super.finish();
    }

    public void setTransparentStatusBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
    }

    private String nextProverb() {
        final String[] stringArray = getResources().getStringArray(R.array.proverbs);
        return stringArray[((int) (Math.random() * stringArray.length))];
    }
}
