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
import edu.hm.cs.fs.app.database.model.PublicTransportModel;
import edu.hm.cs.fs.app.view.IPublicTransportView;
import edu.hm.cs.fs.common.model.PublicTransport;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class PublicTransportPasingPresenterTest {

    @Test
    public void testGetPublicTransportsSuccess() {
        final IPublicTransportView view = mock(IPublicTransportView.class);

        final PublicTransportModel model = mock(PublicTransportModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<PublicTransport>> callback = (ICallback<List<PublicTransport>>) invocation.getArguments()[0];
                callback.onSuccess(new ArrayList<PublicTransport>());
                return null;
            }
        }).when(model).getDepartureTimesForPasing(any(ICallback.class));

        final PublicTransportPasingPresenter presenter = new PublicTransportPasingPresenter(view, model);
        presenter.loadPublicTransports();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getDepartureTimesForPasing(any(ICallback.class));
        verify(model, atLeast(0)).getDepartureTimesForLothstr(any(ICallback.class));
        verify(view, atLeastOnce()).showContent(anyList());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetPublicTransportsError() {
        final IPublicTransportView view = mock(IPublicTransportView.class);

        final PublicTransportModel model = mock(PublicTransportModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<PublicTransport>> callback = (ICallback<List<PublicTransport>>) invocation.getArguments()[0];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getDepartureTimesForPasing(any(ICallback.class));

        final PublicTransportPasingPresenter presenter = new PublicTransportPasingPresenter(view, model);
        presenter.loadPublicTransports();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getDepartureTimesForPasing(any(ICallback.class));
        verify(model, atLeast(0)).getDepartureTimesForLothstr(any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
