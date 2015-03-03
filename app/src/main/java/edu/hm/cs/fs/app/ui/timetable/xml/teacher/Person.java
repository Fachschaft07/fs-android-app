package edu.hm.cs.fs.app.ui.timetable.xml.teacher;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "person", strict = false)
public class Person {
	@Element(required = false)
	private String lastname;

	@Element(required = false)
	private String firstname;

	@Element(required = false)
	private String title;

	@Element(required = false)
	private String status;

	@Element(required = false)
	private String phone;

	@Element(required = false)
	private String website;

	@Element(required = false)
	private String officehourweekday;

	@Element(required = false)
	private String officehourtime;

	@Element(required = false)
	private String id;

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(final String website) {
		this.website = website;
	}

	public String getOfficehourweekday() {
		return this.officehourweekday;
	}

	public void setOfficehourweekday(final String officehourweekday) {
		this.officehourweekday = officehourweekday;
	}

	public String getOfficehourtime() {
		return this.officehourtime;
	}

	public void setOfficehourtime(final String officehourtime) {
		this.officehourtime = officehourtime;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}
}
