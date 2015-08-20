package edu.hm.cs.fs.app.view;

import android.support.annotation.NonNull;

import com.dexafree.materialList.card.Card;

import edu.hm.cs.fs.app.presenter.HomePresenter;

/**
 * @author Fabio
 */
public interface IHomeView extends IView<HomePresenter> {
    void showContent(@NonNull final Card card);

    void clear();
}
