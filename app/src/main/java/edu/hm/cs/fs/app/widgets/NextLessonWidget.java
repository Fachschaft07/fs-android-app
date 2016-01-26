package edu.hm.cs.fs.app.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fk07.R;

import java.util.Calendar;

import javax.inject.Inject;

import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.domain.DataService;
import rx.Subscriber;

/**
 * Implementation of App Widget functionality.
 */
public class NextLessonWidget extends AppWidgetProvider {
    private static final String DAY_TIME_FORMAT = "%1$ta. %1$tH:%1$tM - %2$tH:%2$tM";

    @Inject
    DataService mDataService;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        App.getAppComponent(context).inject(this);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                 final int appWidgetId) {
        mDataService.nextLesson().subscribe(new Subscriber<Lesson>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Lesson lesson) {
                final String subject;
                final String dateTime;
                final String room;

                if (lesson != null) {
                    subject = lesson.getModule().getName();
                    room = lesson.getRoom();

                    Calendar calendarLessonStart = Calendar.getInstance();
                    calendarLessonStart.set(Calendar.DAY_OF_WEEK, lesson.getDay().getCalendarId());
                    calendarLessonStart.set(Calendar.HOUR_OF_DAY, lesson.getHour());
                    calendarLessonStart.set(Calendar.MINUTE, lesson.getMinute());

                    Calendar calendarLessonEnd = Calendar.getInstance();
                    calendarLessonEnd.setTime(calendarLessonStart.getTime());
                    calendarLessonEnd.add(Calendar.MINUTE, 90);

                    dateTime = String.format(DAY_TIME_FORMAT, calendarLessonStart,
                            calendarLessonEnd);
                } else {
                    subject = "";
                    dateTime = "";
                    room = "";
                }

                // Construct the RemoteViews object
                final RemoteViews views = new RemoteViews(context.getPackageName(),
                        R.layout.next_lesson_widget);

                views.setTextViewText(R.id.textSubject, subject);
                views.setTextViewText(R.id.textDateTime, dateTime);
                views.setTextViewText(R.id.textRoom, room);

                // Instruct the widget manager to updateOnline the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        });
    }
}
