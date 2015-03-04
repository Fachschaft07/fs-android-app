package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.google.common.base.Optional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractFetcher;
import edu.hm.cs.fs.app.datastore.web.fetcher.IFilter;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;

/**
 * The offered jobs. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/job">http:
 * //fi.cs.hm.edu/fi/rest/public/job</a>)
 *
 * @author Fabio
 *
 */
public class JobFetcher {
	private final String mTitle;
	private final String mProvider;
	private final String mDescription;
	private final Study mProgram;
	private final String mContact;
	private final Date mExpire;
	private final String mUrl;

	private JobFetcher(final Builder builder) {
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
	public static final class Builder extends AbstractFetcher<Builder, JobFetcher> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/job.xml";
		private static final String ROOT_NODE = "/joblist/job";
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
			addFilter(new IFilter<JobFetcher>() {
				@Override
				public boolean apply(final JobFetcher data) {
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
			addFilter(new IFilter<JobFetcher>() {
				@Override
				public boolean apply(final JobFetcher data) {
					// To get the generally information{
					return !data.getProgram().isPresent()
							|| data.getProgram().isPresent()
							&& data.getProgram().get() == studyGroup;
				}
			});
			return this;
		}

		@Override
		protected JobFetcher onCreateItem(final String rootPath) throws Exception {
			// reset Variables...
			mProgram = null;

			// Parse Elements...
			mTitle = findByXPath(rootPath + "/title/text()",
					XPathConstants.STRING);
			mProvider = findByXPath(rootPath + "/provider/text()",
					XPathConstants.STRING);
			mDescription = findByXPath(rootPath + "/description/text()",
					XPathConstants.STRING);
			mContact = findByXPath(rootPath + "/contact/text()",
					XPathConstants.STRING);
			final String program = findByXPath(rootPath + "/program/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(program)) {
				mProgram = GroupImpl.of(program).getStudy();
			}
			mExpire = DATE_PARSER.parse((String) findByXPath(rootPath
					+ "/expire/text()", XPathConstants.STRING));
			mUrl = findByXPath(rootPath + "/url/text()", XPathConstants.STRING);

			return new JobFetcher(this);
		}
	}
}
