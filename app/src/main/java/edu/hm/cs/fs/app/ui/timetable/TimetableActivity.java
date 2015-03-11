package edu.hm.cs.fs.app.ui.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fk07.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.timetable.dialogs.DownloadDialog;
import edu.hm.cs.fs.app.ui.timetable.dialogs.DownloadFK10Dialog;
import edu.hm.cs.fs.app.ui.timetable.xml.FK10Handler;
import edu.hm.cs.fs.app.ui.timetable.xml.FK7Handler;
import edu.hm.cs.fs.app.ui.timetable.xml.TimetableHandler;
import edu.hm.cs.fs.app.ui.timetable.xml.groups.FK07Group;
import edu.hm.cs.fs.app.ui.timetable.xml.timetable.Timetable;
import edu.hm.cs.fs.app.ui.timetable.xml.timetablefk10.FK10Group;
import edu.hm.cs.fs.app.util.DownloadException;
import edu.hm.cs.fs.app.util.NetworkUtils;

/**
 * TimetableActivity
 * 
 * This Activity is the super class for TimetableDayActivity and
 * TimetableWeekActivity. It contains all Methods that are used from both
 * Activites.
 * 
 * @author Rene
 */
public abstract class TimetableActivity extends Activity {

	@SuppressWarnings("unused")
	private final String TAG = "TimetableActivity";

	/**
	 * Course that got download last.
	 */
	private String course;

	/**
	 * Group that got download last.
	 */
	private FK10Group group;

	/**
	 * Timetable that contains the current Timetables and is displayed.
	 */
	protected Timetable timetable;

	/**
	 * File for saving the Timetable Object.
	 */
	protected File timetableFile;

	@SuppressWarnings("rawtypes")
	protected Class otherActivity;

