package edu.hm.cs.fs.app.presenter;

import com.fk07.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.InfoModel;
import edu.hm.cs.fs.app.view.IInfoView;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class InfoPresenterTest {
    @Test
    public void testGetVersionSuccess() {
        final IInfoView view = mock(IInfoView.class);

        final InfoModel model = mock(InfoModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<String> callback = (ICallback<String>) invocation.getArguments()[0];
                callback.onSuccess(anyString());
                return null;
            }
        }).when(model).getVersion(any(ICallback.class));

        final InfoPresenter presenter = new InfoPresenter(view, model);
        presenter.loadVersion();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getVersion(any(ICallback.class));
        verify(view, atLeastOnce()).showVersion(anyString());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetVersionError() {
        final IInfoView view = mock(IInfoView.class);

        final InfoModel model = mock(InfoModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<String> callback = (ICallback<String>) invocation.getArguments()[0];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getVersion(any(ICallback.class));

        final InfoPresenter presenter = new InfoPresenter(view, model);
        presenter.loadVersion();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getVersion(any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
