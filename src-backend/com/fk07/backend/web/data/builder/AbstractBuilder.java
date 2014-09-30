package com.fk07.backend.web.data.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.fk07.backend.web.data.utils.DataUtils;
import com.google.gson.Gson;

/**
 * @author Fabio
 *
 * @param <B>
 *            The Builder
 * @param <T>
 *            The Content type
 */
public abstract class AbstractBuilder<B extends AbstractBuilder<B, T>, T> {
	private static final String TAG = AbstractBuilder.class.getSimpleName();
	private final XPathFactory mXPathfactory = XPathFactory.newInstance();
	private final XPath mXPath = mXPathfactory.newXPath();
	private final Context mContext;
	private final String mUrl;
	private File mOfflineFile;
	private Document xmlDoc;
	private final Gson mGson = new Gson();
	private final Handler mHandler = new Handler();
	private final List<IFilter<T>> mFilterList = new ArrayList<IFilter<T>>();
	private final String mRootNode;
	private boolean mOfflineMode;
	private long mUpdateIntervalMillis;

	protected AbstractBuilder(final Context context, final String url,
			final String rootNode) {
		mContext = context;
		this.mUrl = url;
		this.mRootNode = rootNode;
	}

	/**
	 * The file to store the data in when loading the content from the internet.
	 * If the internet is not available, then the data will be loaded from this
	 * file.
	 *
	 * @param file
	 *            save and load content from.
	 * @return the builder.
	 */
	@SuppressWarnings("unchecked")
	public B setOfflineStorage(final File file) {
		mOfflineFile = file;
		return (B) this;
	}

	/**
	 * Set the offline mode to <code>true</code> to work with the existing
	 * offline storage file.
	 *
	 * @param offlineMode
	 *            should set to <code>true</code> to work offline and don't
	 *            fetch with every data request the content from the internet.
	 * @return the builder.
	 */
	@SuppressWarnings("unchecked")
	public B setWorkOffline(final boolean offlineMode) {
		this.mOfflineMode = offlineMode;
		return (B) this;
	}

	/**
	 * Set a update interval for the offline storage file to automaticaly update
	 * the file with a request. No need to load the data every time from the web
	 * if a connection is available.
	 *
	 * @param timeUnit
	 *            for convertion.
	 * @param time
	 *            of next update.
	 * @return the builder.
	 */
	@SuppressWarnings("unchecked")
	public B setOfflineUpdateInterval(final TimeUnit timeUnit, final long time) {
		mUpdateIntervalMillis = TimeUnit.MILLISECONDS.convert(time, timeUnit);
		return (B) this;
	}

	/**
	 * Downloads and reads the content from the web if a connection is
	 * available. Otherwise the offline file will be read.
	 *
	 * @return a list with the content.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public final List<T> build() throws Exception {
		final List<T> content = new ArrayList<T>();

		if (!isOnline() && mOfflineFile == null) {
			// No internet connection and no offline file --> exit
			throw new IllegalArgumentException(
					"You can not work offline and have no offline storage file");
		}

		// Only load the data from xml if necassary!
		if (isLocale()) {
			// Otherwise load the data from json offline file
			final List<T> result = mGson.fromJson(DataUtils.read(mOfflineFile)
					.toString(), List.class);

			// do the filtering stuff...
			for (final T item : result) {
				if (apply(item)) {
					content.add(item);
				}
			}
		} else {
			xmlDoc = DataUtils.read(mUrl);

			if (xmlDoc == null) {
				// Unable to parse xml --> exit
				throw new ParseException("Unable to parse url");
			}

			// Convert everything into json format
			final JSONArray jsonArray = new JSONArray();

			// 2014-09-18: BugFix: Wrong count with
			// xmlDoc.getElementByTagName(...)
			// 2014-09-19: Put the getCountByXPah method outside the for-loop to
			// increase speed
			final int countElements = getCountByXPath(mRootNode);
			for (int index = 1; index <= countElements; index++) {
				final String path = mRootNode + "[" + index + "]";
				final T item = onCreateItem(path);

				// add every item to the json --> we want to save all items not
				// only the filtered
				jsonArray.put(mGson.toJson(item));

				// do the filtering stuff...
				if (apply(item)) {
					content.add(item);
				}
			}

			if (mOfflineFile != null && !isLocale()) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						// Only save the xml if the device is connected to the
						// internet
						try {
							// DataUtils.save(xmlDoc, mOfflineFile);

							// 2014-09-23: Save as json to wrap json after
							// reading to objects
							DataUtils.save(jsonArray, mOfflineFile);
						} catch (final Exception e) {
							Log.e(TAG, "", e);
						}
					}
				});
			}
		}

		return content;
	}

	/**
	 * Create the next item at the specified index.
	 *
	 * @param rootPath
	 *            of the item.
	 * @return the item.
	 * @throws Exception
	 */
	protected abstract T onCreateItem(String rootPath) throws Exception;

	/**
	 * Find a element by using the {@link XPath}.
	 *
	 * @param xPath
	 *            for searching.
	 * @param name
	 *            of the type.
	 * @return the found value.
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	protected <X> X findByXPath(final String xPath, final QName name)
			throws XPathExpressionException {
		return (X) mXPath.evaluate(xPath, xmlDoc, name);
	}

	/**
	 * Get the count of elements of a name.
	 *
	 * @param xPath
	 *            to the element.
	 * @return the count.
	 * @throws XPathExpressionException
	 */
	protected int getCountByXPath(final String xPath)
			throws XPathExpressionException {
		return ((NodeList) findByXPath(xPath, XPathConstants.NODESET))
				.getLength();
	}

	/**
	 * Add a filter which every created item has to pass before it will be added
	 * to the build list.
	 *
	 * @param filter
	 *            to pass.
	 */
	protected void addFilter(final IFilter<T> filter) {
		mFilterList.add(filter);
	}

	private boolean apply(final T item) {
		boolean apply = true;
		for (final IFilter<T> filter : mFilterList) {
			apply &= filter.apply(item);
		}
		return apply;
	}

	private boolean isLocale() {
		return mOfflineFile != null
				&& mOfflineFile.exists()
				&& mOfflineFile.lastModified() + mUpdateIntervalMillis < System
				.currentTimeMillis() || !DataUtils.isOnline(mContext);
	}

	private boolean isOnline() {
		return DataUtils.isOnline(mContext) && !mOfflineMode;
	}
}
