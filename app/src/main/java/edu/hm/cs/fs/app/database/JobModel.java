package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.simple.SimpleJob;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for jobs.
 *
 * @author Fabio
 */
public class JobModel extends CachedModel<SimpleJob> {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

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
            public void onError(@NonNull Throwable e) {
                callback.onError(e);
            }
        });
    }

    @Override
    protected void update(@NonNull final ICallback<List<SimpleJob>> callback) {
        REST_CLIENT.getJobs().enqueue(new Callback<List<SimpleJob>>() {
            @Override
            public void onResponse(Call<List<SimpleJob>> call, Response<List<SimpleJob>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<SimpleJob>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
