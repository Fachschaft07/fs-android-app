package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.fs.news.FsNewsListView;
import edu.hm.cs.fs.common.model.News;

@PerActivity
public class FsNewsListPresenter extends BasePresenter<FsNewsListView> {
    @Inject
    public FsNewsListPresenter() {
    }

    public void loadNews(final boolean refresh) {
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().fsNews(refresh).subscribe(new BasicSubscriber<News>(getView()) {
            @Override
            public void onNext(News news) {
                getView().add(news);
            }
        }));
    }
}
