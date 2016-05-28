package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for blackboard.
 *
 * @author Fabio
 */
public class BlackBoardModel extends CachedModel<BlackboardEntry> {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    /**
     * Get all blackboard entries.
     *
     * @param refresh  should set to <code>true</code> if the blackboard entries should be updated
     *                 from the web.
     * @param callback to retrieve the result.
     */
    public void getAll(final boolean refresh, @NonNull final ICallback<List<BlackboardEntry>> callback) {
        getData(refresh, callback);
    }

    public void getAllSinceYesterday(@NonNull final ICallback<List<BlackboardEntry>> callback) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        getAllSince(calendar.getTimeInMillis(), callback);
    }

    /**
     * Get a specific blackboard entry by id.
     *
     * @param id       of the blackboard entry.
     * @param callback to retrieve the result.
     */
    public void getItem(@NonNull final String id, @NonNull final ICallback<BlackboardEntry> callback) {
        getData(false, new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(@NonNull List<BlackboardEntry> data) {
                for (BlackboardEntry entry : data) {
                    if (id.equals(entry.getId())) {
                        callback.onSuccess(entry);
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
    public void update(@NonNull final ICallback<List<BlackboardEntry>> callback) {
        REST_CLIENT.getEntries().enqueue(new Callback<List<BlackboardEntry>>() {
            @Override
            public void onResponse(Call<List<BlackboardEntry>> call, Response<List<BlackboardEntry>> response) {
                final List<BlackboardEntry> blackboardEntries = response.body();
                Collections.sort(blackboardEntries, new Comparator<BlackboardEntry>() {
                    @Override
                    public int compare(BlackboardEntry lhs, BlackboardEntry rhs) {
                        return -1 * lhs.getPublish().compareTo(rhs.getPublish()); // DESC
                    }
                });
                callback.onSuccess(blackboardEntries);
            }

            @Override
            public void onFailure(Call<List<BlackboardEntry>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getAllBySearchString(@NonNull final String newText,
                                     @NonNull final ICallback<List<BlackboardEntry>> callback) {
        REST_CLIENT.getEntries(newText).enqueue(new Callback<List<BlackboardEntry>>() {
            @Override
            public void onResponse(Call<List<BlackboardEntry>> call, Response<List<BlackboardEntry>> response) {
                final List<BlackboardEntry> blackboardEntries = response.body();
                Collections.sort(blackboardEntries, new Comparator<BlackboardEntry>() {
                    @Override
                    public int compare(BlackboardEntry lhs, BlackboardEntry rhs) {
                        return -1 * lhs.getPublish().compareTo(rhs.getPublish()); // DESC
                    }
                });
                callback.onSuccess(blackboardEntries);
            }

            @Override
            public void onFailure(Call<List<BlackboardEntry>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getAllSince(final long time,
                            @NonNull final ICallback<List<BlackboardEntry>> callback) {
        REST_CLIENT.getEntriesSince(time).enqueue(new Callback<List<BlackboardEntry>>() {
            @Override
            public void onResponse(Call<List<BlackboardEntry>> call, Response<List<BlackboardEntry>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<BlackboardEntry>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
