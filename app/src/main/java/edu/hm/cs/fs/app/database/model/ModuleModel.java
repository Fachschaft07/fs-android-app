package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.ErrorFactory;
import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.restclient.FsRestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author Fabio
 */
public class ModuleModel implements IModel {
    public void getModuleById(@NonNull final String moduleId,
                              @NonNull final ICallback<Module> callback) {
        FsRestClient.getV1().getModuleById(moduleId, new Callback<Module>() {
            @Override
            public void success(Module module, Response response) {
                callback.onSuccess(module);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError(ErrorFactory.http(error));
            }
        });
    }
}
