package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.impl.LostFoundImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;

/**
 * The things which gone lost and found. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/lostfound"
 * >http://fi.cs.hm.edu/fi/rest/public/lostfound</a>)
 *
 * @author Fabio
 *
 */
public class LostFoundFetcher extends AbstractXmlFetcher<LostFoundFetcher, LostFoundImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/lostfound.xml";
	private static final String ROOT_NODE = "/lostfoundlist/lostfound";
	@SuppressLint("SimpleDateFormat")
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat(
			"yyyy-MM-dd");

	public LostFoundFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected LostFoundImpl onCreateItem(final String rootPath)
			throws Exception {
		String id;
		String subject;
		Date date;
		
		// Parse Elements...
		id = findByXPath(rootPath + "/id/text()",
				XPathConstants.STRING);
		subject = findByXPath(rootPath + "/subject/text()",
				XPathConstants.STRING);
		date = DATE_FORMATTER.parse((String) findByXPath(rootPath
				+ "/date/text()", XPathConstants.STRING));
		
		LostFoundImpl lostFound = new LostFoundImpl();
		lostFound.setId(id);
		lostFound.setSubject(subject);
		lostFound.setDate(date);

		return lostFound;
	}
}
