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
import edu.hm.cs.fs.app.database.JobModel;
import edu.hm.cs.fs.app.view.IJobView;
import edu.hm.cs.fs.common.model.Job;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class JobPresenterTest {
    @Test
    public void testGetJobsSuccess() {
        final IJobView view = mock(IJobView.class);

        final JobModel model = mock(JobModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Job>> callback = (ICallback<List<Job>>) invocation.getArguments()[1];
                callback.onSuccess(new ArrayList<Job>());
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));

        final JobPresenter presenter = new JobPresenter(view, model);
        presenter.loadJobs(true);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getAll(anyBoolean(), any(ICallback.class));
        verify(view, atLeastOnce()).showContent(anyList());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetJobsError() {
        final IJobView view = mock(IJobView.class);

        final JobModel model = mock(JobModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Job>> callback = (ICallback<List<Job>>) invocation.getArguments()[1];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));

        final JobPresenter presenter = new JobPresenter(view, model);
        presenter.loadJobs(true);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getAll(anyBoolean(), any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
