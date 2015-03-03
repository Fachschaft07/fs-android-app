package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.google.common.base.Optional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractFetcher;
import edu.hm.cs.fs.app.datastore.web.fetcher.IFilter;
import edu.hm.cs.fs.app.datastore.model.constants.Letter;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.StudyGroup;
import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;

/**
 * The news which are shown at the black board. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/news"
 * >http://fi.cs.hm.edu/fi/rest/public/news</a>)
 *
 * @author Fabio
 *
 */
public class NewsFetcher {
	private final String mAuthor;
	private final String mSubject;
	private final String mText;
	private final List<String> mTeacherList;
	private final List<StudyGroup> mGroupList;
	private final String mScope;
	private final Date mPublish;
	private final Date mExpire;
	private final String mUrl;

	private NewsFetcher(final Builder builder) {
		mAuthor = builder.mAuthor;
		mSubject = builder.mSubject;
		mText = builder.mText;
		mTeacherList = new ArrayList<String>(builder.mTeacherList);
		mGroupList = new ArrayList<StudyGroup>(builder.mGroupList);
		mScope = builder.mScope;
		mPublish = builder.mPublish;
		mExpire = builder.mExpire;
		mUrl = builder.mUrl;
	}

	/**
	 * @return the author.
	 */
	public String getAuthor() {
		return mAuthor;
	}

	/**
	 * @return the subject.
	 */
	public String getSubject() {
		return mSubject;
	}

	/**
	 * @return the text.
	 */
	public String getText() {
		return mText;
	}

	/**
	 * @return the teacher.
	 */
	public List<String> getTeachers() {
		return new ArrayList<String>(mTeacherList);
	}

	/**
	 * @return the group.
	 */
	public List<StudyGroup> getGroups() {
		return new ArrayList<StudyGroup>(mGroupList);
	}

	/**
	 * @return the scope.
	 */
	public Optional<String> getScope() {
		return Optional.fromNullable(mScope);
	}

	/**
	 * @return the publish.
	 */
	public Optional<Date> getPublish() {
		return Optional.fromNullable(mPublish);
	}

	/**
	 * @return the expire.
	 */
	public Optional<Date> getExpire() {
		return Optional.fromNullable(mExpire);
	}

	/**
	 * @return the url.
	 */
	public Optional<String> getUrl() {
		return Optional.fromNullable(mUrl);
	}

	/**
	 * @author Fabio
	 *
	 */
	public static class Builder extends AbstractFetcher<Builder, NewsFetcher> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/news.xml";
		private static final String ROOT_NODE = "/newslist/news";
		@SuppressLint("SimpleDateFormat")
		private static final DateFormat DATE_PARSER = new SimpleDateFormat(
				"yyyy-MM-dd");

		private String mAuthor;
		private String mSubject;
		private String mText;
		private List<String> mTeacherList;
		private List<StudyGroup> mGroupList;
		private String mScope;
		private Date mPublish;
		private Date mExpire;
		private String mUrl;

		/**
		 * Create a new builder.
		 *
		 * @param context
		 */
		public Builder(final Context context) {
			super(context, URL, ROOT_NODE);
		}

