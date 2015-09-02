package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.database.model.PublicTransportModel;
import edu.hm.cs.fs.app.view.IPublicTransportView;
import edu.hm.cs.fs.common.model.PublicTransport;

/**
 * Created by FHellman on 10.08.2015.
 */
public class PublicTransportPasingPresenter extends BasePresenter<IPublicTransportView, PublicTransportModel> {

    /**
     * @param view
     */
    public PublicTransportPasingPresenter(IPublicTransportView view) {
        this(view, ModelFactory.getPublicTransport());
    }

    /**
     * Needed for testing!
     */
    public PublicTransportPasingPresenter(IPublicTransportView view, PublicTransportModel model) {
        super(view, model);
    }

    public void loadPublicTransports() {
        getView().showLoading();
        getModel().getPasing(new ICallback<List<PublicTransport>>() {
            @Override
            public void onSuccess(@NonNull List<PublicTransport> data) {
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
