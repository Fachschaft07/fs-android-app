package edu.hm.cs.fs.app.datastore.web;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.hm.cs.fs.app.datastore.model.impl.Presence;

/**
 * Created by Fabio on 18.02.2015.
 */
public class PresenceFetcher {
	private final String nickName;
	private final String status;

	public PresenceFetcher(final String nickName, final String status) {
		this.nickName = nickName;
		this.status = status;
	}

	public String getName() {
		return nickName;
	}

	public boolean isBusy() {
		return "Busy".equalsIgnoreCase(status);
	}

	public static class Builder {
		private final static String URL = "http://fs.cs.hm.edu/presence/?app=true";
		private final Context mContext;

		public Builder(Context context) {
			mContext = context;
		}

		public List<Presence> build() {
			List<Presence> result = new ArrayList<>();
			try {
				final URL source = new URL(URL);
				HttpURLConnection conn;
				conn = (HttpURLConnection) source.openConnection();
				conn.setReadTimeout(3000);
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					builder.append(line);
				}
				bufferedReader.close();

				JSONObject jsonObject = new JSONObject(builder.toString());
				JSONArray jsonArray = (JSONArray) jsonObject.get("persons");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject person = jsonArray.getJSONObject(i);
					result.add(new Presence(person.getString("nickName"), person.getString("status")));
				}
			} catch (final Exception e) {
				// TODO Exceptionhandling
			}
			return result;
		}
	}
}
