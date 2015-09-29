package edu.hm.cs.fs.app.ui;

import com.fk07.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import edu.hm.cs.fs.app.ui.timetable.TimetableFragment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Created by FHellman on 18.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class NavigatorTest {
    private Navigator navigator;

    @Before
    public void setUp() throws Exception {
        final MainActivity activity = Robolectric.buildActivity(MainActivity.class)
                .setup()
                .get();
        navigator = activity.getNavigator();
    }

    @Test
    public void testNavigatorCreated() {
        assertThat(null, is(not(navigator)));
        assertThat(true, is(navigator.isEmpty()));
    }

    @Test
    public void testGetActiveFragment() throws Exception {
        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment = new TimetableFragment();
        navigator.goTo(fragment);

        assertThat(fragment, is(navigator.getActiveFragment()));
        assertThat(1, is(navigator.getSize()));
    }

    @Test
    public void testGoTo() throws Exception {
        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment1 = new TimetableFragment();
        navigator.goTo(fragment1);

        assertThat(fragment1, is(navigator.getActiveFragment()));
        assertThat(1, is(navigator.getSize()));

        final TimetableFragment fragment2 = new TimetableFragment();
        navigator.goTo(fragment2);

        assertThat(fragment2, is(navigator.getActiveFragment()));
        assertThat(2, is(navigator.getSize()));
    }

    @Test
    public void testGetName() throws Exception {
        final TimetableFragment fragment1 = new TimetableFragment();
        assertThat(fragment1.getClass().getSimpleName(), is(navigator.getName(fragment1)));
    }

    @Test(expected = NullPointerException.class)
    public void testSetRootFragment() throws Exception {
        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment1 = new TimetableFragment();
        navigator.setRootFragment(fragment1);

        assertThat(fragment1, is(navigator.getActiveFragment()));
        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment2 = new TimetableFragment();
        navigator.goTo(fragment2);

        assertThat(fragment2, is(navigator.getActiveFragment()));
        assertThat(1, is(navigator.getSize()));

        // popBackStackEntries does not work like this with Robolectric
        final TimetableFragment fragment3 = new TimetableFragment();
        navigator.setRootFragment(fragment3);

        assertThat(fragment3, is(navigator.getActiveFragment()));
        assertThat(true, is(navigator.isEmpty()));
    }

    @Test(expected = NullPointerException.class)
    public void testGoOneBack() throws Exception {
        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment1 = new TimetableFragment();
        navigator.setRootFragment(fragment1);

        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment2 = new TimetableFragment();
        navigator.goTo(fragment2);

        assertThat(fragment2, is(navigator.getActiveFragment()));
        assertThat(1, is(navigator.getSize()));

        // popBackStackEntries does not work like this with Robolectric
        navigator.goOneBack();
        assertThat(fragment1, is(navigator.getActiveFragment()));
        assertThat(true, is(navigator.isEmpty()));
    }

    @Test(expected = NullPointerException.class)
    public void testGotToTheRootFragmentBack() throws Exception {
        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment1 = new TimetableFragment();
        navigator.setRootFragment(fragment1);

        assertThat(fragment1, is(navigator.getActiveFragment()));
        assertThat(true, is(navigator.isEmpty()));

        final TimetableFragment fragment2 = new TimetableFragment();
        navigator.goTo(fragment2);

        assertThat(fragment2, is(navigator.getActiveFragment()));
        assertThat(1, is(navigator.getSize()));

        final TimetableFragment fragment3 = new TimetableFragment();
        navigator.goTo(fragment3);

        assertThat(fragment3, is(navigator.getActiveFragment()));
        assertThat(2, is(navigator.getSize()));

        // popBackStackEntries does not work like this with Robolectric
        navigator.gotToTheRootFragmentBack();

        assertThat(fragment1, is(navigator.getActiveFragment()));
        assertThat(true, is(navigator.isEmpty()));
    }
}
