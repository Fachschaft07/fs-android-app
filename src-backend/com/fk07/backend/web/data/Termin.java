package com.fk07.backend.web.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.fk07.backend.web.data.builder.AbstractBuilder;
import com.fk07.backend.web.data.builder.IFilter;
import com.fk07.backend.web.data.utils.DataUtils;

/**
 * The appointments at the faculty 07. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/termin.xml"
 * >http://fi.cs.hm.edu/fi/rest/public/termin.xml</a>)
 *
 * @author Fabio
 *
 */
public class Termin {
	private final String mSubject;
	private final String mScope;
	private final Date mDate;

	private Termin(final Builder builder) {
		mSubject = builder.mSubject;
		mScope = builder.mScope;
		mDate = builder.mDate;
	}

	/**
	 * @return the subject.
	 */
	public String getSubject() {
		return mSubject;
	}

	/**
	 * @return the scope.
	 */
	public String getScope() {
		return mScope;
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
	public static class Builder extends AbstractBuilder<Builder, Termin> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/termin.xml";
		private static final String ROOT_NODE = "/terminlist/termin";
		@SuppressLint("SimpleDateFormat")
		private static final DateFormat DATE_PARSER = new SimpleDateFormat(
				"yyyy-MM-dd");

		private String mSubject;
		private String mScope;
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
		 * Filters the subjects for the specified key word.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addSubjectFilter(final String keyWord) {
			addFilter(new IFilter<Termin>() {
				@Override
				public boolean apply(final Termin data) {
					return DataUtils.containsIgnoreCase(data.getSubject(),
							keyWord);
				}
			});
			return this;
		}

		@Override
		protected Termin onCreateItem(final String rootPath) throws Exception {
			// reset Variables...
			mDate = null;

			// Parse Elements...
			mSubject = findByXPath(rootPath + "/subject/text()",
					XPathConstants.STRING);
			mScope = findByXPath(rootPath + "/scope/text()",
					XPathConstants.STRING);
			final String date = findByXPath(rootPath + "/date/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(date)) {
				mDate = DATE_PARSER.parse(date);
			}

			return new Termin(this);
		}
	}
}
