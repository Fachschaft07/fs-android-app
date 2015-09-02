package edu.hm.cs.fs.app.database.model;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.common.model.LostFound;

/**
 * @author Fabio
 */
public class LostFoundModel implements IModel {

    public void getLostFound(@NonNull final ICallback<List<LostFound>> callback) {
        // TODO Connection to the Rest-Controller
        LostFound item = new LostFound();
        item.setDate(new Date());
        item.setSubject("USB-Stick");

        callback.onSuccess(Arrays.asList(item));
    }
}
