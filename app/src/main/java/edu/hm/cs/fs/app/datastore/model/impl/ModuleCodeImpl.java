package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.List;

import edu.hm.cs.fs.app.datastore.model.ModuleCode;
import edu.hm.cs.fs.app.datastore.model.constants.Offer;
import edu.hm.cs.fs.app.datastore.model.constants.Semester;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ModuleCodeImpl extends RealmObject implements ModuleCode {
	@PrimaryKey
	private String modul;
	private String regulation;
	private Offer offer;
	private String services;
	private String code;
	private List<Semester> semester;
	private String curriculum;

	public String getModul() {
		return modul;
	}

	public void setModul(String modul) {
		this.modul = modul;
	}

	public String getRegulation() {
		return regulation;
	}

	public void setRegulation(String regulation) {
		this.regulation = regulation;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Semester> getSemester() {
		return semester;
	}

	public void setSemester(List<Semester> semester) {
		this.semester = semester;
	}

	public String getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}
}
