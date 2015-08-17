package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

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
    private List<BlackboardEntry> mData;

    private BlackBoardModel() {
    }

    public static BlackBoardModel getInstance() {
        if (mInstance == null) {
            mInstance = new BlackBoardModel();
        }
        return mInstance;
    }

    public void getBlackBoard(@NonNull final ICallback<List<BlackboardEntry>> callback) {
        Controllers.create(BlackboardController.class)
                .getEntries(new Callback<List<BlackboardEntry>>() {
            @Override
            public void success(List<BlackboardEntry> blackboardEntries, Response response) {
                mData = blackboardEntries;
                callback.onSuccess(blackboardEntries);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.network(error));
            }
        });
    }

    public void getBlackBoardEntry(@NonNull final String id, @NonNull final ICallback<BlackboardEntry> callback) {
        final ICallback<List<BlackboardEntry>> listCallback = new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(List<BlackboardEntry> data) {
                for (BlackboardEntry entry : mData) {
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

        if(mData != null) {
            listCallback.onSuccess(mData);
        } else {
            getBlackBoard(listCallback);
        }
    }
}
