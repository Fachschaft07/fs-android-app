package com.fk07.backend.web.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.xpath.XPathConstants;

import android.content.Context;
import android.text.TextUtils;

import com.fk07.backend.web.data.builder.AbstractBuilder;
import com.fk07.backend.web.data.builder.IFilter;
import com.fk07.backend.web.data.constants.Offer;
import com.fk07.backend.web.data.constants.Semester;
import com.fk07.backend.web.data.constants.Study;
import com.fk07.backend.web.data.constants.TeachingForm;
import com.fk07.backend.web.data.utils.DataUtils;
import com.google.common.base.Optional;

/**
 * A modul can be choosen by a student. Some moduls are mandatory. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/modul">http://fi.cs.hm.edu/fi/rest/
 * public/modul</a>)
 *
 * @author Fabio
 *
 */
public class Modul {
	private final String mName;
	private final int mCredits;
	private final int mSws;
	private final String mResponsible;
	private final List<String> mTeachers;
	private final List<Locale> mLanguages;
	private final TeachingForm mTeachingForm;
	private final String mExpenditure;
	private final String mRequirements;
	private final String mGoals;
	private final String mContent;
	private final String mMedia;
	private final String mLiterature;
	private final Study mProgram;
	private final List<ModulCode> mModulCodes;

	private Modul(final Builder builder) {
		mName = builder.mName;
		mCredits = builder.mCredits;
		mSws = builder.mSws;
		mResponsible = builder.mResponsible;
		mTeachers = new ArrayList<String>(builder.mTeachers);
		mLanguages = new ArrayList<Locale>(builder.mLanguages);
		mTeachingForm = builder.mTeachingForm;
		mExpenditure = builder.mExpenditure;
		mRequirements = builder.mRequirements;
		mGoals = builder.mGoals;
		mContent = builder.mContent;
		mMedia = builder.mMedia;
		mLiterature = builder.mLiterature;
		mProgram = builder.mProgram;
		mModulCodes = new ArrayList<ModulCode>(builder.mModulCodes);
	}

	/**
	 * @return the name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @return the credits.
	 */
	public int getCredits() {
		return mCredits;
	}

	/**
	 * @return the semester week hours.
	 */
	public int getSws() {
		return mSws;
	}

	/**
	 * @return the responsible.
	 */
	public String getResponsible() {
		return mResponsible;
	}

	/**
	 * @return the teachers.
	 */
	public List<String> getTeachers() {
		return new ArrayList<String>(mTeachers);
	}

	/**
	 * @return the languages.
	 */
	public List<Locale> getLanguages() {
		return new ArrayList<Locale>(mLanguages);
	}

	/**
	 * @return the teaching form.
	 */
	public TeachingForm getTeachingForm() {
		return mTeachingForm;
	}

	/**
	 * @return the expenditure.
	 */
	public String getExpenditure() {
		return mExpenditure;
	}

	/**
	 * @return the requirements.
	 */
	public String getRequirements() {
		return mRequirements;
	}

	/**
	 * @return the goals.
	 */
	public String getGoals() {
		return mGoals;
	}

	/**
	 * @return the content.
	 */
	public String getContent() {
		return mContent;
	}

	/**
	 * @return the used media.
	 */
	public String getMedia() {
		return mMedia;
	}

	/**
	 * @return the literature.
	 */
	public String getLiterature() {
		return mLiterature;
	}

	/**
	 * @return the program.
	 */
	public Optional<Study> getProgram() {
		return Optional.fromNullable(mProgram);
	}

	/**
	 * @return the modul codes.
	 */
	public List<ModulCode> getModulCodes() {
		return new ArrayList<Modul.ModulCode>(mModulCodes);
	}

	/**
	 * @author Fabio
	 *
	 */
	public static class Builder extends AbstractBuilder<Builder, Modul> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/modul.xml";
		private static final String ROOT_NODE = "/modullist/modul";
		private String mName;
		private int mCredits;
		private int mSws;
		private String mResponsible;
		private List<String> mTeachers;
		private List<Locale> mLanguages;
		private TeachingForm mTeachingForm;
		private String mExpenditure;
		private String mRequirements;
		private String mGoals;
		private String mContent;
		private String mMedia;
		private String mLiterature;
		private Study mProgram;
		private List<ModulCode> mModulCodes;

