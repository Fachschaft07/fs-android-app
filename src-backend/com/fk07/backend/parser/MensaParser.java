package com.fk07.backend.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.fk07.backend.data.Meal;
import com.fk07.backend.parser.async.AsyncDownloader;
import com.fk07.mensa.MensaAdapter;

/**
 * @author Fabio
 * 
 */
public class MensaParser extends AsyncDownloader<List<Meal>> {
	private static final String TAG = MensaParser.class.getSimpleName();
	private static final Pattern PATTERN_MEAL = Pattern.compile(".*<span style=\"float:left\">(.*)</span>.*");
	@SuppressLint("SimpleDateFormat")
	private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final MensaAdapter adapter;

	/**
	 * @param context
	 * @param adapter
	 */
	public MensaParser(final Context context, final MensaAdapter adapter) {
		super(context);
		this.adapter = adapter;
	}

	@Override
	protected List<Meal> onParse(final InputStream openFileStream) {
		final List<Meal> mealList = new ArrayList<Meal>();

		try {
			final BufferedReader br = new BufferedReader(new InputStreamReader(openFileStream));

			final Calendar cal = Calendar.getInstance();
			boolean menuFound = false;
			final List<String> parsedMensaMeals = new ArrayList<String>();

			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();

				// Samstag und Sonntag sind nicht relevant
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DAY_OF_MONTH, 1);
				} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
					cal.add(Calendar.DAY_OF_MONTH, 2);
				}

				final Date date = cal.getTime();

				if (line.contains("heute_" + sdf.format(date))) {
					menuFound = true;
				} else if (menuFound) {
					final Matcher matcherDescription = PATTERN_MEAL.matcher(line);

					if (matcherDescription.find()) {
						parsedMensaMeals.add(matcherDescription.group(1));
					} else if (line.matches(".*</table>.*")) {
						menuFound = false;

						// Gerichte hinzufügen
						for (final String mealDescription : parsedMensaMeals) {
							mealList.add(new Meal(cal.getTime(), mealDescription));

							// Dieses Hinzufügen einer Millisekunde ist essentiell für den SectionizerAdapter!!!
							cal.add(Calendar.MILLISECOND, 1);
						}
						parsedMensaMeals.clear();

						// nächster Tag
						cal.add(Calendar.DAY_OF_MONTH, 1);
					}
				}
			}
		} catch (final IOException e) {
			Log.e(TAG, "", e);
		}
		return mealList;
	}

	@Override
	protected void onPostExecute(final List<Meal> result) {
		if (result != null) {
			adapter.clear();
			adapter.addAll(result);
		}
		super.onPostExecute(result);
	}
}
