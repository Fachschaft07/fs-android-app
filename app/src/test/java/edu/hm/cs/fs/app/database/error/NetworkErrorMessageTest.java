package edu.hm.cs.fs.app.database.error;

import android.content.Context;

import com.fk07.BuildConfig;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import retrofit.RetrofitError;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class NetworkErrorMessageTest {
    @Test
    public void testConnection() {
        final RetrofitError retrofitError = mock(RetrofitError.class);
        when(retrofitError.getKind()).thenReturn(RetrofitError.Kind.NETWORK);

        final IError networkError = ErrorFactory.http(retrofitError);

        Assert.assertThat(false, equalTo(networkError.isConnected()));
    }

    @Test
    public void testConnectionMessage() {
        final String expectedOutput = "message";

        final Context context = mock(Context.class);
        when(context.getString(anyInt())).thenReturn(expectedOutput);

        final RetrofitError retrofitError = mock(RetrofitError.class);
        when(retrofitError.getKind()).thenReturn(RetrofitError.Kind.NETWORK);

        final IError networkError = ErrorFactory.http(retrofitError);

        Assert.assertThat(expectedOutput, is(equalTo(networkError.getMessage(context))));
    }

    @Test
    public void testOtherMessage() {
        final String expectedOutput = "message";

        final Context context = mock(Context.class);
        when(context.getString(anyInt())).thenReturn("other");

        final RetrofitError retrofitError = mock(RetrofitError.class);
        when(retrofitError.getKind()).thenReturn(RetrofitError.Kind.CONVERSION);
        when(retrofitError.getLocalizedMessage()).thenReturn(expectedOutput);

        final IError networkError = ErrorFactory.http(retrofitError);

        Assert.assertThat(expectedOutput, is(equalTo(networkError.getMessage(context))));
    }
}
