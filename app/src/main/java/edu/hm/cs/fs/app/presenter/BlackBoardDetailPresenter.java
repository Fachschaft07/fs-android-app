package edu.hm.cs.fs.app.presenter;

import edu.hm.cs.fs.app.database.BlackBoardModel;
import edu.hm.cs.fs.app.view.IBlackBoardDetailView;

/**
 * Created by FHellman on 11.08.2015.
 */
public class BlackBoardDetailPresenter extends BasePresenter<IBlackBoardDetailView, BlackBoardModel> {
    /**
     * @param view
     */
    public BlackBoardDetailPresenter(IBlackBoardDetailView view) {
        super(view, BlackBoardModel.getInstance());
    }
}
