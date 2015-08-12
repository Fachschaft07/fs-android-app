package edu.hm.cs.fs.app.presenter;

import edu.hm.cs.fs.app.database.HomeModel;
import edu.hm.cs.fs.app.view.IHomeView;

/**
 * Created by FHellman on 12.08.2015.
 */
public class HomePresenter extends BasePresenter<IHomeView, HomeModel> {
    /**
     * @param view
     */
    public HomePresenter(IHomeView view) {
        super(view, HomeModel.getInstance());
    }
}
