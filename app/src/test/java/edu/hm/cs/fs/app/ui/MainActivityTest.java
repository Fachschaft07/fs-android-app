package edu.hm.cs.fs.app.ui;

import android.os.Bundle;

import com.fk07.BuildConfig;
import com.fk07.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import edu.hm.cs.fs.app.ui.blackboard.BlackBoardFragment;
import edu.hm.cs.fs.app.ui.home.HomeFragment;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by FHellman on 18.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class)
                .setup()
                .get();
    }

    @Test
    public void testDefaultMenuItemSelection() {
        final MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .setup()
                .get();

        assertThat(true, is(MainActivity.getNavigator().getActiveFragment() instanceof HomeFragment));
    }

    @Test
    public void testSavedMenuItemSelection() {
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.NAV_ITEM_ID, R.id.menu_blackboard);

        final MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .setup(bundle)
                .get();

        assertThat(true, is(MainActivity.getNavigator().getActiveFragment() instanceof BlackBoardFragment));
    }
}
