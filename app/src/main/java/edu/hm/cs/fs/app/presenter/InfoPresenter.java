package edu.hm.cs.fs.app.presenter;

import android.content.Context;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.InfoModel;
import edu.hm.cs.fs.app.view.IInfoView;

/**
 * @author Fabio
 */
public class InfoPresenter extends BasePresenter<IInfoView, InfoModel> {
    /**
     *
     * @param context
     * @param view
     */
    public InfoPresenter(Context context, final IInfoView view) {
        this(view, InfoModel.getInstance(context));
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public InfoPresenter(IInfoView view, InfoModel model) {
        super(view, model);
    }

    public void loadVersion() {
        getView().showLoading();
        getModel().getVersion(new ICallback<String>() {
            @Override
            public void onSuccess(String data) {
                getView().showVersion(data);
                getView().hideLoading();
            }

            @Override
            public void onError(IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}