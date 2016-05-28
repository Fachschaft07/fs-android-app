package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.FsModel;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.view.IFsNewsView;
import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public class FsNewsPresenter extends BasePresenter<IFsNewsView, FsModel> {
    /**
     * @param view
     */
    public FsNewsPresenter(final IFsNewsView view) {
        super(view, ModelFactory.getFs());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public FsNewsPresenter(final IFsNewsView view, final FsModel model) {
        super(view, model);
    }

    public void loadNews(final boolean refresh) {
        getView().showLoading();
        getModel().getNews(refresh, new ICallback<List<News>>() {
            @Override
            public void onSuccess(final List<News> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull final Throwable e) {
                getView().showError(e);
                getView().hideLoading();
            }
        });
    }
}
