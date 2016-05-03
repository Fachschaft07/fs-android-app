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
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().blackboardEntries(refresh)
                .subscribe(new BasicSubscriber<BlackboardEntry>(getView()) {
                    @Override
                    public void onNext(BlackboardEntry blackboardEntry) {
                        getView().add(blackboardEntry);
                    }
                }));
    }

    public void search(String newText) {
        if(checkSubscriber()) {
            return;
        }
        getView().showLoading();
        getView().clear();
        setSubscriber(getModel().blackboardEntriesBySearchString(false, newText)
                .subscribe(new BasicSubscriber<BlackboardEntry>(getView()) {
                    @Override
                    public void onNext(BlackboardEntry blackboardEntry) {
                        getView().add(blackboardEntry);
                    }
                }));
    }
}
