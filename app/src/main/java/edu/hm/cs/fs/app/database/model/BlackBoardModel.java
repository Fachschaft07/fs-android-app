package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.restclient.BlackboardController;
import edu.hm.cs.fs.restclient.Controllers;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Fabio
 */
public class BlackBoardModel extends CachedModel<BlackboardEntry> {
    public void getAll(final boolean refresh,
                       @NonNull final ICallback<List<BlackboardEntry>> callback) {
        getData(refresh, callback);
    }

    public void getItem(@NonNull final String id,
                        @NonNull final ICallback<BlackboardEntry> callback) {
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
            public void onError(@NonNull IError error) {
                callback.onError(error);
            }
        });
    }

    @Override
    public void update(@NonNull final ICallback<List<BlackboardEntry>> callback) {
        Controllers.create(BlackboardController.class)
                .getEntries(new Callback<List<BlackboardEntry>>() {
                    @Override
                    public void success(List<BlackboardEntry> blackboardEntries, Response response) {
                        Collections.sort(blackboardEntries, new Comparator<BlackboardEntry>() {
                            @Override
                            public int compare(BlackboardEntry lhs, BlackboardEntry rhs) {
                                return -1 * lhs.getPublish().compareTo(rhs.getPublish()); // DESC
                            }
                        });
                        callback.onSuccess(blackboardEntries);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onError(ErrorFactory.http(error));
                    }
                });
    }
}