		public Builder(final Context context) {
			super(context, URL, ROOT_NODE);
		}

		/**
		 * Filter for a key word in the modul name.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addNameFilter(final String keyWord) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return DataUtils
							.containsIgnoreCase(data.getName(), keyWord);
				}
			});
			return this;
		}

		/**
		 * Filter for a semester of a modul code.
		 *
		 * @param semester
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addSemesterFilter(final Semester semester) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					for (final ModulCode code : data.getModulCodes()) {
						for (final Semester codeSemester : code.getSemester()) {
							if (codeSemester == semester) {
								return true;
							}
						}
					}
					return false;
				}
			});
			return this;
		}

		/**
		 * Filter for a language the modul is teached in.
		 *
		 * @param languageCode
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addLanguageFilter(final String languageCode) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return data.getTeachers().contains(
							DataUtils.toLocale(languageCode));
				}
			});
			return this;
		}

		/**
		 * Filter for a teacher who is teaching the modul.
		 *
		 * @param teacher
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addTeacherFilter(final String teacher) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return data.getTeachers().contains(teacher);
				}
			});
			return this;
		}

		/**
		 * Filte for a key word in the goals the student should reach.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addGoalsFilter(final String keyWord) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return DataUtils.containsIgnoreCase(data.getGoals(),
							keyWord);
				}
			});
			return this;
		}

		/**
		 * Filter for a key word in the content description.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addContentFilter(final String keyWord) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return DataUtils.containsIgnoreCase(data.getContent(),
							keyWord);
				}
			});
			return this;
		}

		/**
		 * Filter for a key word in the requiremenets.
		 *
		 * @param keyWord
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addRequirementFilter(final String keyWord) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return DataUtils.containsIgnoreCase(data.getRequirements(),
							keyWord);
				}
			});
			return this;
		}

		/**
		 * Filter for a teaching form.
		 *
		 * @param teachingForm
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addTeachingFormFilter(final TeachingForm teachingForm) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return data.getTeachingForm() == teachingForm;
				}
			});
			return this;
		}

		/**
		 * Filter for a study.
		 *
		 * @param study
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addStudyFilter(final Study study) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					return data.getProgram().isPresent()
							&& data.getProgram().get() == study;
				}
			});
			return this;
		}

		/**
		 * Filter for an offer.
		 *
		 * @param offer
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addOfferFilter(final Offer offer) {
			addFilter(new IFilter<Modul>() {
				@Override
				public boolean apply(final Modul data) {
					for (final ModulCode code : data.getModulCodes()) {
						if (code.getOffer() == offer) {
							return true;
						}
					}
					return false;
				}
			});
			return this;
		}

		@Override
		protected Modul onCreateItem(final String rootPath) throws Exception {
			// reset Variables...
			mTeachers = new ArrayList<String>();
			mLanguages = new ArrayList<Locale>();
			mTeachingForm = null;
			mProgram = null;
			mModulCodes = new ArrayList<Modul.ModulCode>();

			// Parse Elements...
			mName = findByXPath(rootPath + "/name/text()",
					XPathConstants.STRING);
			mCredits = ((Double) findByXPath(rootPath + "/credits/text()",
					XPathConstants.NUMBER)).intValue();
			mSws = ((Double) findByXPath(rootPath + "/sws/text()",
					XPathConstants.NUMBER)).intValue();
			mResponsible = findByXPath(rootPath + "/verantwortlich/text()",
					XPathConstants.STRING);

			final int countTeachers = getCountByXPath(rootPath + "/teacher");
			for (int indexTeacher = 1; indexTeacher <= countTeachers; indexTeacher++) {
				final String teacher = findByXPath(rootPath + "/teacher["
						+ indexTeacher + "]/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(teacher)) {
					mTeachers.add(teacher);
				}
			}

			final int countLanguage = getCountByXPath(rootPath + "/language");
			for (int indexLanguage = 1; indexLanguage <= countLanguage; indexLanguage++) {
				final String language = findByXPath(rootPath + "/language["
						+ indexLanguage + "]/text()", XPathConstants.STRING);
				if (!TextUtils.isEmpty(language)) {
					mLanguages.add(DataUtils.toLocale(language));
				}
			}

			final String teachingForm = findByXPath(rootPath
					+ "/lehrform/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(teachingForm)) {
				mTeachingForm = TeachingForm.of(teachingForm);
			}
			mExpenditure = findByXPath(rootPath + "/aufwand/text()",
					XPathConstants.STRING);
			mRequirements = findByXPath(rootPath + "/voraussetzungen/text()",
					XPathConstants.STRING);
			mGoals = findByXPath(rootPath + "/ziele/text()",
					XPathConstants.STRING);
			mContent = findByXPath(rootPath + "/inhalt/text()",
					XPathConstants.STRING);
			mMedia = findByXPath(rootPath + "/medien/text()",
					XPathConstants.STRING);
			mLiterature = findByXPath(rootPath + "/literatur/text()",
					XPathConstants.STRING);
			final String program = findByXPath(rootPath + "/program/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(program)) {
				mProgram = Study.of(program);
			}

			final int countCodes = getCountByXPath(rootPath
					+ "/modulcodes/modulcode");
			for (int indexCodes = 1; indexCodes <= countCodes; indexCodes++) {
				final String modulCodePath = rootPath
						+ "/modulcodes/modulcode[" + indexCodes + "]";

				final String modul = findByXPath(modulCodePath
						+ "/modul/text()", XPathConstants.STRING);
				final String regulation = findByXPath(modulCodePath
						+ "/regulation/text()", XPathConstants.STRING);
				final String stringOffer = findByXPath(modulCodePath
						+ "/angebot/text()", XPathConstants.STRING);
				Offer offer = null;
				if (!TextUtils.isEmpty(stringOffer)) {
					offer = Offer.of(stringOffer);
				}
				final String services = findByXPath(modulCodePath
						+ "/leistungen/text()", XPathConstants.STRING);
				final String code = findByXPath(modulCodePath + "/code/text()",
						XPathConstants.STRING);

				final List<Semester> semesterList = new ArrayList<Semester>();
				final int countSemester = getCountByXPath(modulCodePath
						+ "/semester");
				for (int indexSemester = 1; indexSemester <= countSemester; indexSemester++) {
					final String semester = findByXPath(modulCodePath
							+ "/semester[" + indexSemester + "]/text()",
							XPathConstants.STRING);
					if (!TextUtils.isEmpty(semester)) {
						semesterList
						.add(Semester.of(Integer.parseInt(semester)));
					}
				}

				final String curriculum = findByXPath(modulCodePath
						+ "/curriculum/text()", XPathConstants.STRING);

				mModulCodes.add(new ModulCode(modul, regulation, offer,
						services, code, semesterList, curriculum));
			}

			return new Modul(this);
		}
	}

	/**
	 * There can be more then one code for a modul. It is a detailed information
	 * about the modul.
	 *
	 * @author Fabio
	 *
	 */
	public static class ModulCode {
		private final String mModul;
		private final String mRegulation;
		private final Offer mOffer;
		private final String mServices;
		private final String mCode;
		private final List<Semester> mSemester;
		private final String mCurriculum;

		private ModulCode(final String modul, final String regulation,
				final Offer offer, final String services, final String code,
				final List<Semester> semesters, final String curriculum) {
			mModul = modul;
			mRegulation = regulation;
			mOffer = offer;
			mServices = services;
			mCode = code;
			mSemester = semesters;
			mCurriculum = curriculum;
		}

		/**
		 * @return the modul.
		 */
		public String getModul() {
			return mModul;
		}

		/**
		 * @return the regulation.
		 */
		public String getRegulation() {
			return mRegulation;
		}

		/**
		 * @return the offer.
		 */
		public Offer getOffer() {
			return mOffer;
		}

		/**
		 * @return the services.
		 */
		public String getServices() {
			return mServices;
		}

		/**
		 * @return the code.
		 */
		public String getCode() {
			return mCode;
		}

		/**
		 * @return the semesters.
		 */
		public List<Semester> getSemester() {
			return mSemester;
		}

		/**
		 * @return curriculum.
		 */
		public String getCurriculum() {
			return mCurriculum;
		}
	}
}
