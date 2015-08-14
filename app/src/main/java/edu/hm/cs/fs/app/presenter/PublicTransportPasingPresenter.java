package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
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
        this(view, PublicTransportModel.getInstance());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public PublicTransportPasingPresenter(IPublicTransportView view, PublicTransportModel model) {
        super(view, model);
    }

    public void loadPublicTransports() {
        getView().showLoading();
        getModel().getDepartureTimesForPasing(new ICallback<List<PublicTransport>>() {
            @Override
            public void onSuccess(List<PublicTransport> data) {
                getView().showContent(data);
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
