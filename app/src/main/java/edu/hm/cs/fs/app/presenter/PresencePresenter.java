package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.model.FsModel;
import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.view.IPresenceView;
import edu.hm.cs.fs.common.model.Presence;

/**
 * Created by FHellman on 11.08.2015.
 */
public class PresencePresenter extends BasePresenter<IPresenceView, FsModel> {
    /**
     * @param view
     */
    public PresencePresenter(IPresenceView view) {
        this(view, ModelFactory.getFs());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public PresencePresenter(IPresenceView view, FsModel model) {
        super(view, model);
    }

    public void loadPresence() {
        getView().showLoading();
        getModel().getPresence(new ICallback<List<Presence>>() {
            @Override
            public void onSuccess(@NonNull List<Presence> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
