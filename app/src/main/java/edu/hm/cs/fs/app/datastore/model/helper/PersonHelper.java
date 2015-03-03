package edu.hm.cs.fs.app.datastore.model.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.Person;
import edu.hm.cs.fs.app.datastore.model.impl.PersonImpl;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class PersonHelper implements Person {
    private final Context mContext;

    private PersonHelper(Context context, PersonImpl person) {
        mContext = context;
    }

    static Person findById(final Context context, final String id) {
        return new RealmExecutor<Person>(context) {
            @Override
            public Person run(final Realm realm) {
                return new PersonHelper(
                        context,
                        realm.where(PersonImpl.class).equalTo("id", id).findFirst()
                );
            }
        }.execute();
    }

    static List<Person> listAll(final Context context) {
        return new RealmExecutor<List<Person>>(context) {
            @Override
            public List<Person> run(final Realm realm) {
                List<Person> helperList = new ArrayList<>();
                for (PersonImpl person : realm.where(PersonImpl.class).findAll()) {
                    helperList.add(new PersonHelper(context, person));
                }
                return helperList;
            }
        }.execute();
    }
}
