package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.JobModel;
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
        super(view, JobModel.getInstance());
    }

    public void loadJobs() {
        getView().showLoading();
        getModel().getJobs(new ICallback<List<Job>>() {
            @Override
            public void onSuccess(List<Job> data) {
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
