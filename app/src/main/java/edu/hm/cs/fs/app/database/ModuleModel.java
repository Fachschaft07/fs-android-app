package edu.hm.cs.fs.app.database;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.common.model.Module;
import edu.hm.cs.fs.restclient.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Fabio
 */
public class ModuleModel implements IModel {
    private static final RestClient REST_CLIENT = new RestClient.Builder().build();

    public void getModuleById(@NonNull final String moduleId,
                              @NonNull final ICallback<Module> callback) {
        REST_CLIENT.getModuleById(moduleId).enqueue(new Callback<Module>() {
            @Override
            public void onResponse(Call<Module> call, Response<Module> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Module> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
