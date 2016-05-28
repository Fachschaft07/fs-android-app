package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.InfoModel;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.view.IInfoView;

/**
 * @author Fabio
 */
public class InfoPresenter extends BasePresenter<IInfoView, InfoModel> {

    /**
     * @param context
     * @param view
     */
    public InfoPresenter(Context context, final IInfoView view) {
        this(view, ModelFactory.getInfo(context));
    }

    /**
     * Needed for testing!
     */
    public InfoPresenter(IInfoView view, InfoModel model) {
        super(view, model);
    }

    public void loadVersion() {
        getView().showLoading();
        getModel().getVersion(new ICallback<String>() {
            @Override
            public void onSuccess(@NonNull String data) {
                getView().showVersion(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().showError(e);
                getView().hideLoading();
            }
        });
    }
}
