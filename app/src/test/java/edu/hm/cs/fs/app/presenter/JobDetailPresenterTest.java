package edu.hm.cs.fs.app.presenter;

import android.text.Spanned;

import com.fk07.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

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
import edu.hm.cs.fs.app.database.model.JobModel;
import edu.hm.cs.fs.app.view.IJobDetailView;
import edu.hm.cs.fs.common.model.Job;
import edu.hm.cs.fs.common.model.Person;

/**
 * Created by FHellman on 13.08.2015.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class JobDetailPresenterTest {
    private static final String JOB_TITLE = "title";

    @Test
    public void testGetJobByTitleSuccess() {
        final IJobDetailView view = mock(IJobDetailView.class);

        final Job job = mock(Job.class);
        when(job.getId()).thenReturn(null);
        when(job.getTitle()).thenReturn(JOB_TITLE);
        when(job.getProvider()).thenReturn("provider");
        when(job.getDescription()).thenReturn("description");
        when(job.getUrl()).thenReturn("url");
        when(job.getContact()).thenReturn(mock(Person.class));
        when(job.getExpire()).thenReturn(mock(Date.class));
        when(job.getProgram()).thenReturn(null);

        final JobModel model = spy(JobModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Job>> callback = (ICallback<List<Job>>) invocation.getArguments()[0];
                callback.onSuccess(Arrays.asList(job));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<Job> callback = (ICallback<Job>) invocation.getArguments()[1];
                callback.onSuccess(job);
                return null;
            }
        }).when(model).getItem(anyString(), any(ICallback.class));

        final JobDetailPresenter presenter = new JobDetailPresenter(view, model);
        presenter.loadJob(JOB_TITLE);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(view, atLeastOnce()).showSubject(any(Spanned.class));
        verify(view, atLeastOnce()).showProvider(anyString());
        verify(view, atLeastOnce()).showDescription(any(Spanned.class));
        verify(view, atLeastOnce()).showUrl(anyString());
        verify(view, atLeastOnce()).showAuthor(anyString());
        verify(view, atLeastOnce()).hideLoading();
    }

    @Test
    public void testGetJobByTitleCacheSuccess() {
        final IJobDetailView view = mock(IJobDetailView.class);

        final Job job = mock(Job.class);
        when(job.getId()).thenReturn(null);
        when(job.getTitle()).thenReturn(JOB_TITLE);
        when(job.getProvider()).thenReturn("provider");
        when(job.getDescription()).thenReturn("description");
        when(job.getUrl()).thenReturn("url");
        when(job.getContact()).thenReturn(mock(Person.class));
        when(job.getExpire()).thenReturn(mock(Date.class));
        when(job.getProgram()).thenReturn(null);

        final JobModel model = spy(JobModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Job>> callback = (ICallback<List<Job>>) invocation.getArguments()[0];
                callback.onSuccess(Arrays.asList(job));
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<Job> callback = (ICallback<Job>) invocation.getArguments()[1];
                callback.onSuccess(job);
                return null;
            }
        }).when(model).getItem(anyString(), any(ICallback.class));

        final JobDetailPresenter presenter = new JobDetailPresenter(view, model);
        presenter.loadJob(JOB_TITLE);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(view, atLeastOnce()).showSubject(any(Spanned.class));
        verify(view, atLeastOnce()).showProvider(anyString());
        verify(view, atLeastOnce()).showDescription(any(Spanned.class));
        verify(view, atLeastOnce()).showUrl(anyString());
        verify(view, atLeastOnce()).showAuthor(anyString());
        verify(view, atLeastOnce()).hideLoading();

        presenter.loadJob(JOB_TITLE);
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(model, atLeast(0)).getAll(anyBoolean(), any(ICallback.class));
    }

    @Test
    public void testGetJobByTitleError() {
        final IJobDetailView view = mock(IJobDetailView.class);

        final JobModel model = mock(JobModel.class);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<List<Job>> callback = (ICallback<List<Job>>) invocation.getArguments()[0];
                callback.onSuccess(new ArrayList<Job>());
                return null;
            }
        }).when(model).getAll(anyBoolean(), any(ICallback.class));
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                ICallback<Job> callback = (ICallback<Job>) invocation.getArguments()[1];
                callback.onError(any(IError.class));
                return null;
            }
        }).when(model).getItem(anyString(), any(ICallback.class));

        final JobDetailPresenter presenter = new JobDetailPresenter(view, model);
        presenter.loadJob(JOB_TITLE);

        verify(view, atLeastOnce()).showLoading();
        verify(model, atLeastOnce()).getItem(anyString(), any(ICallback.class));
        verify(view, atLeastOnce()).showError(any(IError.class));
        verify(view, atLeastOnce()).hideLoading();
    }
}
