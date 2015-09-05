package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.model.simple.SimpleJob;
import edu.hm.cs.fs.restclient.FsRestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data only for jobs.
 *
 * @author Fabio
 */
public class JobModel extends CachedModel<SimpleJob> {

    /**
     * Get all job entries.
     *
     * @param refresh  should set to <code>true</code> if the blackboard entries should be updated
     *                 from the web.
     * @param callback to retrieve the result.
     */
    public void getAll(final boolean refresh, @NonNull final ICallback<List<SimpleJob>> callback) {
        getData(refresh, callback);
    }

    /**
     * Get a specific job entry by title.
     *
     * @param title    of the job entry.
     * @param callback to retrieve the result.
     */
    public void getItem(@NonNull final String title, @NonNull final ICallback<SimpleJob> callback) {
        getData(false, new ICallback<List<SimpleJob>>() {
            @Override
            public void onSuccess(@NonNull List<SimpleJob> data) {
                for (SimpleJob job : data) {
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
    protected void updateOnline(@NonNull final ICallback<List<SimpleJob>> callback) {
        FsRestClient.getV1().getJobs(new Callback<List<SimpleJob>>() {
            @Override
            public void success(List<SimpleJob> jobs, Response response) {
                callback.onSuccess(jobs);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
