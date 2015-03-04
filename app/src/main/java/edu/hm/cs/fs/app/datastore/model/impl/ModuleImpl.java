package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.List;
import java.util.Locale;

import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.constants.TeachingForm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Fabio on 18.02.2015.
 */
public class ModuleImpl extends RealmObject {
	@PrimaryKey
	private String id;
	private String name;
	private int credits;
	private int sws;
	private String responsible;
	private List<String> teachers;
	private List<Locale> languages;
	private TeachingForm teachingForm;
	private String expenditure;
	private String requirements;
	private String goals;
	private String content;
	private String media;
	private String literature;
	private Study program;
	private List<ModuleCodeImpl> modulCodes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getSws() {
		return sws;
	}

	public void setSws(int sws) {
		this.sws = sws;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public List<String> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<String> teachers) {
		this.teachers = teachers;
	}

	public List<Locale> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Locale> languages) {
		this.languages = languages;
	}

	public TeachingForm getTeachingForm() {
		return teachingForm;
	}

	public void setTeachingForm(TeachingForm teachingForm) {
		this.teachingForm = teachingForm;
	}

	public String getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(String expenditure) {
		this.expenditure = expenditure;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getLiterature() {
		return literature;
	}

	public void setLiterature(String literature) {
		this.literature = literature;
	}

	public Study getProgram() {
		return program;
	}

	public void setProgram(Study program) {
		this.program = program;
	}

	public List<ModuleCodeImpl> getModulCodes() {
		return modulCodes;
	}

	public void setModulCodes(List<ModuleCodeImpl> modulCodes) {
		this.modulCodes = modulCodes;
	}
}
