package edu.hm.cs.fs.app.database.model;

import com.fk07.BuildConfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import edu.hm.cs.fs.common.model.Job;
import hugo.weaving.DebugLog;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class JobModelTest {
    @Test
    public void test() {
        // TODO
    }
}
