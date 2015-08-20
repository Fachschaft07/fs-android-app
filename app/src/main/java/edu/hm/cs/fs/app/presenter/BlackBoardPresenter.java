package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import edu.hm.cs.fs.app.database.model.BlackBoardModel;
import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.ModelFactory;
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
     *
     * @param view
     * @param model
     */
    public BlackBoardPresenter(IBlackBoardView view, BlackBoardModel model) {
        super(view, model);
    }

    public void loadBlackBoard(final boolean cache) {
        getView().showLoading();
        getModel().getAll(cache, new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(@NonNull List<BlackboardEntry> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
