package edu.hm.cs.fs.app.presenter;

import java.util.List;

import edu.hm.cs.fs.app.database.BlackBoardModel;
import edu.hm.cs.fs.app.database.ICallback;
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
        super(view, BlackBoardModel.getInstance());
    }

    public void loadBlackBoard() {
        getView().showLoading();
        getModel().getBlackBoard(new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(List<BlackboardEntry> data) {
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
