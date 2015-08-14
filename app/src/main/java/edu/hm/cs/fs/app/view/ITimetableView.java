package edu.hm.cs.fs.app.view;

import java.util.List;

import edu.hm.cs.fs.app.presenter.TimetablePresenter;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * @author Fabio
 */
public interface ITimetableView extends IView<TimetablePresenter> {
    void showContent(List<Lesson> content);
}
