package edu.hm.cs.fs.app.presenter;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardListView;
import edu.hm.cs.fs.common.model.BlackboardEntry;

@PerActivity
public class BlackBoardListPresenter extends BasePresenter<BlackBoardListView> {
    @Inject
    BlackBoardListPresenter() {
    }

    public void loadBlackBoard(final boolean refresh) {
        getView().showLoading();
        getView().clear();
        getModel().blackboardEntries(refresh)
                .subscribe(new BasicSubscriber<BlackboardEntry>(getView()) {
                    @Override
                    public void onNext(BlackboardEntry blackboardEntry) {
                        getView().add(blackboardEntry);
                    }
                });
    }

    public void search(String newText) {
        getView().showLoading();
        getView().clear();
        getModel().blackboardEntriesBySearchString(false, newText)
                .subscribe(new BasicSubscriber<BlackboardEntry>(getView()) {
                    @Override
                    public void onNext(BlackboardEntry blackboardEntry) {
                        getView().add(blackboardEntry);
                    }
                });
    }
}
