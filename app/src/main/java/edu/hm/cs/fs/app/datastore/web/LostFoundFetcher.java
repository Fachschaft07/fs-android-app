package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractFetcher;
import edu.hm.cs.fs.app.datastore.web.fetcher.IFilter;
import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;

/**
 * The things which gone lost and found. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/lostfound"
 * >http://fi.cs.hm.edu/fi/rest/public/lostfound</a>)
 *
 * @author Fabio
 *
 */
public class LostFoundFetcher {
	private final String mSubject;
	private final Date mDate;

	private LostFoundFetcher(final Builder builder) {
		mSubject = builder.mSubject;
		mDate = builder.mDate;
	}

	/**
	 * @return the subject.
	 */
	public String getSubject() {
		return mSubject;
	}

	/**
	 * @return the date.
	 */
	public Date getDate() {
		return mDate;
	}

	/**
	 * @author Fabio
	 *
	 */
	public static class Builder extends AbstractFetcher<Builder, LostFoundFetcher> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/lostfound.xml";
		private static final String ROOT_NODE = "/lostfoundlist/lostfound";
		@SuppressLint("SimpleDateFormat")
		private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
				"yyyy-MM-dd");

		private String mSubject;
		private Date mDate;

		/**
		 * Creates a new builder.
		 *
		 * @param context
		 */
		public Builder(final Context context) {
			super(context, URL, ROOT_NODE);
		}

		/**
		 * Check whether the subject contains the key word.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addSubjectFilter(final String keyWord) {
			addFilter(new IFilter<LostFoundFetcher>() {
				@Override
				public boolean apply(final LostFoundFetcher data) {
					return DataUtils.containsIgnoreCase(data.getSubject(),
							keyWord);
				}
			});
			return this;
		}

		/**
		 * Check whether the date is the same then the one of the data item.
		 *
		 * @param date
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addDateFilter(final Date date) {
			addFilter(new IFilter<LostFoundFetcher>() {
				@Override
				public boolean apply(final LostFoundFetcher data) {
					return DataUtils.isSameDate(date, data.getDate());
				}
			});
			return this;
		}

		@Override
		protected LostFoundFetcher onCreateItem(final String rootPath)
				throws Exception {
			// Parse Elements...
			mSubject = findByXPath(rootPath + "/subject/text()",
					XPathConstants.STRING);
			mDate = DATE_FORMATTER.parse((String) findByXPath(rootPath
					+ "/date/text()", XPathConstants.STRING));

			return new LostFoundFetcher(this);
		}
	}
}
