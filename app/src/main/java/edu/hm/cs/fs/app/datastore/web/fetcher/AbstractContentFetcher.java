package edu.hm.cs.fs.app.datastore.web.fetcher;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.owlike.genson.Genson;
import com.owlike.genson.reflect.VisibilityFilter;

import org.json.JSONArray;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.ExamGroup;
import edu.hm.cs.fs.app.datastore.model.constants.ExamType;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.Offer;
import edu.hm.cs.fs.app.datastore.model.constants.PersonStatus;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Sex;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.StudyGroup;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;

/**
 * Created by Fabio on 18.02.2015.
 */
public abstract class AbstractContentFetcher<Builder extends AbstractContentFetcher<Builder, T>, T> {
	private static final String TAG = AbstractContentFetcher.class.getSimpleName();
	private final Context mContext;
	private final String mUrl;
	private final File mOfflineFile;
	private final Handler mHandler = new Handler();
	private final List<IFilter<T>> mFilterList = new ArrayList<IFilter<T>>();
	protected final Gson mGson = new Gson();
	private final Genson mGenson;
	private long mUpdateIntervalMillis;

	protected AbstractContentFetcher(final Context context, File offlineFile, final String url) {
		mContext = context;
		mOfflineFile = offlineFile;
		this.mUrl = url;
		setOfflineUpdateInterval(TimeUnit.MINUTES, 15);

		mGenson = new GensonBuilder()
				.useDateAsTimestamp(true)
				.withConverter(new StudyGroupMapper(), StudyGroup.class)
				.withConverter(new EnumMapper<Day>(), Day.class)
				.withConverter(new EnumMapper<ExamGroup>(), ExamGroup.class)
				.withConverter(new EnumMapper<ExamType>(), ExamType.class)
				.withConverter(new EnumMapper<Faculty>(), Faculty.class)
				.withConverter(new EnumMapper<Offer>(), Offer.class)
				.withConverter(new EnumMapper<PersonStatus>(),
						PersonStatus.class)
				.withConverter(new EnumMapper<Semester>(), Semester.class)
				.withConverter(new EnumMapper<Sex>(), Sex.class)
				.withConverter(new EnumMapper<Study>(), Study.class)
				.withConverter(new EnumMapper<TeachingForm>(),
						TeachingForm.class)
				.withConverter(new EnumMapper<Time>(), Time.class)
				.useFields(true, VisibilityFilter.PRIVATE).useRuntimeType(true)
				.exclude(AbstractContentFetcher.class).useMethods(false)
				.useIndentation(true).create();
	}

	/**
	 * Set a update interval for the offline storage file to automaticaly update the file with a
	 * request. No need to load the data every time from the web if a connection is available.
	 * (Default: Every 15 Minutes)
	 *
	 * @param timeUnit
	 * 		for convertion.
	 * @param time
	 * 		of next update.
	 * @return the builder.
	 */
	@SuppressWarnings("unchecked")
	public Builder setOfflineUpdateInterval(final TimeUnit timeUnit, final long time) {
		mUpdateIntervalMillis = TimeUnit.MILLISECONDS.convert(time, timeUnit);
		return (Builder) this;
	}

	/**
	 * Downloads and reads the content from the web if a connection is available. Otherwise the
	 * offline file will be read.
	 *
	 * @return a list with the content.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public final List<T> fetch() {
		List<T> result = new ArrayList<>();

		JSONArray jsonArray = new JSONArray();
		if (DataUtils.isOnline(mContext) && isNeedUpdate()) {
			// Convert everything into json format
			jsonArray = read(mUrl);

			// Only save the file if the device is connected to the
			// internet
			try {
				// DataUtils.save(xmlDoc, mOfflineFile);

				// 2014-09-23: Save as json to wrap json after
				// reading to objects
				DataUtils.save(jsonArray, mOfflineFile);
			} catch (final Exception e) {
				Log.e(TAG, "", e);
			}
		} else if(mOfflineFile.exists()) {
			try {
				jsonArray = DataUtils.read(mOfflineFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("unchecked")
		final Class<T> genericClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];

		for (int index = 0; index < jsonArray.length(); index++) {
			final T item = mGenson.deserialize(jsonArray.getString(index),
					genericClass);

			// do the filtering stuff...
			if (apply(item)) {
				result.add(item);
			}
		}

		return result;
	}

	/**
	 * @param url
	 * @return
	 */
	protected abstract JSONArray read(String url);

	/**
	 * Add a filter which every created item has to pass before it will be added to the fetch list.
	 *
	 * @param filter
	 * 		to pass.
	 */
	protected void addFilter(final IFilter<T> filter) {
		mFilterList.add(filter);
	}

	protected boolean apply(final T item) {
		boolean apply = true;
		for (final IFilter<T> filter : mFilterList) {
			apply &= filter.apply(item);
		}
		return apply;
	}

	private boolean isNeedUpdate() {
		return mOfflineFile.exists()
				&& mOfflineFile.lastModified() + mUpdateIntervalMillis < System
				.currentTimeMillis();
	}
}
