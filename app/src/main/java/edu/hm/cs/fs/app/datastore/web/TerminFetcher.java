package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.impl.TerminImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;

/**
 * The appointments at the faculty 07. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/termin.xml"
 * >http://fi.cs.hm.edu/fi/rest/public/termin.xml</a>)
 *
 * @author Fabio
 *
 */
public class TerminFetcher extends
		AbstractXmlFetcher<TerminFetcher, TerminImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/termin.xml";
	private static final String ROOT_NODE = "/terminlist/termin";
	@SuppressLint("SimpleDateFormat")
	private static final DateFormat DATE_PARSER = new SimpleDateFormat(
			"yyyy-MM-dd");

	public TerminFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected TerminImpl onCreateItem(final String rootPath)
			throws Exception {
		String id;
		String subject;
		String scope;
		Date date = null;

		// Parse Elements...
		id = findByXPath(rootPath + "/id/text()",
				XPathConstants.STRING);
		subject = findByXPath(rootPath + "/subject/text()",
				XPathConstants.STRING);
		scope = findByXPath(rootPath + "/scope/text()", XPathConstants.STRING);
		final String dateStr = findByXPath(rootPath + "/date/text()",
				XPathConstants.STRING);
		if (!TextUtils.isEmpty(dateStr)) {
			date = DATE_PARSER.parse(dateStr);
		}
		
		TerminImpl termin = new TerminImpl();
		termin.setId(id);
		termin.setSubject(subject);
		termin.setScope(scope);
		termin.setDate(date);

		return termin;
	}
}
