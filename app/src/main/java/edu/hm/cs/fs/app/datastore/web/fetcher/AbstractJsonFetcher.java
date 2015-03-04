package edu.hm.cs.fs.app.datastore.web.fetcher;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Fabio on 18.02.2015.
 */
public abstract class AbstractJsonFetcher<Builder extends AbstractJsonFetcher<Builder, T>, T> extends AbstractContentFetcher<Builder, T> {
	protected AbstractJsonFetcher(final Context context, final String url) {
		super(context, url);
	}
	
	@Override
	protected List<T> read(String url) {
		List<T> result = new ArrayList<>();
		BufferedReader reader = null;
		try {
			final URL source = new URL(url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) source.openConnection();
			conn.setReadTimeout(3000);
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			
			reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			String rawJson = builder.toString().trim();
			if(rawJson.startsWith("[")) {
				result.addAll(convert(new JSONArray(rawJson)));
			} else {
				result.addAll(convert(new JSONObject(rawJson)));
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(reader != null) {
					reader.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	protected List<T> convert(JSONArray data) {
		return new ArrayList<T>();
	}
	
	protected List<T> convert(JSONObject data) {
		return new ArrayList<T>();
	}
}
