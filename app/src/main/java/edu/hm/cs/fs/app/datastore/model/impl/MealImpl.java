package edu.hm.cs.fs.app.datastore.model.impl;

import java.util.Date;

/**
 * Created by Fabio on 18.02.2015.
 */
public class MealImpl {
    private Date date;
    private String description;

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
