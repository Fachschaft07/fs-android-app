package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.common.model.News;
import edu.hm.cs.fs.common.model.Presence;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Requests the data only for Fs.
 *
 * @author Fabio
 */
public class FsModel extends CachedModel<News> {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    /**
     * Get the presence.
     *
     * @param callback to retrieve the result.
     */
    public void getPresence(@NonNull final ICallback<List<Presence>> callback) {
        REST_CLIENT.getPresence().enqueue(new Callback<List<Presence>>() {
            @Override
            public void onResponse(Call<List<Presence>> call, Response<List<Presence>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Presence>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    /**
     * @param callback
     */
    public void getNews(final boolean refresh, @NonNull final ICallback<List<News>> callback) {
        getData(refresh, callback);
    }

    public void getNewsByTitle(@NonNull final String title, @NonNull final ICallback<News> callback) {
        getNews(false, new ICallback<List<News>>() {
            @Override
            public void onSuccess(final List<News> data) {
                for (News newsItem : data) {
                    if (title.equalsIgnoreCase(newsItem.getTitle())) {
                        callback.onSuccess(newsItem);
                        return;
                    }
                }
                callback.onSuccess(null);
            }

            @Override
            public void onError(@NonNull final Throwable e) {
                callback.onError(e);
            }
        });
    }

    @Override
    protected void update(@NonNull final ICallback<List<News>> callback) {
        REST_CLIENT.getNews().enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
