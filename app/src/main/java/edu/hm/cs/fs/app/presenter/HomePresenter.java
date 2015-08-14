package edu.hm.cs.fs.app.presenter;

import edu.hm.cs.fs.app.database.model.HomeModel;
import edu.hm.cs.fs.app.view.IHomeView;

/**
 * Created by FHellman on 12.08.2015.
 */
public class HomePresenter extends BasePresenter<IHomeView, HomeModel> {
    /**
     * @param view
     */
    public HomePresenter(IHomeView view) {
        this(view, HomeModel.getInstance());
    }

    /**
     * Needed for testing!
     *
     * @param view
     * @param model
     */
    public HomePresenter(IHomeView view, HomeModel model) {
        super(view, model);
    }
}
