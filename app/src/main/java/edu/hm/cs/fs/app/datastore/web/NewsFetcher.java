package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;
import edu.hm.cs.fs.app.datastore.model.impl.NewsImpl;

/**
 * The news which are shown at the black board. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/news"
 * >http://fi.cs.hm.edu/fi/rest/public/news</a>)
 *
 * @author Fabio
 *
 */
public class NewsFetcher extends AbstractXmlFetcher<NewsFetcher, NewsImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/news.xml";
	private static final String ROOT_NODE = "/newslist/news";
	@SuppressLint("SimpleDateFormat")
	private static final DateFormat DATE_PARSER = new SimpleDateFormat(
			"yyyy-MM-dd");

	public NewsFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected NewsImpl onCreateItem(final String rootPath) throws Exception {
		String mId;
		String mAuthor;
		String mSubject;
		String mText;
		List<String> mTeacherList = new ArrayList<String>();
		List<String> mGroupList = new ArrayList<String>();
		Date mPublish = null;
		Date mExpire = null;
		String mUrl;

		// Parse Elements...
		mId = findByXPath(rootPath + "/id/text()", 
				XPathConstants.STRING);
		mAuthor = findByXPath(rootPath + "/author/text()",
				XPathConstants.STRING);
		mSubject = findByXPath(rootPath + "/subject/text()",
				XPathConstants.STRING);
		mText = findByXPath(rootPath + "/text/text()",
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
				mGroupList.add(groupName);
			}
		}
		
		NewsImpl news = new NewsImpl();
		news.setId(mId);
		news.setAuthor(mAuthor);
		news.setSubject(mSubject);
		news.setText(mText);
		news.setGroups(mGroupList);
		news.setTeachers(mTeacherList);
		news.setPublish(mPublish);
		news.setExpire(mExpire);
		news.setUrl(mUrl);

		return news;
	}
}
