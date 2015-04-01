package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;

import javax.xml.xpath.XPathConstants;

import edu.hm.cs.fs.app.datastore.model.constants.Offer;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleCodeImpl;
import edu.hm.cs.fs.app.datastore.model.impl.ModuleImpl;
import edu.hm.cs.fs.app.datastore.model.realm.RealmString;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractXmlFetcher;
import io.realm.RealmList;

/**
 * A modul can be choosen by a student. Some moduls are mandatory. (Url: <a
 * href="http://fi.cs.hm.edu/fi/rest/public/modul">http://fi.cs.hm.edu/fi/rest/
 * public/modul</a>)
 *
 * @author Fabio
 *
 */
public class ModuleFetcher extends AbstractXmlFetcher<ModuleFetcher, ModuleImpl> {
    private static final String BASE_URL = "http://fi.cs.hm.edu/fi/rest/public/";
	private static final String URL = BASE_URL + "modul.xml";
	private static final String ROOT_NODE = "/modullist/modul";

	public ModuleFetcher(final Context context) {
		super(context, URL, ROOT_NODE);
	}

    public ModuleFetcher(final Context context, String moduleId) {
        super(context, BASE_URL + "modul/title/"+moduleId+".xml", "modul");
    }

	@Override
	protected ModuleImpl onCreateItem(final String rootPath) throws Exception {
		String name;
		int credits;
		int sws;
		String responsible;
		RealmList<RealmString> teachers = new RealmList<>();
        RealmList<RealmString> languages = new RealmList<>();
		TeachingForm teachingForm = null;
		String expenditure;
		String requirements;
		String goals;
		String content;
		String media;
		String literature;
		Study program = null;
        RealmList<ModuleCodeImpl> modulCodes = new RealmList<>();

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
				teachers.add(new RealmString(teacher));
			}
		}

		final int countLanguage = getCountByXPath(rootPath + "/language");
		for (int indexLanguage = 1; indexLanguage <= countLanguage; indexLanguage++) {
			final String language = findByXPath(rootPath + "/language["
					+ indexLanguage + "]/text()", XPathConstants.STRING);
			if (!TextUtils.isEmpty(language)) {
				languages.add(new RealmString(language));
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

			final RealmList<RealmString> semesterList = new RealmList<>();
			final int countSemester = getCountByXPath(modulCodePath
					+ "/semester");
			for (int indexSemester = 1; indexSemester <= countSemester; indexSemester++) {
				final String semester = findByXPath(modulCodePath
						+ "/semester[" + indexSemester + "]/text()",
						XPathConstants.STRING);
				if (!TextUtils.isEmpty(semester)) {
					semesterList
					.add(new RealmString(Semester.of(Integer.parseInt(semester)).toString()));
				}
			}

			final String curriculum = findByXPath(modulCodePath
					+ "/curriculum/text()", XPathConstants.STRING);
			
			ModuleCodeImpl moduleCode = new ModuleCodeImpl();
			moduleCode.setModul(modul);
			moduleCode.setRegulation(regulation);
			moduleCode.setOffer(offer.toString());
			moduleCode.setServices(services);
			moduleCode.setCode(code);
			moduleCode.setSemesters(semesterList);
			moduleCode.setCurriculum(curriculum);

			modulCodes.add(moduleCode);
		}
		
		ModuleImpl module = new ModuleImpl();
        module.setId(name.replaceAll("\\s", "").replaceAll("[^A-z0-9]+", "").toLowerCase(Locale.getDefault()));
		module.setName(name);
		module.setCredits(credits);
		module.setSws(sws);
		module.setResponsible(responsible);
		module.setTeachers(teachers);
		module.setLanguages(languages);
		module.setTeachingForm(teachingForm.toString());
		module.setExpenditure(expenditure);
		module.setRequirements(requirements);
		module.setGoals(goals);
		module.setContent(content);
		module.setMedia(media);
		module.setLiterature(literature);
        if(program != null) {
            module.setProgram(program.toString());
        }
		module.setModulCodes(modulCodes);

		return module;
	}
}
