package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
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
 * Created by FHellman on 10.08.2015.
 */
public class BlackBoardModel implements IModel {
    private static BlackBoardModel mInstance;
    private List<BlackboardEntry> mDataCache = new ArrayList<>();

    private BlackBoardModel() {
    }

    public static BlackBoardModel getInstance() {
        if (mInstance == null) {
            mInstance = new BlackBoardModel();
        }
        return mInstance;
    }

    public void getBlackBoard(final boolean cache,
                              @NonNull final ICallback<List<BlackboardEntry>> callback) {
        if(cache && !mDataCache.isEmpty()) {
            callback.onSuccess(mDataCache);
        } else {
            Controllers.create(BlackboardController.class)
                    .getEntries(new Callback<List<BlackboardEntry>>() {
                        @Override
                        public void success(List<BlackboardEntry> blackboardEntries, Response response) {
                            mDataCache = blackboardEntries;
                            callback.onSuccess(blackboardEntries);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            callback.onError(ErrorFactory.network(error));
                        }
                    });
        }
    }

    public void getBlackBoardEntry(@NonNull final String id, @NonNull final ICallback<BlackboardEntry> callback) {
        final ICallback<List<BlackboardEntry>> listCallback = new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(List<BlackboardEntry> data) {
                for (BlackboardEntry entry : mDataCache) {
                    if (id.equals(entry.getId())) {
                        callback.onSuccess(entry);
                        return;
                    }
                }
            }

            @Override
            public void onError(IError error) {
                callback.onError(error);
            }
        };

        if(mDataCache != null) {
            listCallback.onSuccess(mDataCache);
        } else {
            getBlackBoard(false, listCallback);
        }
    }
}
