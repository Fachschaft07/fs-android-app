package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.Job;
import edu.hm.cs.fs.restclient.Controllers;
import edu.hm.cs.fs.restclient.JobController;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobModel implements IModel {
    private static JobModel mInstance;
    private List<Job> mJobCache;

    private JobModel() {
    }

    public static JobModel getInstance() {
        if(mInstance == null) {
            mInstance = new JobModel();
        }
        return mInstance;
    }

    public void getJobs(@NonNull final ICallback<List<Job>> callback) {
        Controllers.create(SERVER_IP, JobController.class)
                .getJobs(new Callback<List<Job>>() {

                    @Override
                    public void success(List<Job> jobs, Response response) {
                        mJobCache = jobs;
                        callback.onSuccess(jobs);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onError(error.getLocalizedMessage());
                    }
                });
    }

    public void getJob(@NonNull final String title, @NonNull final ICallback<Job> callback) {
        if(mJobCache != null) {
            for (Job job : mJobCache) {
                if (title.equals(job.getTitle())) {
                    callback.onSuccess(job);
                }
            }
        } else {
            getJobs(new ICallback<List<Job>>() {
                @Override
                public void onSuccess(List<Job> data) {
                    for (Job job : data) {
                        if (job.getId().equals(title)) {
                            callback.onSuccess(job);
                            return;
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    callback.onError(error);
                }
            });
        }
    }
}
