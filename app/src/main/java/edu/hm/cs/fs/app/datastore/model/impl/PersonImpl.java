package edu.hm.cs.fs.app.datastore.model.impl;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Fabio on 18.02.2015.
 */
public class PersonImpl extends RealmObject {
    @PrimaryKey
    private String id;
    private String lastName;
    private String firstName;
    private String sex;
    private String title;
    private String faculty;
    private String status;
    private boolean hidden;
    private String email;
    private String website;
    private String phone;
    private String function;
    private String focus;
    private String publication;
    private String office;
    private boolean emailOptin;
    private boolean referenceOptin;
    private String officeHourWeekday;
    private String officeHourTime;
    private String officeHourRoom;
    private String officeHourComment;
    private String einsichtDate;
    private String einsichtTime;
    private String einsichtRoom;
    private String einsichtComment;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(final String faculty) {
        this.faculty = faculty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(final String function) {
        this.function = function;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(final String focus) {
        this.focus = focus;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(final String publication) {
        this.publication = publication;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(final String office) {
        this.office = office;
    }

    public boolean isEmailOptin() {
        return emailOptin;
    }

    public void setEmailOptin(final boolean emailOptin) {
        this.emailOptin = emailOptin;
    }

    public boolean isReferenceOptin() {
        return referenceOptin;
    }

    public void setReferenceOptin(final boolean referenceOptin) {
        this.referenceOptin = referenceOptin;
    }

    public String getOfficeHourWeekday() {
        return officeHourWeekday;
    }

    public void setOfficeHourWeekday(final String officeHourWeekday) {
        this.officeHourWeekday = officeHourWeekday;
    }

    public String getOfficeHourTime() {
        return officeHourTime;
    }

    public void setOfficeHourTime(final String officeHourTime) {
        this.officeHourTime = officeHourTime;
    }

    public String getOfficeHourRoom() {
        return officeHourRoom;
    }

    public void setOfficeHourRoom(final String officeHourRoom) {
        this.officeHourRoom = officeHourRoom;
    }

    public String getOfficeHourComment() {
        return officeHourComment;
    }

    public void setOfficeHourComment(final String officeHourComment) {
        this.officeHourComment = officeHourComment;
    }

    public String getEinsichtDate() {
        return einsichtDate;
    }

    public void setEinsichtDate(final String einsichtDate) {
        this.einsichtDate = einsichtDate;
    }

    public String getEinsichtTime() {
        return einsichtTime;
    }

    public void setEinsichtTime(final String einsichtTime) {
        this.einsichtTime = einsichtTime;
    }

    public String getEinsichtRoom() {
        return einsichtRoom;
    }

    public void setEinsichtRoom(final String einsichtRoom) {
        this.einsichtRoom = einsichtRoom;
    }

    public String getEinsichtComment() {
        return einsichtComment;
    }

    public void setEinsichtComment(final String einsichtComment) {
        this.einsichtComment = einsichtComment;
    }
}
