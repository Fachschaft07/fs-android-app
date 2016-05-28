package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.JobModel;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.view.IJobView;
import edu.hm.cs.fs.common.model.simple.SimpleJob;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobPresenter extends BasePresenter<IJobView, JobModel> {

    /**
     * @param view
     */
    public JobPresenter(IJobView view) {
        this(view, ModelFactory.getJob());
    }

    /**
     * Needed for testing!
     */
    public JobPresenter(IJobView view, JobModel model) {
        super(view, model);
    }

    public void loadJobs(final boolean refresh) {
        getView().showLoading();
        getModel().getAll(refresh, new ICallback<List<SimpleJob>>() {
            @Override
            public void onSuccess(@NonNull List<SimpleJob> data) {
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
