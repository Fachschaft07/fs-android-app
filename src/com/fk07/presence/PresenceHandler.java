package com.fk07.presence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PresenceHandler {
	
	private final static String TAG = "PresenceFactory";

	private final static String url = "http://fs.cs.hm.edu/presence/?app=true";

	public Map<String, String> downloadPresence() {
		
		Map<String, String> presenceMap = new HashMap<String, String>();
		String presenceResult = "";
		try {
			final URL source = new URL(url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) source.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				builder.append(line);
			}
			presenceResult = builder.toString();
		} catch (final Exception e) {
			Log.e(TAG, e.getMessage());
		}
		try {
			JSONObject jsonObject = new JSONObject(presenceResult);
			JSONArray jsonArray = (JSONArray) jsonObject.get("persons");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject person = jsonArray.getJSONObject(i);
				presenceMap.put(person.getString("nickName"), person.getString("status"));
			}
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		return presenceMap;
	}
}
