package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.lostfound.LostFoundListView;
import edu.hm.cs.fs.common.model.LostFound;

@PerActivity
public class LostFoundListPresenter extends BasePresenter<LostFoundListView> {
    @Inject
    public LostFoundListPresenter() {
    }

    public void loadLostFound(@NonNull final Boolean refresh) {
        getView().showLoading();
        getModel().lostfound(refresh).subscribe(new BasicSubscriber<LostFound>(getView()) {
            @Override
            public void onNext(LostFound lostFound) {
                getView().add(lostFound);
            }
        });
    }
}
