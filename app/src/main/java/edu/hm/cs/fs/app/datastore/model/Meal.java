package edu.hm.cs.fs.app.datastore.model;

import java.util.Date;

import edu.hm.cs.fs.app.datastore.model.constants.MealType;

/**
 * Created by Fabio on 04.03.2015.
 */
public interface Meal {
    Date getDate();

    MealType getType();

    String getDescription();
}
