package edu.hm.cs.fs.app.datastore.helper;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.Presence;
import edu.hm.cs.fs.app.datastore.model.impl.PresenceImpl;
import edu.hm.cs.fs.app.datastore.web.PresenceFetcher;
import edu.hm.cs.fs.app.util.NetworkUtils;
import io.realm.Realm;

/**
 * Created by Fabio on 03.03.2015.
 */
public class PresenceHelper implements Presence {
    private final Context mContext;
    private final String name;
    private final String status;

    private PresenceHelper(Context context, PresenceImpl presence) {
        mContext = context;
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

	public static void isPresent(Context context, final Callback<Boolean> callback) {
		listAll(context, new Callback<List<Presence>>() {
			@Override
			public void onResult(List<Presence> result) {
        		boolean present = false;
        		for (Presence presence : result) {
        			if (!presence.isBusy()) {
        				present = true;
        			}
        		}
            	callback.onResult(present);
			}
		});
	}
    
    public static void listAll(final Context context, final Callback<List<Presence>> callback) {
    	// Request data from web...
    	new RealmExecutor<List<Presence>>(context) {
            @Override
            public List<Presence> run(final Realm realm) {
            	List<Presence> result = new ArrayList<>();
            	if(NetworkUtils.isConnected(context)) {
                	List<PresenceImpl> presenceImplList = fetchOnlineData(context, realm);
                	for(PresenceImpl presence : presenceImplList) {
                		result.add(new PresenceHelper(context, presence));
                	}
            	}
            	return result;
            }
    	}.executeAsync(callback);
    }
    
    private static List<PresenceImpl> fetchOnlineData(Context context, Realm realm) {
    	List<PresenceImpl> presenceList = new PresenceFetcher(context).fetch();
//    	for(PresenceImpl presence : presenceList) {
//    		// Add to or update database
//    		realm.copyToRealmOrUpdate(presence);
//    	}
    	return presenceList;
    }
}
