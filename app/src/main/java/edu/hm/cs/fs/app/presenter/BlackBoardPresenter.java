package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.BlackBoardModel;
import edu.hm.cs.fs.app.database.ModelFactory;
import edu.hm.cs.fs.app.view.IBlackBoardView;
import edu.hm.cs.fs.common.model.BlackboardEntry;

/**
 * Created by FHellman on 10.08.2015.
 */
public class BlackBoardPresenter extends BasePresenter<IBlackBoardView, BlackBoardModel> {

    /**
     * @param view
     */
    public BlackBoardPresenter(IBlackBoardView view) {
        this(view, ModelFactory.getBlackboard());
    }

    /**
     * Needed for testing!
     */
    public BlackBoardPresenter(IBlackBoardView view, BlackBoardModel model) {
        super(view, model);
    }

    public void loadBlackBoard(final boolean refresh) {
        getView().showLoading();
        getModel().getAll(refresh, new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(@NonNull List<BlackboardEntry> data) {
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

    public void search(String newText) {
        getView().showLoading();
        getModel().getAllBySearchString(newText, new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(@NonNull List<BlackboardEntry> data) {
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
