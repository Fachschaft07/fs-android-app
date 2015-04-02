package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.hm.cs.fs.app.datastore.model.Presence;
import edu.hm.cs.fs.app.datastore.model.impl.PresenceImpl;
import edu.hm.cs.fs.app.datastore.web.PresenceFetcher;
import edu.hm.cs.fs.app.util.PrefUtils;
import hugo.weaving.DebugLog;

/**
 * Created by Fabio on 03.03.2015.
 */
public class PresenceHelper extends BaseHelper implements Presence {
    private final String name;
    private final String status;

    PresenceHelper(Context context, PresenceImpl presence) {
        super(context);
        name = presence.getName();
        status = presence.getStatus();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean isBusy() {
        return "Busy".equalsIgnoreCase(status);
    }

    @DebugLog
    public static boolean isPresent(List<Presence> presenceList) {
        boolean present = false;
        for (Presence presence : presenceList) {
            if (!presence.isBusy()) {
                present = true;
            }
        }
        return present;
    }

    @DebugLog
    public static void isPresent(Context context, final Callback<Boolean> callback) {
        listAll(context, new Callback<List<Presence>>() {
            @Override
            public void onResult(List<Presence> result) {
                callback.onResult(isPresent(result));
            }
        });
    }

    @DebugLog
    public static void listAll(final Context context, final Callback<List<Presence>> callback) {
        PrefUtils.setUpdateInterval(context, PresenceFetcher.class, TimeUnit.MILLISECONDS.convert(1l, TimeUnit.MINUTES));

        listAllOnline(context, new PresenceFetcher(context), PresenceImpl.class, callback, new OnHelperCallback<Presence, PresenceImpl>() {
            @Override
            public Presence createHelper(Context context, PresenceImpl impl) {
                return new PresenceHelper(context, impl);
            }
        });
    }
}