		/**
		 * Filter only a {@link Study}.
		 *
		 * @param group
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addGroupFilter(final Study group) {
			addFilter(new IFilter<NewsFetcher>() {
				@Override
				public boolean apply(final NewsFetcher data) {
					return data.getGroups().isEmpty() || !DataUtils
							.filter(data.getGroups(), group).isEmpty();
				}
			});
			return this;
		}

		/**
		 * Filter for the {@link Study} and {@link Semester}.
		 *
		 * @param group
		 *            to filter for.
		 * @param semester
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addGroupFilter(final Study group, final Semester semester) {
			addFilter(new IFilter<NewsFetcher>() {
				@Override
				public boolean apply(final NewsFetcher data) {
					return data.getGroups().isEmpty() || !DataUtils
							.filter(data.getGroups(), group, semester)
							.isEmpty();
				}
			});
			return this;
		}

		/**
		 * Filter for the {@link Study}, {@link Semester} and {@link Letter}.
		 *
		 * @param group
		 *            to filter for.
		 * @param semester
		 *            to filter for.
		 * @param letter
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addGroupFilter(final Study group,
				final Semester semester, final Letter letter) {
			addFilter(new IFilter<NewsFetcher>() {
				@Override
				public boolean apply(final NewsFetcher data) {
					return data.getGroups().isEmpty() || !DataUtils
							.filter(data.getGroups(), group, semester, letter)
							.isEmpty();
				}
			});
			return this;
		}

		/**
		 * Filter for a study group. It can be only a {@link Study} but also
		 * with {@link Semester} and {@link Letter}.
		 *
		 * @param groupName
		 *            the groupName to filter.
		 * @return the builder.
		 */
		public Builder addGroupFilter(final String groupName) {
			if (!TextUtils.isEmpty(groupName)) {
				final StudyGroup group = StudyGroup.of(groupName);
				final Optional<Semester> semester = group.getSemester();
				final Optional<Letter> letter = group.getLetter();

				if (letter.isPresent()) {
					addGroupFilter(group.getStudy(), semester.get(),
							letter.get());
				} else if (semester.isPresent()) {
					addGroupFilter(group.getStudy(), semester.get());
				} else {
					addGroupFilter(group.getStudy());
				}
			}
			return this;
		}

		/**
		 * Filter for a teacher name. It does not check if every letter is
		 * equal. It will only check if the specified content is in some teacher
		 * name contained.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addTeacherFilter(final String keyWord) {
			addFilter(new IFilter<NewsFetcher>() {
				@SuppressLint("DefaultLocale")
				@Override
				public boolean apply(final NewsFetcher data) {
					boolean apply = false;
					for (final String string : data.getTeachers()) {
						if (DataUtils.containsIgnoreCase(string, keyWord)) {
							apply = true;
						}
					}
					return apply;
				}
			});
			return this;
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
			addFilter(new IFilter<NewsFetcher>() {
				@Override
				public boolean apply(final NewsFetcher data) {
					return DataUtils
							.containsIgnoreCase(data.getText(), keyWord);
				}
			});
			return this;
		}

		@Override
		protected NewsFetcher onCreateItem(final String rootPath) throws Exception {
			// reset Variables...
			mTeacherList = new ArrayList<String>();
			mGroupList = new ArrayList<StudyGroup>();
			mPublish = null;
			mExpire = null;

			// Parse Elements...
			mAuthor = findByXPath(rootPath + "/author/text()",
					XPathConstants.STRING);
			mSubject = findByXPath(rootPath + "/subject/text()",
					XPathConstants.STRING);
			mText = findByXPath(rootPath + "/text/text()",
					XPathConstants.STRING);
			mScope = findByXPath(rootPath + "/scope/text()",
					XPathConstants.STRING);

			final String publishDate = findByXPath(
					rootPath + "/publish/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(publishDate)) {
				mPublish = DATE_PARSER.parse(publishDate);
			}

			final String expireDate = findByXPath(rootPath + "/expire/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(expireDate)) {
				mExpire = DATE_PARSER.parse(expireDate);
			}

			mUrl = findByXPath(rootPath + "/url/text()", XPathConstants.STRING);

			final int teacherCount = getCountByXPath(rootPath + "/teacher");
			for (int teacherIndex = 1; teacherIndex <= teacherCount; teacherIndex++) {
				final String teacherName = findByXPath(rootPath + "/teacher["
						+ teacherIndex + "]/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(teacherName)) {
					mTeacherList.add(teacherName);
				}
			}

			final int groupCount = getCountByXPath(rootPath + "/group");
			for (int groupIndex = 1; groupIndex <= groupCount; groupIndex++) {
				final String groupName = findByXPath(rootPath + "/group["
						+ groupIndex + "]/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(groupName)) {
					mGroupList.add(StudyGroup.of(groupName));
				}
			}

			return new NewsFetcher(this);
		}
	}
}