	protected int menuID;

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(menuID, menu);
		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);

		case R.id.menu_fk07:
			if (NetworkUtils.isConnected(this)) {
				downloadFK07Groups();
			} else {
				showToast(getString(R.string.no_internet));
			}
			break;
		case R.id.menu_fk10:
			if (NetworkUtils.isConnected(this)) {
				downloadFK10Groups();
			} else {
				showToast(getString(R.string.no_internet));
			}
			break;
		case R.id.menu_view:
			intent = new Intent(this, otherActivity);
			startActivity(intent);
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (timetable != null) {
			TimetableHandler.saveTimetable(timetable, timetableFile);
		}
	}

	/**
	 * If the User has selected a Course from "DownloadDialog" this Method will
	 * be called and a CourseDownloadThread will be executed depending on
	 * Internet Connectivity.
	 * 
	 * @param selectedCourse
	 *            The Course that should be downloaded from FK07.
	 * @param newCourse
	 *            True, if a new Timetable Object should be created. False, if
	 *            the Selected Course should be added to the current Timetable.
	 */
	public void onCourseSelected(final String selectedCourse,
			final boolean newCourse) {
		course = selectedCourse;
		final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
			final CourseDownloadThread downloadThread = new CourseDownloadThread();
			downloadThread.execute(newCourse);
		} else {
			Toast.makeText(getApplicationContext(),
					"Zurzeit kein Download m�glich", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * If the User has selected a Course from "DownloadFK10Dialog" this Method
	 * will be called and a FK10DownloadThread will be executed depending on
	 * Internet Connectivity.
	 * 
	 * @param selectedGroup
	 *            The FK10 Course that should be downloaded from FK07.
	 * @param newCourse
	 *            True, if a new Timetable Object should be created. False, if
	 *            the Selected Course should be added to the current Timetable.
	 */
	public void onGroupSelected(final FK10Group selectedGroup,
			final boolean newCourse) {
		group = selectedGroup;
		final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
			final FK10DownloadThread fk10downloadThread = new FK10DownloadThread();
			fk10downloadThread.execute(newCourse);
		} else {
			Toast.makeText(getApplicationContext(),
					"Zurzeit kein Download m�glich", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * Call generateViews on UIThread. The current View will be completly
	 * refreshed.
	 */
	public void refreshViews() {
		runOnUiThread(new Runnable() {
			public void run() {
				generateViews();
			}
		});
	}

	/**
	 * Generate the Views for displaying the current Timetable.
	 */
	protected abstract void generateViews();

	/**
	 * Enable or Disable the ProgressBar in the ActionBar. This will be executed
	 * by the UIThread.
	 * 
	 * @param show
	 *            True, if the ProgressBar should be Enabled. Else False.
	 */
	public void showProgressBar(final boolean show) {
		runOnUiThread(new Runnable() {
			public void run() {
				setProgressBarIndeterminateVisibility(show);
			}
		});
	}

	public void showToast(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(TimetableActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Download all FK10Groups. This is necessary for the Course List of the
	 * "DownloadFK10Dialog"
	 */
	public void downloadFK10Groups() {
		final FK10GroupDownloadThread fk10GroupDownloadThread = new FK10GroupDownloadThread();
		fk10GroupDownloadThread.execute();
	}

	/**
	 * Download all downloadFK07Groups. This is necessary for the Course List of
	 * the "DownloadFK07Dialog"
	 */
	public void downloadFK07Groups() {
		final FK07GroupDownloadThread downloadThread = new FK07GroupDownloadThread();
		downloadThread.execute();
	}

	/**
	 * Download a FK07 Timetable. The Download depends on the Selected Course.
	 * 
	 * @author Rene
	 */
	private class CourseDownloadThread extends AsyncTask<Boolean, Void, Void> {

		/**
		 * Tag.
		 */
		private final String TAG = "CourseDownloadThread";

		@Override
		protected void onPostExecute(final Void result) {
			Log.d(TAG, "CourseDownloadThread has finished.");
			showProgressBar(false);
			refreshViews();
		}

		@Override
		protected Void doInBackground(final Boolean... params) {
			showProgressBar(true);
			try {
				if (params[0]) {
					timetable = TimetableHandler.downloadTimetable(course);
				} else {
					final Timetable nextTable = TimetableHandler
							.downloadTimetable(course);
					timetable.addAll(nextTable);
				}
			} catch (final DownloadException de) {
				showToast(getResources().getString(R.string.downloadError));
				Log.e(TAG, de.getMessage());
			}

			TimetableHandler.saveTimetable(timetable, timetableFile);
			return null;
		}
	}

	/**
	 * Download a FK10 Timetable.The Download depends on the Selected Group.
	 * 
	 * @author Rene
	 */
	public class FK10DownloadThread extends AsyncTask<Boolean, Void, Void> {

		/**
		 * Tag.
		 */
		private final String TAG = "FK10DownloadThread";

		@Override
		protected void onPostExecute(final Void result) {
			Log.d(TAG, "FK10DownloadThread has finished.");
			showProgressBar(false);
			refreshViews();
		}

		@Override
		protected Void doInBackground(final Boolean... params) {
			try {
				if (params[0]) {
					timetable = TimetableHandler.downloadTimetableFK10(
							TimetableActivity.this, group);
				} else {
					final Timetable nextTable = TimetableHandler
							.downloadTimetableFK10(TimetableActivity.this,
									group);
					timetable.addAll(nextTable);
				}
			} catch (final IOException e) {
				showToast(e.getMessage());
				Log.e(TAG, e.getMessage());
			}
			return null;
		}
	}

	/**
	 * Download all FK10 Courses.
	 * 
	 * @author Rene
	 */
	public class FK10GroupDownloadThread extends AsyncTask<Void, Void, Void> {

		/**
		 * Tag.
		 */
		private final String TAG = "FK10DownloadThread";

		/**
		 * Downloaded Courses.
		 */
		private List<FK10Group> groups;

		@Override
		protected void onPostExecute(final Void result) {
			Log.d(TAG, "FK10GroupDownloadThread has finished.");
			showProgressBar(false);
			final DownloadFK10Dialog downloadFK10Dialog = new DownloadFK10Dialog(
					TimetableActivity.this, timetable, groups);
			downloadFK10Dialog.show();
		}

		@Override
		protected Void doInBackground(final Void... params) {
			showProgressBar(true);
			try {
				groups = FK10Handler.getBWLGroups();
			} catch (final IOException e) {
				showToast(getResources().getString(R.string.downloadError)
						+ " getBWLGroups()");
				Log.e(TAG, e.getMessage());
			}
			return null;
		}

	}

	/**
	 * Download all FK10 Courses.
	 * 
	 * @author Rene
	 */
	public class FK07GroupDownloadThread extends AsyncTask<Void, Void, Void> {

		/**
		 * Tag.
		 */
		private final String TAG = "FK07DownloadThread";

		/**
		 * Downloaded Courses.
		 */
		private List<FK07Group> groups;

		@Override
		protected void onPostExecute(final Void result) {
			Log.d(TAG, "FK07GroupDownloadThread has finished.");
			showProgressBar(false);
			final DownloadDialog downloadDialog = new DownloadDialog(
					TimetableActivity.this, timetable, groups);
			downloadDialog.show();
		}

		@Override
		protected Void doInBackground(final Void... params) {
			showProgressBar(true);
			try {
				groups = FK7Handler.getGroupList();
			} catch (final DownloadException e) {
				showToast(getResources().getString(R.string.downloadError)
						+ " getGroupList()");
				Log.e(TAG, e.getMessage());
			}
			return null;
		}
	}
}
