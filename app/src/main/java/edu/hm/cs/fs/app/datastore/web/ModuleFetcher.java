package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;
import edu.hm.cs.fs.app.datastore.model.constants.Offer;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleCodeImpl;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleImpl;
import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;

/**
 * A modul can be choosen by a student. Some moduls are mandatory. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/modul">http://fi.cs.hm.edu/fi/rest/
 * public/modul</a>)
 *
 * @author Fabio
 *
 */
public class ModuleFetcher extends AbstractXmlFetcher<ModuleFetcher, ModuleImpl> {
	private static final String URL = "http://fi.cs.hm.edu/fi/rest/public/modul.xml";
	private static final String ROOT_NODE = "/modullist/modul";

	public ModuleFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

	@Override
	protected ModuleImpl onCreateItem(final String rootPath) throws Exception {
		String name;
		int credits;
		int sws;
		String responsible;
		List<String> teachers = new ArrayList<>();
		List<Locale> languages = new ArrayList<>();
		TeachingForm teachingForm = null;
		String expenditure;
		String requirements;
		String goals;
		String content;
		String media;
		String literature;
		Study program = null;
		List<ModuleCodeImpl> modulCodes = new ArrayList<>();

		// Parse Elements...
		name = findByXPath(rootPath + "/name/text()",
				XPathConstants.STRING);
		credits = ((Double) findByXPath(rootPath + "/credits/text()",
				XPathConstants.NUMBER)).intValue();
		sws = ((Double) findByXPath(rootPath + "/sws/text()",
				XPathConstants.NUMBER)).intValue();
		responsible = findByXPath(rootPath + "/verantwortlich/text()",
				XPathConstants.STRING);

		final int countTeachers = getCountByXPath(rootPath + "/teacher");
		for (int indexTeacher = 1; indexTeacher <= countTeachers; indexTeacher++) {
			final String teacher = findByXPath(rootPath + "/teacher["
					+ indexTeacher + "]/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(teacher)) {
				teachers.add(teacher);
			}
		}

		final int countLanguage = getCountByXPath(rootPath + "/language");
		for (int indexLanguage = 1; indexLanguage <= countLanguage; indexLanguage++) {
			final String language = findByXPath(rootPath + "/language["
					+ indexLanguage + "]/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(language)) {
				languages.add(toLocale(language));
			}
		}

		final String teachingFormStr = findByXPath(rootPath
				+ "/lehrform/text()", XPathConstants.STRING);
		if (!TextUtils.isEmpty(teachingFormStr)) {
			teachingForm = TeachingForm.of(teachingFormStr);
		}
		expenditure = findByXPath(rootPath + "/aufwand/text()",
				XPathConstants.STRING);
		requirements = findByXPath(rootPath + "/voraussetzungen/text()",
				XPathConstants.STRING);
		goals = findByXPath(rootPath + "/ziele/text()",
				XPathConstants.STRING);
		content = findByXPath(rootPath + "/inhalt/text()",
				XPathConstants.STRING);
		media = findByXPath(rootPath + "/medien/text()",
				XPathConstants.STRING);
		literature = findByXPath(rootPath + "/literatur/text()",
				XPathConstants.STRING);
		final String programStr = findByXPath(rootPath + "/program/text()",
				XPathConstants.STRING);
		if (!TextUtils.isEmpty(programStr)) {
			program = Study.of(programStr);
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
			
			ModuleCodeImpl moduleCode = new ModuleCodeImpl();
			moduleCode.setModul(modul);
			moduleCode.setRegulation(regulation);
			moduleCode.setOffer(offer);
			moduleCode.setServices(services);
			moduleCode.setCode(code);
			moduleCode.setSemester(semesterList);
			moduleCode.setCurriculum(curriculum);

			modulCodes.add(moduleCode);
		}
		
		ModuleImpl module = new ModuleImpl();
		module.setName(name);
		module.setCredits(credits);
		module.setSws(sws);
		module.setResponsible(responsible);
		module.setTeachers(teachers);
		module.setLanguages(languages);
		module.setTeachingForm(teachingForm);
		module.setExpenditure(expenditure);
		module.setRequirements(requirements);
		module.setGoals(goals);
		module.setContent(content);
		module.setMedia(media);
		module.setLiterature(literature);
		module.setProgram(program);
		module.setModulCodes(modulCodes);

		return module;
	}

	/**
	 * @param languageCode
	 * @return
	 */
	private static Locale toLocale(final String languageCode) {
		for (final Locale locale : Locale.getAvailableLocales()) {
			if (locale.getLanguage().equalsIgnoreCase(languageCode)) {
				return locale;
			}
		}
		throw new IllegalArgumentException("Not a valid language code: "
				+ languageCode);
	}
}
