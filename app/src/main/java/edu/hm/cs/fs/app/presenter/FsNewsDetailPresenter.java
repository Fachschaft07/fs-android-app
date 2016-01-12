package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.Locale;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.fs.news.FsNewsDetailView;
import edu.hm.cs.fs.common.model.News;

@PerActivity
public class FsNewsDetailPresenter extends BasePresenter<FsNewsDetailView> {
    @Inject
    public FsNewsDetailPresenter() {
    }

    public void loadNews(@NonNull final String title) {
        getView().showLoading();
        getModel().fsNewsByTitle(title).subscribe(new BasicSubscriber<News>(getView()) {
            @Override
            public void onNext(News data) {
                if (data != null) {
                    getView().showTitle(data.getTitle());
                    getView().showDescription(data.getDescription());
                    getView().showDate(String.format(Locale.getDefault(), "%1$td.%1$tm.%1$ty", data.getDate()));
                    getView().showLink(data.getLink());
                }
            }
        });
    }
}
