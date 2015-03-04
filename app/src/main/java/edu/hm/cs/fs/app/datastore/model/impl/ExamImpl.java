package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.List;

import edu.hm.cs.fs.app.datastore.model.constants.ExamGroup;
import edu.hm.cs.fs.app.datastore.model.constants.ExamType;
import edu.hm.cs.fs.app.datastore.model.constants.Study;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Fabio on 18.02.2015.
 */
public class ExamImpl extends RealmObject {
	@PrimaryKey
	private String id;
	private String code;
	private Study group;
	private String module;
	private String subtitle;
	private List<Study> references;
	private List<String> examiners;
	private ExamType type;
	private String material;
	private ExamGroup allocation;

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

	public Study getGroup() {
		return group;
	}

	public void setGroup(Study group) {
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

	public List<Study> getReferences() {
		return references;
	}

	public void setReferences(List<Study> references) {
		this.references = references;
	}

	public List<String> getExaminers() {
		return examiners;
	}

	public void setExaminers(List<String> examiners) {
		this.examiners = examiners;
	}

	public ExamType getType() {
		return type;
	}

	public void setType(ExamType type) {
		this.type = type;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public ExamGroup getAllocation() {
		return allocation;
	}

	public void setAllocation(ExamGroup allocation) {
		this.allocation = allocation;
	}
}
