package com.fk07.backend.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

import com.fk07.backend.data.PublicTransport;
import com.fk07.backend.parser.async.AsyncDownloader;
import com.fk07.mvv.MvvAdapter;

/**
 * @author Fabio
 * 
 */
public class MvvParser extends AsyncDownloader<List<PublicTransport>> {
	private static final String TAG = MvvParser.class.getSimpleName();
	private final int NR_OF_ITEMS_TO_LOAD = 6;
	private final MvvAdapter adapter;

	/**
	 * @param adapter
	 */
	public MvvParser(final Context context, final MvvAdapter adapter) {
		super(context);
		this.adapter = adapter;
	}

	@Override
	protected List<PublicTransport> onParse(final InputStream openFileStream) {
		final ArrayList<PublicTransport> content = new ArrayList<PublicTransport>();
		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(openFileStream, "ISO-8859-15"));

			String line = br.readLine();

			int index = 0;

			final Pattern linePattern = Pattern.compile("(.*)(<td class=\"lineColumn\">)(\\d{1,3})(</td>)(.*)");
			final Pattern timePattern = Pattern.compile("(.*)(<td class=\"inMinColumn\">)(\\d{1,3})(</td>)(.*)");
			Matcher m;

			String lineNr = "";
			String dest = "";
			String time = "";

			while (line != null) {

				if (index < NR_OF_ITEMS_TO_LOAD) {
					line = br.readLine();

					m = linePattern.matcher(line);
					if (m.matches()) {
						lineNr = m.group(3);
						continue;
					}

					line = line.replace('\t', ' ').trim();
					if (!line.startsWith("<") && !line.equals("")) {
						dest = line;
						continue;
					}

					m = timePattern.matcher(line);
					if (m.matches()) {
						time = m.group(3);
						final int lineNumber = Integer.parseInt(lineNr.trim());
						final int depTime = Integer.parseInt(time.trim());
						content.add(new PublicTransport(lineNumber, dest.trim(), depTime));
						index++;
						continue;
					}
				} else {
					break;
				}
			}
		} catch (final IOException e) {
			Log.e(TAG, "", e);
		} catch (final NullPointerException e) {
			Log.e(TAG, "", e);
		}

		return content;
	}

	@Override
	protected void onPostExecute(final List<PublicTransport> result) {
		if (result != null) {
			adapter.clear();
			adapter.addAll(result);
		}
		super.onPostExecute(result);
	}
}
