package com.fk07.backend.web.data;

import javax.xml.xpath.XPathConstants;

import android.content.Context;
import android.text.TextUtils;

import com.fk07.backend.web.data.builder.AbstractBuilder;
import com.fk07.backend.web.data.builder.IFilter;
import com.fk07.backend.web.data.constants.Day;
import com.fk07.backend.web.data.constants.Faculty;
import com.fk07.backend.web.data.constants.PersonStatus;
import com.fk07.backend.web.data.constants.Sex;

/**
 * Every persons who work at the {@link Faculty#_07}. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/person"
 * >http://fi.cs.hm.edu/fi/rest/public/person</a>)
 *
 * @author Fabio
 *
 */
public class Person {
	private final String mLastName;
	private final String mFirstName;
	private final Sex mSex;
	private final String mTitle;
	private final Faculty mFaculty;
	private final PersonStatus mStatus;
	private final boolean mHidden;
	private final String mEmail;
	private final String mWebsite;
	private final String mPhone;
	private final String mFunction;
	private final String mFocus;
	private final String mPublication;
	private final String mOffice;
	private final boolean mEmailOptin;
	private final boolean mReferenceOptin;
	private final Day mOfficeHourWeekday;
	private final String mOfficeHourTime;
	private final String mOfficeHourRoom;
	private final String mOfficeHourComment;
	private final String mEinsichtDate;
	private final String mEinsichtTime;
	private final String mEinsichtRoom;
	private final String mEinsichtComment;

	private Person(final Builder builder) {
		mLastName = builder.mLastName;
		mFirstName = builder.mFirstName;
		mSex = builder.mSex;
		mTitle = builder.mTitle;
		mFaculty = builder.mFaculty;
		mStatus = builder.mStatus;
		mHidden = builder.mHidden;
		mEmail = builder.mEmail;
		mWebsite = builder.mWebsite;
		mPhone = builder.mPhone;
		mFunction = builder.mFunction;
		mFocus = builder.mFocus;
		mPublication = builder.mPublication;
		mOffice = builder.mOffice;
		mEmailOptin = builder.mEmailOptin;
		mReferenceOptin = builder.mReferenceOptin;
		mOfficeHourWeekday = builder.mOfficeHourWeekday;
		mOfficeHourTime = builder.mOfficeHourTime;
		mOfficeHourRoom = builder.mOfficeHourRoom;
		mOfficeHourComment = builder.mOfficeHourComment;
		mEinsichtDate = builder.mEinsichtDate;
		mEinsichtTime = builder.mEinsichtTime;
		mEinsichtRoom = builder.mEinsichtRoom;
		mEinsichtComment = builder.mEinsichtComment;
	}

	/**
	 * @return the last name.
	 */
	public String getLastName() {
		return mLastName;
	}

	/**
	 * @return the first name.
	 */
	public String getFirstName() {
		return mFirstName;
	}

	/**
	 * @return the sex.
	 */
	public Sex getSex() {
		return mSex;
	}

	/**
	 * @return the title.
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * @return the faculty.
	 */
	public Faculty getFaculty() {
		return mFaculty;
	}

	/**
	 * @return the status.
	 */
	public PersonStatus getStatus() {
		return mStatus;
	}

	/**
	 * @return <code>true</code> if this person is normaly hidden from the list.
	 */
	public boolean isHidden() {
		return mHidden;
	}

	/**
	 * @return the mail.
	 */
	public String getEmail() {
		return mEmail;
	}

	/**
	 * @return the website.
	 */
	public String getWebsite() {
		return mWebsite;
	}

	/**
	 * @return the phone.
	 */
	public String getPhone() {
		return mPhone;
	}

	/**
	 * @return the function.
	 */
	public String getFunction() {
		return mFunction;
	}

	/**
	 * @return the focus.
	 */
	public String getFocus() {
		return mFocus;
	}

	/**
	 * @return the publication.
	 */
	public String getPublication() {
		return mPublication;
	}

	/**
	 * @return the office.
	 */
	public String getOffice() {
		return mOffice;
	}

	/**
	 * @return <code>true</code> if an email adress is available.
	 */
	public boolean isEmailOptin() {
		return mEmailOptin;
	}

	/**
	 * @return <code>true</code> if a reference is available.
	 */
	public boolean isReferenceOptin() {
		return mReferenceOptin;
	}

	/**
	 * @return the weekday of the office hour.
	 */
	public Day getOfficeHourWeekday() {
		return mOfficeHourWeekday;
	}

	/**
	 * @return the time (HH:mm) of the office hour.
	 */
	public String getOfficeHourTime() {
		return mOfficeHourTime;
	}

	/**
	 * @return the room where the office hour is.
	 */
	public String getOfficeHourRoom() {
		return mOfficeHourRoom;
	}

	/**
	 * @return a comment to the office hour.
	 */
	public String getOfficeHourComment() {
		return mOfficeHourComment;
	}

	/**
	 * @return a date of einsicht.
	 */
	public String getEinsichtDate() {
		return mEinsichtDate;
	}

	/**
	 * @return a time of einsicht.
	 */
	public String getEinsichtTime() {
		return mEinsichtTime;
	}

	/**
	 * @return a room of einsicht.
	 */
	public String getEinsichtRoom() {
		return mEinsichtRoom;
	}

