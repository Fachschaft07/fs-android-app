package com.fk07.backend.parser.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fk07.backend.utils.FileUtils;
import com.fk07.backend.utils.NetworkUtils;

/**
 * @author Fabio
 * 
 * @param <R>
 *            the result type.
 */
public abstract class AsyncDownloader<R> extends AsyncTask<String, Integer, R> {
	private static final String TAG = "AsyncDownloader";
	private final Context context;

	public AsyncDownloader(final Context context) {
		this.context = context;
	}

	@Override
	protected final R doInBackground(final String... params) {
		if (params.length == 0) {
			return null;
		}

		try {
			InputStream inputStream = null;
			if (NetworkUtils.isConnected(context)) {
				final URL url = new URL(params[0]);
				if (params.length == 2) {
					writeUrlContentToFile(url, params[1]);
				} else {
					inputStream = url.openStream();
				}
			}
			if (inputStream == null) {
				// Vielleicht wurde bereits der Inhalt geparst?!
				inputStream = FileUtils.openFileStream(params[1]);
			}
			return onParse(inputStream);
		} catch (final MalformedURLException e) {
			Log.e(TAG, "", e);
		} catch (final IOException e) {
			Log.w(TAG, "Datei existiert nicht. Wurde also noch nicht heruntergeladen und gespeichert.", e);
		}
		return null;
	}

	protected abstract R onParse(InputStream openFileStream);

	private void writeUrlContentToFile(final URL url, final String fileName) {
		BufferedReader reader = null;
		try {
			final URLConnection con = url.openConnection();
			final InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
			reader = new BufferedReader(inputStreamReader);

			final StringBuilder stringBuilder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}

			FileUtils.writeToFile(fileName, stringBuilder.toString());
		} catch (final IOException e) {
			Log.e(TAG, "", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (final IOException e) {
				Log.e(TAG, "", e);
			}
		}
	}
}
