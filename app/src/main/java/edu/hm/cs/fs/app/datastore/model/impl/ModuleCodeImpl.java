package edu.hm.cs.fs.app.datastore.model.impl;

import edu.hm.cs.fs.app.datastore.model.realm.RealmString;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ModuleCodeImpl extends RealmObject {
	@PrimaryKey
	private String modul;
	private String regulation;
	private String offer;
	private String services;
	private String code;
	private RealmList<RealmString> semesters;
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

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
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

	public RealmList<RealmString> getSemesters() {
		return semesters;
	}

	public void setSemesters(RealmList<RealmString> semesters) {
		this.semesters = semesters;
	}

	public String getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}
}
