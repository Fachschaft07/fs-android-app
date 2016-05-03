package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.job.JobListView;
import edu.hm.cs.fs.common.model.simple.SimpleJob;

@PerActivity
public class JobListPresenter extends BasePresenter<JobListView> {
    @Inject
    public JobListPresenter() {
    }

    public void loadJobs(final boolean refresh) {
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().jobs(refresh).subscribe(new BasicSubscriber<SimpleJob>(getView()) {
            @Override
            public void onNext(SimpleJob simpleJob) {
                getView().add(simpleJob);
            }
        }));
    }
}
