package edu.hm.cs.fs.app.presenter;

import android.text.Spanned;

import com.fk07.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.BlackBoardModel;
import edu.hm.cs.fs.app.view.IBlackBoardDetailView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.simple.SimplePerson;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class BlackBoardDetailPresenterTest {
    private static final String BLACK_BAORD_ENTRY_ID = "id";

    @Test
    public void testGetBlackBoardSuccess() {
        final IBlackBoardDetailView view = mock(IBlackBoardDetailView.class);

        final BlackboardEntry entry = mock(BlackboardEntry.class);
        when(entry.getId()).thenReturn(BLACK_BAORD_ENTRY_ID);
        when(entry.getSubject()).thenReturn("subject");
        when(entry.getText()).thenReturn("text");
        when(entry.getUrl()).thenReturn("url");
        when(entry.getAuthor()).thenReturn(mock(SimplePerson.class));
        when(entry.getGroups()).thenReturn(new ArrayList<Group>());
        when(entry.getPublish()).thenReturn(mock(Date.class));
        when(entry.getTeachers()).thenReturn(new ArrayList<SimplePerson>());

        final BlackBoardModel model = mock(BlackBoardModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<BlackboardEntry>> callback = (ICallback<List<BlackboardEntry>>) invocation.getArguments()[0];
                callback.onSuccess(Arrays.asList(entry));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<BlackboardEntry> callback = (ICallback<BlackboardEntry>) invocation.getArguments()[1];
                callback.onSuccess(entry);
                return null;
            }
        }).when(model).getItem(anyString(), any(ICallback.class));

        final BlackBoardDetailPresenter presenter = new BlackBoardDetailPresenter(view, model);
        presenter.loadBlackBoardEntry(BLACK_BAORD_ENTRY_ID);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(view, atLeastOnce()).showSubject(any(Spanned.class));
        verify(view, atLeastOnce()).showAuthor(anyString());
        verify(view, atLeastOnce()).showDescription(any(Spanned.class));
        verify(view, atLeastOnce()).showUrl(anyString());
        verify(view, atLeastOnce()).showGroups(anyString());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetBlackBoardCacheSuccess() {
        final IBlackBoardDetailView view = mock(IBlackBoardDetailView.class);

        final BlackboardEntry entry = mock(BlackboardEntry.class);
        when(entry.getId()).thenReturn(BLACK_BAORD_ENTRY_ID);
        when(entry.getSubject()).thenReturn("subject");
        when(entry.getText()).thenReturn("text");
        when(entry.getUrl()).thenReturn("url");
        when(entry.getAuthor()).thenReturn(mock(SimplePerson.class));
        when(entry.getGroups()).thenReturn(new ArrayList<Group>());
        when(entry.getPublish()).thenReturn(mock(Date.class));
        when(entry.getTeachers()).thenReturn(new ArrayList<SimplePerson>());

        final BlackBoardModel model = mock(BlackBoardModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<BlackboardEntry>> callback = (ICallback<List<BlackboardEntry>>) invocation.getArguments()[0];
                callback.onSuccess(Arrays.asList(entry));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<BlackboardEntry> callback = (ICallback<BlackboardEntry>) invocation.getArguments()[1];
                callback.onSuccess(entry);
                return null;
            }
        }).when(model).getItem(anyString(), any(ICallback.class));

        final BlackBoardDetailPresenter presenter = new BlackBoardDetailPresenter(view, model);
        presenter.loadBlackBoardEntry(BLACK_BAORD_ENTRY_ID);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(view, atLeastOnce()).showSubject(any(Spanned.class));
        verify(view, atLeastOnce()).showAuthor(anyString());
        verify(view, atLeastOnce()).showDescription(any(Spanned.class));
        verify(view, atLeastOnce()).showUrl(anyString());
        verify(view, atLeastOnce()).showGroups(anyString());
        verify(view, atLeastOnce()).hideLoading();

        presenter.loadBlackBoardEntry(BLACK_BAORD_ENTRY_ID);
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(model, atLeast(0)).getAll(anyBoolean(), any(ICallback.class));
    }

    @Test
    public void testGetBlackBoardError() {
        final IBlackBoardDetailView view = mock(IBlackBoardDetailView.class);

        final BlackBoardModel model = mock(BlackBoardModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback callback = (ICallback) invocation.getArguments()[0];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback callback = (ICallback) invocation.getArguments()[1];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getItem(anyString(), any(ICallback.class));

        final BlackBoardDetailPresenter presenter = new BlackBoardDetailPresenter(view, model);
        presenter.loadBlackBoardEntry(BLACK_BAORD_ENTRY_ID);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(model, atLeast(0)).getAll(anyBoolean(), any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
