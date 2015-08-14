package edu.hm.cs.fs.app.database.error;

import android.content.Context;

import com.fk07.BuildConfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by FHellman on 14.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class ExceptionErrorMessageTest {
    @Test
    public void testConnection() {
        final Exception exception = mock(Exception.class);

        final IError networkError = ErrorFactory.exception(exception);

        Assert.assertThat(true, equalTo(networkError.isConnected()));
    }

    @Test
    public void testMessage() {
        final String expectedOutput = "message";
        final Context context = mock(Context.class);

        final Exception exception = mock(Exception.class);
        when(exception.getLocalizedMessage()).thenReturn(expectedOutput);

        final IError networkError = ErrorFactory.exception(exception);

        Assert.assertThat(expectedOutput, is(equalTo(networkError.getMessage(context))));
    }
}
