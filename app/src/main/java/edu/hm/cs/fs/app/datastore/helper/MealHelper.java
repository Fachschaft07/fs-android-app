package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.Meal;
import edu.hm.cs.fs.app.datastore.model.constants.MealType;
import edu.hm.cs.fs.app.datastore.model.impl.MealImpl;
import edu.hm.cs.fs.app.datastore.web.MealFetcher;
import io.realm.Realm;

/**
 * Created by Fabio on 11.03.2015.
 */
public class MealHelper extends BaseHelper implements Meal {
    private final Date date;
    private final MealType type;
    private final String description;

    private MealHelper(final Context context, MealImpl meal) {
        super(context);
        date = meal.getDate();
        type = MealType.of(meal.getType());
        description = meal.getDescription();
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public MealType getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static void listAll(Context context, Callback<List<Meal>> callback) {
        listAll(context, new MealFetcher(context), MealImpl.class, callback, new OnHelperCallback<Meal, MealImpl>() {
            @Override
            public Meal createHelper(final Context context, final MealImpl meal) {
                return new MealHelper(context, meal);
            }

            @Override
            public void copyToRealmOrUpdate(final Realm realm, final MealImpl meal) {
                // TODO Diese Daten in der Datenbank speichern?
            }
        });
    }
}
