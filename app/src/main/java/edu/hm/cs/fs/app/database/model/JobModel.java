package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.model.Job;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.JobController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data from the {@link JobController}.
 *
 * @author Fabio
 */
public class JobModel extends CachedModel<Job> {

    /**
     * Get all job entries.
     *
     * @param refresh  should set to <code>true</code> if the blackboard entries should be updated
     *                 from the web.
     * @param callback to retrieve the result.
     */
    public void getAll(final boolean refresh, @NonNull final ICallback<List<Job>> callback) {
        getData(refresh, callback);
    }

    /**
     * Get a specific job entry by title.
     *
     * @param title    of the job entry.
     * @param callback to retrieve the result.
     */
    public void getItem(@NonNull final String title, @NonNull final ICallback<Job> callback) {
        getData(false, new ICallback<List<Job>>() {
            @Override
            public void onSuccess(@NonNull List<Job> data) {
                for (Job job : data) {
                    if (job.getTitle().equals(title)) {
                        callback.onSuccess(job);
                        return;
                    }
                }
            }

            @Override
            public void onError(@NonNull IError error) {
                callback.onError(error);
            }
        });
    }

    @Override
    protected void updateOnline(@NonNull final ICallback<List<Job>> callback) {
        Controllers.create(JobController.class).getJobs(new Callback<List<Job>>() {
            @Override
            public void success(List<Job> jobs, Response response) {
                callback.onSuccess(jobs);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
