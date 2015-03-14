package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.impl.PresenceImpl;
import edu.hm.cs.fs.app.datastore.web.fetcher.AbstractJsonFetcher;

/**
 * Created by Fabio on 18.02.2015.
 */
public class PresenceFetcher extends
		AbstractJsonFetcher<PresenceFetcher, PresenceImpl> {
	private final static String URL = "http://fs.cs.hm.edu/presence/?app=true";

	public PresenceFetcher(Context context) {
		super(context, URL);
	}
	
	@Override
	protected List<PresenceImpl> convert(JSONObject data) {
		List<PresenceImpl> result = new ArrayList<>();
        try {
            JSONArray jsonArray = data.getJSONArray("persons");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject person = jsonArray.getJSONObject(i);

                PresenceImpl presenceImpl = new PresenceImpl();
                presenceImpl.setName(person.getString("nickName"));
                presenceImpl.setStatus(person.getString("status"));

                result.add(presenceImpl);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
		return result;
	}
}