package edu.hm.cs.fs.app.presenter;

import com.fk07.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.FsModel;
import edu.hm.cs.fs.app.view.IPresenceView;
import edu.hm.cs.fs.common.model.Presence;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class PresencePresenterTest {
    @Test
    public void testGetPresenceSuccess() {
        final IPresenceView view = mock(IPresenceView.class);

        final FsModel model = mock(FsModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Presence>> callback = (ICallback<List<Presence>>) invocation.getArguments()[0];
                callback.onSuccess(new ArrayList<Presence>());
                return null;
            }
        }).when(model).getPresence(any(ICallback.class));

        final PresencePresenter presenter = new PresencePresenter(view, model);
        presenter.loadPresence();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getPresence(any(ICallback.class));
        verify(view, atLeastOnce()).showContent(anyList());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetPresenceError() {
        final IPresenceView view = mock(IPresenceView.class);

        final FsModel model = mock(FsModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Presence>> callback = (ICallback<List<Presence>>) invocation.getArguments()[0];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getPresence(any(ICallback.class));

        final PresencePresenter presenter = new PresencePresenter(view, model);
        presenter.loadPresence();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getPresence(any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
