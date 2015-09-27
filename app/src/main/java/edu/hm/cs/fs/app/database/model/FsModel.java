package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.model.News;
import edu.hm.cs.fs.common.model.Presence;
import edu.hm.cs.fs.restclient.FsRestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Requests the data only for Fs.
 *
 * @author Fabio
 */
public class FsModel extends CachedModel<News> {

    /**
     * Get the presence.
     *
     * @param callback
     *         to retrieve the result.
     */
    public void getPresence(@NonNull final ICallback<List<Presence>> callback) {
        FsRestClient.getV1().getPresence(new Callback<List<Presence>>() {
            @Override
            public void success(List<Presence> presences, Response response) {
                callback.onSuccess(presences);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
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
                    if(title.equalsIgnoreCase(newsItem.getTitle())) {
                        callback.onSuccess(newsItem);
                        return;
                    }
                }
                callback.onSuccess(null);
            }

            @Override
            public void onError(@NonNull final IError error) {
                callback.onError(error);
            }
        });
    }

    @Override
    protected void update(@NonNull final ICallback<List<News>> callback) {
        FsRestClient.getV1().getNews(new Callback<List<News>>() {
            @Override
            public void success(final List<News> newses, final Response response) {
                callback.onSuccess(newses);
            }

            @Override
            public void failure(final RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
