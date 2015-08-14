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
 * Created by FHellman on 10.08.2015.
 */
public class JobModel implements IModel {
    private static JobModel mInstance;
    private List<Job> mJobCache;

    private JobModel() {
    }

    public static JobModel getInstance() {
        if (mInstance == null) {
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
                        callback.onError(ErrorFactory.network(error));
                    }
                });
    }

    public void getJob(@NonNull final String title, @NonNull final ICallback<Job> callback) {
        final ICallback<List<Job>> listCallback = new ICallback<List<Job>>() {
            @Override
            public void onSuccess(List<Job> data) {
                for (Job job : data) {
                    if (job.getTitle().equals(title)) {
                        callback.onSuccess(job);
                        return;
                    }
                }
            }

            @Override
            public void onError(IError error) {
                callback.onError(error);
            }
        };

        if (mJobCache != null) {
            listCallback.onSuccess(mJobCache);
        } else {
            getJobs(listCallback);
        }
    }
}
