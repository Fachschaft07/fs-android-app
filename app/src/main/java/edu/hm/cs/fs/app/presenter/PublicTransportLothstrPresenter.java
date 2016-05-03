package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.publictransport.PublicTransportListView;
import edu.hm.cs.fs.common.model.PublicTransport;

@PerActivity
public class PublicTransportLothstrPresenter extends BasePresenter<PublicTransportListView> {
    @Inject
    public PublicTransportLothstrPresenter() {
    }

    public void loadPublicTransports() {
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().publicTransportLothstrasse().subscribe(new BasicSubscriber<PublicTransport>(getView()) {
            @Override
            public void onNext(PublicTransport publicTransport) {
                getView().add(publicTransport);
            }
        }));
    }
}
