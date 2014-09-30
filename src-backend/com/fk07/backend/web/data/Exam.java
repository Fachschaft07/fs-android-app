package com.fk07.backend.web.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import com.fk07.backend.web.data.builder.AbstractBuilder;
import com.fk07.backend.web.data.builder.IFilter;
import com.fk07.backend.web.data.constants.ExamGroup;
import com.fk07.backend.web.data.constants.ExamType;
import com.fk07.backend.web.data.constants.Study;
import com.fk07.backend.web.data.constants.StudyGroup;
import com.fk07.backend.web.data.utils.DataUtils;
import com.google.common.base.Optional;

/**
 * The exams with every information. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/exam"
 * >http://fi.cs.hm.edu/fi/rest/public/exam</a>)
 *
 * @author Fabio
 *
 */
public class Exam {
	private final String mCode;
	private final Study mGroup;
	private final String mModul;
	private final String mSubtitle;
	private final List<Study> mReferences;
	private final List<String> mExaminers;
	private final ExamType mType;
	private final String mMaterial;
	private final ExamGroup mAllocation;
	private final String mSemester;
	private final Date mLastChange;

	private Exam(final Builder builder) {
		mCode = builder.mCode;
		mGroup = builder.mGroup;
		mModul = builder.mModul;
		mSubtitle = builder.mSubtitle;
		mReferences = new ArrayList<Study>(builder.mReferences);
		mExaminers = new ArrayList<String>(builder.mExaminers);
		mType = builder.mType;
		mMaterial = builder.mMaterial;
		mAllocation = builder.mAllocation;
		mSemester = builder.mSemester;
		mLastChange = builder.mLastChange;
	}

	/**
	 * @return the code.
	 */
	public String getCode() {
		return mCode;
	}

	/**
	 * @return the group.
	 */
	public Optional<Study> getStudyGroup() {
		return Optional.fromNullable(mGroup);
	}

	/**
	 * @return the modul.
	 */
	public String getModul() {
		return mModul;
	}

	/**
	 * @return the subtitle.
	 */
	public String getSubtitle() {
		return mSubtitle;
	}

	/**
	 * @return the references.
	 */
	public List<Study> getReferences() {
		return new ArrayList<Study>(mReferences);
	}

	/**
	 * @return the examiners.
	 */
	public List<String> getExaminers() {
		return new ArrayList<String>(mExaminers);
	}

	/**
	 * @return the type.
	 */
	public ExamType getType() {
		return mType;
	}

	/**
	 * @return the material.
	 */
	public String getMaterial() {
		return mMaterial;
	}

	/**
	 * @return the allocation.
	 */
	public ExamGroup getAllocation() {
		return mAllocation;
	}

	/**
	 * @return the semester.
	 */
	public String getSemester() {
		return mSemester;
	}

	/**
	 * @return the lastChange.
	 */
	public Date getLastChange() {
		return mLastChange;
	}

	/**
	 * @author Fabio
	 *
	 */
	public static class Builder extends AbstractBuilder<Builder, Exam> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/exam.xml";
		private static final String ROOT_NODE = "/examlist/exam";
		@SuppressLint("SimpleDateFormat")
		private static final DateFormat DATE_PARSER = new SimpleDateFormat(
				"dd.MM.yyyy");

		private String mCode;
		private Study mGroup;
		private String mModul;
		private String mSubtitle;
		private List<Study> mReferences = new ArrayList<Study>();
		private List<String> mExaminers = new ArrayList<String>();
		private ExamType mType;
		private String mMaterial;
		private ExamGroup mAllocation;
		private String mSemester;
		private Date mLastChange;

		/**
		 * Creates a new builder.
		 *
		 * @param context
		 */
		public Builder(final Context context) {
			super(context, URL, ROOT_NODE);
		}

		/**
		 * Filters the exams with the specified code.
		 *
		 * @param code
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addCodeFilter(final String code) {
			addFilter(new IFilter<Exam>() {
				@Override
				public boolean apply(final Exam data) {
					return data.getCode().equalsIgnoreCase(code);
				}
			});
			return this;
		}

		/**
		 * Filter only a {@link Study}.
		 *
		 * @param study
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addStudyFilter(final Study study) {
			addFilter(new IFilter<Exam>() {
				@Override
				public boolean apply(final Exam data) {
					return data.getStudyGroup().isPresent()
							&& data.getStudyGroup().get() == study;
				}
			});
			return this;
		}

		/**
		 * Filters for a keyword inside of the modul name.
		 *
		 * @param modul
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addModulFilter(final String modul) {
			addFilter(new IFilter<Exam>() {
				@Override
				public boolean apply(final Exam data) {
					return DataUtils.containsIgnoreCase(data.getModul(), modul);
				}
			});
			return this;
		}

		/**
		 * Filters for the type of the exam.
		 *
		 * @param type
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addTypeFilter(final ExamType type) {
			addFilter(new IFilter<Exam>() {
				@Override
				public boolean apply(final Exam data) {
					return data.getType() == type;
				}
			});
			return this;
		}

		/**
		 * Filters for the group of the exam
		 *
		 * @param group
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addGroupFilter(final ExamGroup group) {
			addFilter(new IFilter<Exam>() {
				@Override
				public boolean apply(final Exam data) {
					return data.getAllocation() == group;
				}
			});
			return this;
		}

		@Override
		protected Exam onCreateItem(final String rootPath) throws Exception {
			// reset Variables...
			mGroup = null;
			mReferences = new ArrayList<Study>();
			mExaminers = new ArrayList<String>();
			mType = null;
			mAllocation = null;

			// Parse Elements...
			mCode = findByXPath(rootPath + "/code/text()",
					XPathConstants.STRING);
			final String group = findByXPath(rootPath + "/program/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(group)) {
				mGroup = StudyGroup.of(group).getStudy();
			}
			mModul = findByXPath(rootPath + "/modul/text()",
					XPathConstants.STRING);
			mSubtitle = findByXPath(rootPath + "/subtitle/text()",
					XPathConstants.STRING);

			final int countRef = getCountByXPath(rootPath + "/reference");
			for (int indexRef = 1; indexRef <= countRef; indexRef++) {
				final String ref = findByXPath(rootPath + "/reference["
						+ indexRef + "]/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(ref)) {
					mReferences.add(StudyGroup.of(ref).getStudy());
				}
			}

			final int countExaminer = getCountByXPath(rootPath + "/examiner");
			for (int indexExaminer = 1; indexExaminer <= countExaminer; indexExaminer++) {
				final String examiner = findByXPath(rootPath + "/examiner["
						+ indexExaminer + "]/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(examiner)) {
					mExaminers.add(examiner);
				}
			}

			final String type = findByXPath(rootPath + "/type/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(type)) {
				mType = ExamType.of(type);
			}

			mMaterial = findByXPath(rootPath + "/material/text()",
					XPathConstants.STRING);
			final String allocation = findByXPath(rootPath
					+ "/allocation/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(allocation)) {
				mAllocation = ExamGroup.of(allocation);
			}

			return new Exam(this);
		}
	}
}
