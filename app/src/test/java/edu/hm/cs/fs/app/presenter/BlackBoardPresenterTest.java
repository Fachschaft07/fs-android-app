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
import edu.hm.cs.fs.app.database.BlackBoardModel;
import edu.hm.cs.fs.app.view.IBlackBoardView;
import edu.hm.cs.fs.common.model.BlackboardEntry;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class BlackBoardPresenterTest {
    @Test
    public void testGetBlackBoardSuccess() {
        final IBlackBoardView view = mock(IBlackBoardView.class);

        final BlackBoardModel model = mock(BlackBoardModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<BlackboardEntry>> callback = (ICallback<List<BlackboardEntry>>) invocation.getArguments()[1];
                callback.onSuccess(new ArrayList<BlackboardEntry>());
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));

        final BlackBoardPresenter presenter = new BlackBoardPresenter(view, model);
        presenter.loadBlackBoard(true);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getAll(anyBoolean(), any(ICallback.class));
        verify(view, atLeastOnce()).showContent(anyList());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetBlackBoardError() {
        final IBlackBoardView view = mock(IBlackBoardView.class);

        final BlackBoardModel model = mock(BlackBoardModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback callback = (ICallback) invocation.getArguments()[1];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));

        final BlackBoardPresenter presenter = new BlackBoardPresenter(view, model);
        presenter.loadBlackBoard(true);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getAll(anyBoolean(), any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
