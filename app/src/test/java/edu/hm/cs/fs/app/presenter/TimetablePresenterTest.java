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
import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.app.view.ITimetableView;
import edu.hm.cs.fs.common.model.Lesson;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TimetablePresenterTest {
    @Test
    public void testGetTimetableSuccess() {
        final ITimetableView view = mock(ITimetableView.class);

        final TimetableModel model = mock(TimetableModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Lesson>> callback = (ICallback<List<Lesson>>) invocation.getArguments()[0];
                callback.onSuccess(new ArrayList<Lesson>());
                return null;
            }
        }).when(model).getTimetable(any(ICallback.class));

        final TimetablePresenter presenter = new TimetablePresenter(view, model);
        presenter.loadTimetable();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getTimetable(any(ICallback.class));
        verify(view, atLeastOnce()).showContent(anyList());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetTimetableError() {
        final ITimetableView view = mock(ITimetableView.class);

        final TimetableModel model = mock(TimetableModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback callback = (ICallback) invocation.getArguments()[0];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getTimetable(any(ICallback.class));

        final TimetablePresenter presenter = new TimetablePresenter(view, model);
        presenter.loadTimetable();

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getTimetable(any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
