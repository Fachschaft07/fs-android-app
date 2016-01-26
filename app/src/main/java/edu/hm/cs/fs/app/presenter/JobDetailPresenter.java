package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.job.JobDetailView;
import edu.hm.cs.fs.app.util.MarkdownUtil;
import edu.hm.cs.fs.common.model.simple.SimpleJob;

@PerActivity
public class JobDetailPresenter extends BasePresenter<JobDetailView> {
    @Inject
    public JobDetailPresenter() {
    }

    public void loadJob(@NonNull final String title) {
        getView().showLoading();
        getModel().jobByTitle(false, title).subscribe(new BasicSubscriber<SimpleJob>(getView()) {
            @Override
            public void onNext(SimpleJob data) {
                getView().showSubject(MarkdownUtil.toHtml(data.getTitle()));
                getView().showProvider(data.getProvider());
                getView().showDescription(MarkdownUtil.toHtml(data.getDescription()));
                getView().showUrl(data.getUrl());
                getView().showAuthor(data.getContact().getName());
                getView().hideLoading();
            }
        });
    }
}
