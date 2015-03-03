package edu.hm.cs.fs.app.datastore.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Fabio on 03.03.2015.
 */
public interface News {
    Person getAuthor();

    String getSubject();

    String getText();

    List<Person> getTeachers();

    List<Group> getGroups();

    Date getPublish();

    Date getExpire();

    String getUrl();
}
