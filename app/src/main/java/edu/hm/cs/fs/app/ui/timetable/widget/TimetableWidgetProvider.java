package edu.hm.cs.fs.app.ui.timetable.widget;

import java.io.File;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.fk07.R;
import edu.hm.cs.fs.app.ui.timetable.TimetableDayActivity;
import edu.hm.cs.fs.app.ui.timetable.xml.TimetableHandler;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Entry;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Timetable;

public class TimetableWidgetProvider extends AppWidgetProvider {
	
	private final String TAG = "TimetableWidgetProvider";
	
	private Context context;
	
	private Timetable timetable = null;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Log.d(TAG, "onUpdate");
		this.context = context;
		
		Intent intent = new Intent(context, TimetableDayActivity.class);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timetable_appwidget);
		for (int i = 0; i < appWidgetIds.length; i++) {
			int appWidgetId = appWidgetIds[i];
			if (loadTimetable()) {
				Entry nextLecture = TimetableHandler.getCurrentTimeEntry(timetable);
				if (nextLecture != null) {
					intent.putExtra("Day", nextLecture.getDay());
					views.setTextViewText(R.id.weekDay, nextLecture.getDay());
					views.setTextViewText(R.id.timeFrom, nextLecture.getTime());
					views.setTextViewText(R.id.course, nextLecture.getTitle());
					views.setTextViewText(R.id.room, nextLecture.getRoom());
				} else {
					views.setTextViewText(R.id.weekDay, "");
					views.setTextViewText(R.id.timeFrom, "");
					views.setTextViewText(R.id.course, context.getString(R.string.widgetPlainText));
					views.setTextViewText(R.id.room, "");
				}
			} else {
				views.setTextViewText(R.id.weekDay, "");
				views.setTextViewText(R.id.timeFrom, "");
				views.setTextViewText(R.id.course, context.getString(R.string.widgetPlainText));
				views.setTextViewText(R.id.room, "");
			}
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
	        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
	
	private boolean loadTimetable() {
		File timetableFile = new File(context.getFilesDir() + TimetableHandler.TIMETABLE_FILE);
		
		if (timetableFile.exists()) {
			timetable = TimetableHandler.readTimetable(timetableFile);
			if (timetable != null) {
				return true;
			}
		}
		return false;
	}
}
