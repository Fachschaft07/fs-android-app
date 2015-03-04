package edu.hm.cs.fs.app.datastore.web.fetcher;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

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
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;
import edu.hm.cs.fs.app.datastore.model.constants.Time;
import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;
import edu.hm.cs.fs.app.util.NetworkUtils;

/**
 * Created by Fabio on 18.02.2015.
 */
public abstract class AbstractContentFetcher<Builder extends AbstractContentFetcher<Builder, T>, T> {
	private final Context mContext;
	private final String mUrl;

	protected AbstractContentFetcher(final Context context, final String url) {
		mContext = context;
		this.mUrl = url;
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

		if (NetworkUtils.isConnected(mContext)) {
			result.addAll(read(mUrl));
		}

		return result;
	}

	/**
	 * @param url
	 * @return
	 */
	protected abstract List<T> read(String url);
	
	protected Context getContext() {
		return mContext;
	}
}
