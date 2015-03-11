package edu.hm.cs.fs.app.datastore.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.constants.ExamGroup;
import edu.hm.cs.fs.app.datastore.model.constants.ExamType;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.ExamImpl;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import edu.hm.cs.fs.app.datastore.model.realm.RealmString;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;
import io.realm.RealmList;

/**
 * The exams with every information. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/exam"
 * >http://fi.cs.hm.edu/fi/rest/public/exam</a>)
 *
 * @author Fabio
 *
 */
public class ExamFetcher extends AbstractXmlFetcher<ExamFetcher, ExamImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/exam.xml";
	private static final String ROOT_NODE = "/examlist/exam";
	@SuppressLint("SimpleDateFormat")
	private static final DateFormat DATE_PARSER = new SimpleDateFormat(
			"dd.MM.yyyy");

	public ExamFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected ExamImpl onCreateItem(final String rootPath) throws Exception {
		String id;
		String code;
		Study group = null;
		String modul;
		String subtitle;
		RealmList<RealmString> references = new RealmList<>();
        RealmList<RealmString> examiners = new RealmList<>();
		ExamType type = null;
		String material;
		ExamGroup allocation = null;

		// Parse Elements...
		id = findByXPath(rootPath + "/id/text()",
				XPathConstants.STRING);
		code = findByXPath(rootPath + "/code/text()",
				XPathConstants.STRING);
		final String groupStr = findByXPath(rootPath + "/program/text()",
				XPathConstants.STRING);
		if (!TextUtils.isEmpty(groupStr)) {
			group = GroupImpl.of(groupStr).getStudy();
		}
		modul = findByXPath(rootPath + "/modul/text()",
				XPathConstants.STRING);
		subtitle = findByXPath(rootPath + "/subtitle/text()",
				XPathConstants.STRING);

		final int countRef = getCountByXPath(rootPath + "/reference");
		for (int indexRef = 1; indexRef <= countRef; indexRef++) {
			final String ref = findByXPath(rootPath + "/reference["
					+ indexRef + "]/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(ref)) {
				references.add(new RealmString(ref));
			}
		}

		final int countExaminer = getCountByXPath(rootPath + "/examiner");
		for (int indexExaminer = 1; indexExaminer <= countExaminer; indexExaminer++) {
			final String examiner = findByXPath(rootPath + "/examiner["
					+ indexExaminer + "]/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(examiner)) {
				examiners.add(new RealmString(examiner));
			}
		}

		final String typeStr = findByXPath(rootPath + "/type/text()",
				XPathConstants.STRING);
		if (!TextUtils.isEmpty(typeStr)) {
			type = ExamType.of(typeStr);
		}

		material = findByXPath(rootPath + "/material/text()",
				XPathConstants.STRING);
		final String allocationStr = findByXPath(rootPath
				+ "/allocation/text()", XPathConstants.STRING);
		if (!TextUtils.isEmpty(allocationStr)) {
			allocation = ExamGroup.of(allocationStr);
		}
		
		ExamImpl exam = new ExamImpl();
		exam.setId(id);
		exam.setModule(modul);
		exam.setAllocation(allocation.toString());
		exam.setCode(code);
		exam.setExaminers(examiners);
		exam.setGroup(group.toString());
		exam.setMaterial(material);
		exam.setReferences(references);
		exam.setSubtitle(subtitle);
		exam.setType(type.toString());

		return exam;
	}
}
