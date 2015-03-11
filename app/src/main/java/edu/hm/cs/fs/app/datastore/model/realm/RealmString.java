package edu.hm.cs.fs.app.datastore.model.realm;

import io.realm.RealmObject;

/**
 * Created by Fabio on 07.03.2015.
 */
public class RealmString extends RealmObject {
    private String value;

    public RealmString() {
    }

    public RealmString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
