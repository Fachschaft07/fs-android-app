package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardDetailView;
import edu.hm.cs.fs.app.util.MarkdownUtil;
import edu.hm.cs.fs.common.model.BlackboardEntry;

@PerActivity
public class BlackBoardDetailPresenter extends BasePresenter<BlackBoardDetailView> {
    @Inject
    public BlackBoardDetailPresenter() {
    }

    public void loadBlackBoardEntry(@NonNull final String id) {
        getView().showLoading();
        getModel().blackboardEntrieById(false, id)
                .subscribe(new BasicSubscriber<BlackboardEntry>(getView()) {
                    @Override
                    public void onNext(BlackboardEntry data) {
                        getView().showSubject(MarkdownUtil.toHtml(data.getSubject()));
                        final String groups = data.getGroups().toString();
                        getView().showGroups(groups.substring(1, groups.length() - 1));
                        getView().showDescription(MarkdownUtil.toHtml(data.getText()));
                        getView().showAuthor(data.getAuthor().getName());
                        getView().showUrl(data.getUrl());
                    }
                });
    }
}
