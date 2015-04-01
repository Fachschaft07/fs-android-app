package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.text.TextUtils;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;
import edu.hm.cs.fs.app.datastore.model.constants.Day;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.PersonStatus;
import edu.hm.cs.fs.app.datastore.model.constants.Sex;
import edu.hm.cs.fs.app.datastore.model.impl.PersonImpl;

/**
 * Every persons who work at the {@link Faculty#_07}. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/person"
 * >http://fi.cs.hm.edu/fi/rest/public/person</a>)
 *
 * @author Fabio
 *
 */
public class PersonFetcher extends AbstractXmlFetcher<PersonFetcher, PersonImpl> {
    private static final String BASE_URL = "http://fi.cs.hm.edu/fi/rest/public/";
	private static final String URL = BASE_URL + "person.xml";
	private static final String ROOT_NODE = "/persons/person";

	public PersonFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

    public PersonFetcher(final Context context, final String personId) {
        super(context, BASE_URL + "person/name/"+personId+".xml", "person");
    }

	@Override
	protected PersonImpl onCreateItem(final String rootPath) throws Exception {
		String mId;
		String mLastName;
		String mFirstName;
		Sex mSex = null;
		String mTitle;
		Faculty mFaculty = null;
		PersonStatus mStatus = null;
		boolean mHidden;
		String mEmail;
		String mWebsite;
		String mPhone;
		String mFunction;
		String mFocus;
		String mPublication;
		String mOffice;
		boolean mEmailOptin;
		boolean mReferenceOptin;
		Day mOfficeHourWeekday = null;
		String mOfficeHourTime;
		String mOfficeHourRoom;
		String mOfficeHourComment;
		String mEinsichtDate;
		String mEinsichtTime;
		String mEinsichtRoom;
		String mEinsichtComment;

		// Parse Elements...
		mId = findByXPath(rootPath + "/id/text()",
				XPathConstants.STRING);
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
				XPathConstants.BOOLEAN);
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
				XPathConstants.BOOLEAN);
		mReferenceOptin = findByXPath(rootPath + "/referenceoptin/text()",
				XPathConstants.BOOLEAN);
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

		PersonImpl person = new PersonImpl();
		person.setId(mId);
		person.setLastName(mLastName);
		person.setFirstName(mFirstName);
		person.setSex(mSex.toString());
		person.setTitle(mTitle);
		person.setFaculty(mFaculty.toString());
		person.setStatus(mStatus.toString());
		person.setHidden(mHidden);
		person.setEmail(mEmail);
		person.setPhone(mPhone);
		person.setWebsite(mWebsite);
		person.setFunction(mFunction);
		person.setFocus(mFocus);
		person.setPublication(mPublication);
		person.setOffice(mOffice);
		person.setEmailOptin(mEmailOptin);
		person.setReferenceOptin(mReferenceOptin);
		person.setOfficeHourWeekday(mOfficeHourWeekday == null ? null : mOfficeHourWeekday.toString());
		person.setOfficeHourTime(mOfficeHourTime);
		person.setOfficeHourRoom(mOfficeHourRoom);
		person.setOfficeHourComment(mOfficeHourComment);
		person.setEinsichtDate(mEinsichtDate);
		person.setEinsichtTime(mEinsichtTime);
		person.setEinsichtRoom(mEinsichtRoom);
		person.setEinsichtComment(mEinsichtComment);
		
		return person;
	}
}
