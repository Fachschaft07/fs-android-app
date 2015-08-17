package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.JobModel;
import edu.hm.cs.fs.app.view.IJobView;
import edu.hm.cs.fs.common.model.Job;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobPresenter extends BasePresenter<IJobView, JobModel> {
    /**
     * @param view
     */
    public JobPresenter(IJobView view) {
        this(view, JobModel.getInstance());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public JobPresenter(IJobView view, JobModel model) {
        super(view, model);
    }

    public void loadJobs(final boolean cache) {
        getView().showLoading();
        getModel().getJobs(cache, new ICallback<List<Job>>() {
            @Override
            public void onSuccess(List<Job> data) {
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
