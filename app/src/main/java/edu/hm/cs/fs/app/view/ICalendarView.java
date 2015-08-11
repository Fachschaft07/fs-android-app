package edu.hm.cs.fs.app.view;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.List;

import edu.hm.cs.fs.app.presenter.CalendarPresenter;

/**
 * @author Fabio
 */
public interface ICalendarView extends IView<CalendarPresenter> {
    void showContent(List<WeekViewEvent> content);
}
