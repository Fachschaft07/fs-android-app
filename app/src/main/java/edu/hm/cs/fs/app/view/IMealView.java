package edu.hm.cs.fs.app.view;

import java.util.List;

import edu.hm.cs.fs.app.presenter.MealPresenter;
import edu.hm.cs.fs.common.model.Meal;

/**
 * Created by Fabio on 12.07.2015.
 */
public interface IMealView extends IView<MealPresenter> {
    void showContent(List<Meal> content);
}