	/**
	 * @return a comment of einsicht.
	 */
	public String getEinsichtComment() {
		return mEinsichtComment;
	}

	/**
	 * @author Fabio
	 *
	 */
	public static class Builder extends AbstractBuilder<Builder, Person> {
		private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/person.xml";
		private static final String ROOT_NODE = "person";
		private String mLastName;
		private String mFirstName;
		private Sex mSex;
		private String mTitle;
		private Faculty mFaculty;
		private PersonStatus mStatus;
		private boolean mHidden;
		private String mEmail;
		private String mWebsite;
		private String mPhone;
		private String mFunction;
		private String mFocus;
		private String mPublication;
		private String mOffice;
		private boolean mEmailOptin;
		private boolean mReferenceOptin;
		private Day mOfficeHourWeekday;
		private String mOfficeHourTime;
		private String mOfficeHourRoom;
		private String mOfficeHourComment;
		private String mEinsichtDate;
		private String mEinsichtTime;
		private String mEinsichtRoom;
		private String mEinsichtComment;

		/**
		 * Creates a new builder.
		 *
		 * @param context
		 */
		public Builder(final Context context) {
			super(context, URL, ROOT_NODE);
		}

		/**
		 * Filters for a faculty.
		 *
		 * @param faculty
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addFacultyFilter(final Faculty faculty) {
			addFilter(new IFilter<Person>() {
				@Override
				public boolean apply(final Person data) {
					return data.getFaculty() == faculty;
				}
			});
			return this;
		}

		/**
		 * Searches for a name in the first and last name.
		 *
		 * @param name
		 *            to filter for.
		 * @return the builder.
		 */
		public Builder addNameFilter(final String name) {
			addFilter(new IFilter<Person>() {
				@Override
				public boolean apply(final Person data) {
					return data.getFirstName().equalsIgnoreCase(name)
							|| data.getLastName().equalsIgnoreCase(name);
				}
			});
			return this;
		}

		@Override
		protected Person onCreateItem(final String rootPath) throws Exception {
			// reset Variables...
			mSex = null;
			mFaculty = null;
			mStatus = null;
			mOfficeHourWeekday = null;

			// Parse Elements...
			mLastName = findByXPath(rootPath + "/lastname/text()",
					XPathConstants.STRING);
			mFirstName = findByXPath(rootPath + "/firstname/text()",
					XPathConstants.STRING);
			final String sex = findByXPath(rootPath + "/sex/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(sex)) {
				mSex = Sex.of(sex);
			}
			mTitle = findByXPath(rootPath + "/title/text()",
					XPathConstants.STRING);
			final String faculty = findByXPath(rootPath + "/faculty/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(faculty)) {
				mFaculty = Faculty.of(faculty);
			}
			final String status = findByXPath(rootPath + "/status/text()",
					XPathConstants.STRING);
			if (!TextUtils.isEmpty(status)) {
				mStatus = PersonStatus.of(status);
			}
			mHidden = findByXPath(rootPath + "/hidden/text()",
					XPathConstants.STRING);
			mEmail = findByXPath(rootPath + "/email/text()",
					XPathConstants.STRING);
			mWebsite = findByXPath(rootPath + "/website/text()",
					XPathConstants.STRING);
			mPhone = findByXPath(rootPath + "/phone/text()",
					XPathConstants.STRING);
			if (mPhone.length() == 5) {
				// Vierstellige Telefonnummern sind Durchwahlen nach 089-1265-
				// Remove all '-'
				mPhone = "0891265" + mPhone.substring(1);
			}
			mFunction = findByXPath(rootPath + "/function/text()",
					XPathConstants.STRING);
			mFocus = findByXPath(rootPath + "/focus/text()",
					XPathConstants.STRING);
			mPublication = findByXPath(rootPath + "/publication/text()",
					XPathConstants.STRING);
			mOffice = findByXPath(rootPath + "/office/text()",
					XPathConstants.STRING);
			mEmailOptin = findByXPath(rootPath + "/emailoptin/text()",
					XPathConstants.STRING);
			mReferenceOptin = findByXPath(rootPath + "/referenceoptin/text()",
					XPathConstants.STRING);
			final String officeHourWeekday = findByXPath(rootPath
					+ "/officehourweekday/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(officeHourWeekday)) {
				mOfficeHourWeekday = Day.of(officeHourWeekday);
			}
			mOfficeHourTime = findByXPath(rootPath + "/officehourtime/text()",
					XPathConstants.STRING);
			mOfficeHourRoom = findByXPath(rootPath + "/officehourroom/text()",
					XPathConstants.STRING);
			mOfficeHourComment = findByXPath(rootPath
					+ "/officehourcomment/text()", XPathConstants.STRING);
			mEinsichtDate = findByXPath(rootPath + "/einsichtdate/text()",
					XPathConstants.STRING);
			mEinsichtTime = findByXPath(rootPath + "/einsichttime/text()",
					XPathConstants.STRING);
			mEinsichtRoom = findByXPath(rootPath + "/einsichtroom/text()",
					XPathConstants.STRING);
			mEinsichtComment = findByXPath(
					rootPath + "/einsichtcomment/text()", XPathConstants.STRING);

			return new Person(this);
		}
	}
}
