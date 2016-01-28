package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.fs.presence.PresenceListView;
import edu.hm.cs.fs.common.model.Presence;

@PerActivity
public class PresenceListPresenter extends BasePresenter<PresenceListView> {
    @Inject
    public PresenceListPresenter() {
    }

    public void loadPresence() {
        getView().showLoading();
        getView().clear();
        getModel().fsPresence().subscribe(new BasicSubscriber<Presence>(getView()) {
            @Override
            public void onNext(Presence presence) {
                getView().add(presence);
            }
        });
    }
}
