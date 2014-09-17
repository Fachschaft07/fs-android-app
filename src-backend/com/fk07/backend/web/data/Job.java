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
import com.fk07.backend.web.data.constants.Study;
import com.fk07.backend.web.data.constants.StudyGroup;
import com.fk07.backend.web.data.utils.DataUtils;
import com.google.common.base.Optional;

/**
 * The offered jobs. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/job">http:
 * //fi.cs.hm.edu/fi/rest/public/job</a>)
 *
 * @author Fabio
 *
 */
public class Job {
	private final String mTitle;
	private final String mProvider;
	private final String mDescription;
	private final Study mProgram;
	private final String mContact;
	private final Date mExpire;
	private final String mUrl;

	private Job(final Builder builder) {
		mTitle = builder.mTitle;
		mProvider = builder.mProvider;
		mDescription = builder.mDescription;
		mProgram = builder.mProgram;
		mContact = builder.mContact;
		mExpire = builder.mExpire;
		mUrl = builder.mUrl;
	}

	/**
	 * @return the title.
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @return the provider.
	 */
	public String getProvider() {
		return mProvider;
	}

	/**
	 * @return the description.
	 */
	public String getDescription() {
		return mDescription;
	}

	/**
	 * @return the program.
	 */
	public Optional<Study> getProgram() {
		return Optional.fromNullable(mProgram);
	}

	/**
	 * @return the contact.
	 */
	public String getContact() {
		return mContact;
	}

	/**
	 * @return the expire date.
	 */
	public Date getExpire() {
		return mExpire;
	}

	/**
	 * @return the url.
	 */
	public String getUrl() {
		return mUrl;
	}

	/**
	 * @author Fabio
	 *
	 */
	public static final class Builder extends AbstractBuilder<Builder, Job> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/job.xml";
		private static final String ROOT_NODE = "job";
		@SuppressLint("SimpleDateFormat")
		private static final DateFormat DATE_PARSER = new SimpleDateFormat(
				"yyyy-MM-dd");

		private String mTitle;
		private String mProvider;
		private String mDescription;
		private Study mProgram;
		private String mContact;
		private Date mExpire;
		private String mUrl;

		/**
		 * Creates a new builder.
		 *
		 * @param context
		 */
		public Builder(final Context context) {
			super(context, URL, ROOT_NODE);
		}

		/**
		 * Searches for the key word in the text. If the text contains the key
		 * word, it will be shown otherwise not.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addContentFilter(final String keyWord) {
			addFilter(new IFilter<Job>() {
				@Override
				public boolean apply(final Job data) {
					return DataUtils.containsIgnoreCase(data.getTitle(),
							keyWord)
							|| DataUtils.containsIgnoreCase(data.getContact(),
									keyWord)
									|| DataUtils.containsIgnoreCase(
											data.getDescription(), keyWord);
				}
			});
			return this;
		}

		/**
		 * Filter only a {@link Study}.
		 *
		 * @param studyGroup
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addProgramFilter(final Study studyGroup) {
			addFilter(new IFilter<Job>() {
				@Override
				public boolean apply(final Job data) {
					// To get the generally information{
					return !data.getProgram().isPresent()
							|| data.getProgram().isPresent()
							&& data.getProgram().get() == studyGroup;
				}
			});
			return this;
		}

		@Override
		protected Job onCreateItem(final int index) throws Exception {
			// reset Variables...
			mProgram = null;

			// Parse Elements...
			final String xPathRoot = "/joblist/job[" + index + "]";
			mTitle = findByXPath(xPathRoot + "/title/text()",
					XPathConstants.STRING);
			mProvider = findByXPath(xPathRoot + "/provider/text()",
					XPathConstants.STRING);
			mDescription = findByXPath(xPathRoot + "/description/text()",
					XPathConstants.STRING);
			mContact = findByXPath(xPathRoot + "/contact/text()",
					XPathConstants.STRING);
			final String program = findByXPath(xPathRoot + "/contact/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(program)) {
				mProgram = StudyGroup.of(program).getStudyGroup();
			}
			mExpire = DATE_PARSER.parse((String) findByXPath(xPathRoot
					+ "/expire/text()", XPathConstants.STRING));
			mUrl = findByXPath(xPathRoot + "/url/text()", XPathConstants.STRING);

			return new Job(this);
		}
	}
}
