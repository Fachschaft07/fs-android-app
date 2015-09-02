package edu.hm.cs.fs.app.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.fk07.R;

import java.util.Calendar;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.database.model.TimetableModel;
import edu.hm.cs.fs.common.model.Lesson;

/**
 * Implementation of App Widget functionality.
 */
public class NextLessonWidget extends AppWidgetProvider {
    private static final String DAY_TIME_FORMAT = "%1$ta. %2$tH:%2$tM - %3$tH:%3$tM";

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
        final TimetableModel timetable = ModelFactory.getTimetable(context);
        timetable.getNextLesson(new ICallback<Lesson>() {
            @Override
            public void onSuccess(@Nullable Lesson data) {
                final String subject;
                final String dateTime;
                final String room;

                if (data != null) {
                    subject = data.getModule().getName();
                    room = data.getRoom();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, data.getDay().getCalendarId());
                    dateTime = String.format(DAY_TIME_FORMAT, calendar,
                            data.getTime().getStart(), data.getTime().getEnd());
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

            @Override
            public void onError(@NonNull IError error) {
                // TODO Add error message?
            }
        });
    }
}
