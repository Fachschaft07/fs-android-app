package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.LostFoundModel;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.view.ILostFoundView;
import edu.hm.cs.fs.common.model.LostFound;

/**
 * @author Fabio
 */
public class LostFoundPresenter extends BasePresenter<ILostFoundView, LostFoundModel> {

    /**
     * @param view
     */
    public LostFoundPresenter(ILostFoundView view) {
        super(view, ModelFactory.getLostFound());
    }

    /**
     * Needed for testing!
     */
    public LostFoundPresenter(ILostFoundView view, LostFoundModel model) {
        super(view, model);
    }

    public void loadLostFound() {
        getView().showLoading();
        getModel().getLostFound(new ICallback<List<LostFound>>() {
            @Override
            public void onSuccess(@NonNull List<LostFound> data) {
                Collections.sort(data, new Comparator<LostFound>() {
                    @Override
                    public int compare(LostFound lhs, LostFound rhs) {
                        return -1 * lhs.getDate().compareTo(rhs.getDate());
                    }
                });
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
