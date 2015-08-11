package edu.hm.cs.fs.app.presenter;

import android.content.Context;

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
        super(view, InfoModel.getInstance(context));
    }

    public void loadVersion() {
        getView().showVersion(getModel().getVersion());
    }
}
