package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.JobImpl;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;

/**
 * The offered jobs. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/job">http:
 * //fi.cs.hm.edu/fi/rest/public/job</a>)
 *
 * @author Fabio
 *
 */
public class JobFetcher extends AbstractXmlFetcher<JobFetcher, JobImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/job.xml";
	private static final String ROOT_NODE = "/joblist/job";
	@SuppressLint("SimpleDateFormat")
	private static final DateFormat DATE_PARSER = new SimpleDateFormat(
			"yyyy-MM-dd");

	public JobFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected JobImpl onCreateItem(final String rootPath) throws Exception {
		String id;
		String title;
		String provider;
		String description;
		Study program = null;
		String contact;
		Date expire = null;
		String url;

		// Parse Elements...
		id = findByXPath(rootPath + "/id/text()",
				XPathConstants.STRING);
		title = findByXPath(rootPath + "/title/text()",
				XPathConstants.STRING);
		provider = findByXPath(rootPath + "/provider/text()",
				XPathConstants.STRING);
		description = findByXPath(rootPath + "/description/text()",
				XPathConstants.STRING);
		contact = findByXPath(rootPath + "/contact/text()",
				XPathConstants.STRING);
		final String programStr = findByXPath(rootPath + "/program/text()",
				XPathConstants.STRING);
		if (!TextUtils.isEmpty(programStr)) {
			program = GroupImpl.of(programStr).getStudy();
		}
		expire = DATE_PARSER.parse((String) findByXPath(rootPath
				+ "/expire/text()", XPathConstants.STRING));
		url = findByXPath(rootPath + "/url/text()", XPathConstants.STRING);
		
		JobImpl job = new JobImpl();
		job.setId(id);
		job.setTitle(title);
		job.setProvider(provider);
		job.setDescription(description);
		job.setProgram(program == null ? null : program.toString());
		job.setContact(contact);
		job.setExpire(expire);
		job.setUrl(url);

		return job;
	}
}
