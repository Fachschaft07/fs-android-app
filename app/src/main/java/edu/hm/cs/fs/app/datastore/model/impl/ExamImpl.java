package edu.hm.cs.fs.app.datastore.model.impl;

import edu.hm.cs.fs.app.datastore.model.realm.RealmString;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Fabio on 18.02.2015.
 */
public class ExamImpl extends RealmObject {
	@PrimaryKey
	private String id;
	private String code;
	private String group;
	private String module;
	private String subtitle;
	private RealmList<RealmString> references;
	private RealmList<RealmString> examiners;
	private String type;
	private String material;
	private String allocation;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public RealmList<RealmString> getReferences() {
		return references;
	}

	public void setReferences(RealmList<RealmString> references) {
		this.references = references;
	}

	public RealmList<RealmString> getExaminers() {
		return examiners;
	}

	public void setExaminers(RealmList<RealmString> examiners) {
		this.examiners = examiners;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getAllocation() {
		return allocation;
	}

	public void setAllocation(String allocation) {
		this.allocation = allocation;
	}
}
