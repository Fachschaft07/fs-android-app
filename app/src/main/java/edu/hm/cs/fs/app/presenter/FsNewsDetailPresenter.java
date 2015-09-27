package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Locale;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.FsModel;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.view.IFsNewsDetailView;
import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public class FsNewsDetailPresenter extends BasePresenter<IFsNewsDetailView, FsModel> {
    /**
     * @param view
     */
    public FsNewsDetailPresenter(final IFsNewsDetailView view) {
        super(view, ModelFactory.getFs());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public FsNewsDetailPresenter(final IFsNewsDetailView view, final FsModel model) {
        super(view, model);
    }

    public void loadNews(@NonNull final String title) {
        getView().showLoading();
        getModel().getNewsByTitle(title, new ICallback<News>() {
            @Override
            public void onSuccess(@Nullable final News data) {
                if(data != null) {
                    getView().showTitle(data.getTitle());
                    getView().showDescription(data.getDescription());
                    getView().showDate(String.format(Locale.getDefault(), "%1$td.%1$tm.%1$ty", data.getDate()));
                    getView().showLink(data.getLink());
                }
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull final IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
