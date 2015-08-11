package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.FsModel;
import edu.hm.cs.fs.app.database.ICallback;
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
        super(view, FsModel.getInstance());
    }

    public void loadPresence() {
        getView().showLoading();
        getModel().getPresence(new ICallback<List<Presence>>() {
            @Override
            public void onSuccess(List<Presence> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(String error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
