package edu.hm.cs.fs.app.presenter;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.List;

import edu.hm.cs.fs.app.database.CalendarModel;
import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.view.ICalendarView;

/**
 * Created by FHellman on 11.08.2015.
 */
public class CalendarPresenter extends BasePresenter<ICalendarView, CalendarModel> {
    /**
     * @param view
     */
    public CalendarPresenter(ICalendarView view) {
        super(view, CalendarModel.getInstance());
    }

    public void loadEvents(final int year, final int month) {
        getView().showLoading();
        getModel().loadEvent(year, month, new ICallback<List<WeekViewEvent>>() {
            @Override
            public void onSuccess(List<WeekViewEvent> data) {
                getView().showContent(data);
                getView().hideLoading();
            }

            @Override
            public void onError(String error) {
                getView().showError(error);
                getView().hideLoading();
            }
        });
    }
}
