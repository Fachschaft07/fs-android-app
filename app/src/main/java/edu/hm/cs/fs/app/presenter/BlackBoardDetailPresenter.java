package edu.hm.cs.fs.app.presenter;

import android.support.annotation.NonNull;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.BlackBoardModel;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.util.MarkdownUtil;
import edu.hm.cs.fs.app.view.IBlackBoardDetailView;
import edu.hm.cs.fs.common.model.BlackboardEntry;

/**
 * Created by FHellman on 11.08.2015.
 */
public class BlackBoardDetailPresenter extends BasePresenter<IBlackBoardDetailView, BlackBoardModel> {

    /**
     * @param view
     */
    public BlackBoardDetailPresenter(IBlackBoardDetailView view) {
        this(view, ModelFactory.getBlackboard());
    }

    /**
     * Needed for testing!
     */
    public BlackBoardDetailPresenter(IBlackBoardDetailView view, BlackBoardModel model) {
        super(view, model);
    }

    public void loadBlackBoardEntry(@NonNull final String id) {
        getView().showLoading();
        getModel().getItem(id, new ICallback<BlackboardEntry>() {
            @Override
            public void onSuccess(@NonNull BlackboardEntry data) {
                getView().showSubject(MarkdownUtil.toHtml(data.getSubject()));
                final String groups = data.getGroups().toString();
                getView().showGroups(groups.substring(1, groups.length() - 1));
                getView().showDescription(MarkdownUtil.toHtml(data.getText()));
                getView().showAuthor(data.getAuthor().getTitle() + " " + data.getAuthor().getLastName());
                getView().showUrl(data.getUrl());
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
