package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.JobModel;
import edu.hm.cs.fs.app.view.IJobDetailView;
import edu.hm.cs.fs.common.model.Job;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobDetailPresenter extends BasePresenter<IJobDetailView, JobModel> {
    /**
     * @param view
     */
    public JobDetailPresenter(IJobDetailView view) {
        super(view, JobModel.getInstance());
    }

    public void loadJob(@NonNull final String id) {
        getView().showLoading();
        getModel().getJob(id, new ICallback<Job>() {
            @Override
            public void onSuccess(Job data) {
                getView().showSubject(data.getTitle());
                getView().showProvider(data.getProvider());
                getView().showDescription(data.getDescription());
                getView().showUrl(data.getUrl());
                getView().showAuthor(data.getContact().getTitle() + " " +
                        data.getContact().getLastName() + " " +
                        data.getContact().getFirstName());
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
