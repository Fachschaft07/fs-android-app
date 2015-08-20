package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
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
 * @author Fabio
 */
public class JobModel extends CachedModel<Job> {
    public void getAll(final boolean refresh, @NonNull final ICallback<List<Job>> callback) {
        getData(refresh, callback);
    }

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
    protected void update(@NonNull final ICallback<List<Job>> callback) {
        Controllers.create(LOCALHOST, JobController.class)
                .getJobs(new Callback<List<Job>>() {
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
