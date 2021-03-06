package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.database.PublicTransportModel;
import edu.hm.cs.fs.app.view.IPublicTransportView;
import edu.hm.cs.fs.common.model.PublicTransport;

/**
 * Created by FHellman on 10.08.2015.
 */
public class PublicTransportLothstrPresenter extends BasePresenter<IPublicTransportView, PublicTransportModel> {

    /**
     * @param view
     */
    public PublicTransportLothstrPresenter(IPublicTransportView view) {
        this(view, ModelFactory.getPublicTransport());
    }

    /**
     * Needed for testing!
     */
    public PublicTransportLothstrPresenter(IPublicTransportView view, PublicTransportModel model) {
        super(view, model);
    }

    public void loadPublicTransports() {
        getView().showLoading();
        getModel().getLothstrasse(new ICallback<List<PublicTransport>>() {
            @Override
            public void onSuccess(@NonNull List<PublicTransport> data) {
                getView().showContent(data);
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
