package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Fabio on 18.02.2015.
 */
public class NewsImpl extends RealmObject {
    @PrimaryKey
    private String id;
    private String author;
    private String subject;
    private String text;
    private List<String> teachers;
    private List<String> groups;
    private Date publish;
    private Date expire;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public List<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(final List<String> teachers) {
        this.teachers = teachers;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(final List<String> groups) {
        this.groups = groups;
    }

    public Date getPublish() {
        return publish;
    }

    public void setPublish(final Date publish) {
        this.publish = publish;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(final Date expire) {
        this.expire = expire;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
