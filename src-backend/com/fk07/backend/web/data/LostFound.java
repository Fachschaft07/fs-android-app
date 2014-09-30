package com.fk07.backend.web.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import android.annotation.SuppressLint;
import android.content.Context;

import com.fk07.backend.web.data.builder.AbstractBuilder;
import com.fk07.backend.web.data.builder.IFilter;
import com.fk07.backend.web.data.utils.DataUtils;

/**
 * The things which gone lost and found. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/lostfound"
 * >http://fi.cs.hm.edu/fi/rest/public/lostfound</a>)
 *
 * @author Fabio
 *
 */
public class LostFound {
	private final String mSubject;
	private final Date mDate;

	private LostFound(final Builder builder) {
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
	public static class Builder extends AbstractBuilder<Builder, LostFound> {
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
			addFilter(new IFilter<LostFound>() {
				@Override
				public boolean apply(final LostFound data) {
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
			addFilter(new IFilter<LostFound>() {
				@Override
				public boolean apply(final LostFound data) {
					return DataUtils.isSameDate(date, data.getDate());
				}
			});
			return this;
		}

		@Override
		protected LostFound onCreateItem(final String rootPath)
				throws Exception {
			// Parse Elements...
			mSubject = findByXPath(rootPath + "/subject/text()",
					XPathConstants.STRING);
			mDate = DATE_FORMATTER.parse((String) findByXPath(rootPath
					+ "/date/text()", XPathConstants.STRING));

			return new LostFound(this);
		}
	}
}
