package com.fk07.backend.web.data.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Document;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;

import com.fk07.backend.web.data.constants.Letter;
import com.fk07.backend.web.data.constants.Semester;
import com.fk07.backend.web.data.constants.Study;
import com.fk07.backend.web.data.constants.StudyGroup;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;

/**
 * @author Fabio
 *
 */
public final class DataUtils {
	private static final Pattern PATTERN_LIST_ITEM = Pattern
			.compile("\\s\\.([^\n]+)");
	private static final String LIST_OPENING_TAG = "<ul>";
	private static final String LIST_CLOSEING_TAG = "</ul>";
	private static final String LIST_ITEM_OPENING_TAG = "<li>";
	private static final String LIST_ITEM_CLOSEING_TAG = "</li>";

	private DataUtils() {
	}

	public static String toHtml(final String content) {
		final String replaced = content.replaceAll("#", "\n");
		final String bold = replaceAll(replaced, "*", "b");
		return insertList(bold);
	}

	private static String insertList(final String string) {
		String result = string;
		final String startString = " .";
		final String endString = " .\n";

		int index = 0;
		while (index > -1) {
			final Matcher matcher = PATTERN_LIST_ITEM.matcher(result);
			if (matcher.find(index)) {
				index = matcher.start();
			} else {
				break;
			}
			String firstPart = result.substring(0, index);
			String secondPart = result.substring(index + startString.length(),
					result.length());
			result = firstPart + LIST_OPENING_TAG + "\n." + secondPart;

			index = result.indexOf(endString, index);
			firstPart = result.substring(0, index);
			secondPart = result.substring(index + endString.length(),
					result.length());
			result = firstPart + "\n" + LIST_CLOSEING_TAG + secondPart;
		}

		// System.out.println(result);
		return insertListItems(result);
	}

	private static String insertListItems(final String string) {
		String result = string;
		int index = 0;
		while (index > -1) {
			final Matcher matcher = PATTERN_LIST_ITEM.matcher(result);
			if (matcher.find(index)) {
				final String group = matcher.group(1);
				index = matcher.start();

				final String firstPart = result.substring(0, index);
				final String secondPart = result.substring(index
						+ matcher.group().length(), result.length());

				group.replaceAll("\\.", "");
				result = firstPart + LIST_ITEM_OPENING_TAG + group
						+ LIST_ITEM_CLOSEING_TAG + secondPart;
			} else {
				index = -1;
			}
		}
		// System.out.println(result);
		return result;
	}

	private static String replaceAll(final String string, final String search,
			final String replacement) {
		String content = string;
		int index;
		boolean open = true;
		while ((index = findNext(content, search, 0)) > -1) {
			final String firstPart = content.substring(0, index);
			final String secondPart = content.substring(index + 1,
					content.length());
			if (open) {
				content = firstPart + "<" + replacement + ">" + secondPart;
				open = false;
			} else {
				content = firstPart + "</" + replacement + ">" + secondPart;
				open = true;
			}
		}
		return content;
	}

	private static int findNext(final String content, final String search,
			final int start) {
		final int indexOf = content.indexOf(search);
		if (content.length() > indexOf && indexOf > 0
				&& content.charAt(indexOf - 1) == '\\') {
			return findNext(content, search, indexOf);
		}
		return indexOf;
	}

	/**
	 * @param url
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document read(final String url) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = factory.newDocumentBuilder();
			return documentBuilder.parse(url);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * @param context
	 * @return
	 */
	public static boolean isOnline(final Context context) {
		final ConnectivityManager connectionManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectionManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()
				|| connectionManager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).isConnected();
	}

	/**
	 * @param content
	 * @param file
	 * @throws Exception
	 * @Deprecated use {@link #save(JSONArray, File)} instead of this method.
	 */
	@Deprecated
	public static void save(final Document content, final File file)
			throws Exception {
		// Use a Transformer for output
		final TransformerFactory tFactory = TransformerFactory.newInstance();
		final Transformer transformer = tFactory.newTransformer();

		final DOMSource source = new DOMSource(content);
		final StreamResult result = new StreamResult(new FileOutputStream(file));
		transformer.transform(source, result);
	}

	/**
	 * @param jsonArray
	 * @param offlineFile
	 * @throws IOException
	 */
	public static void save(final JSONArray jsonArray, final File offlineFile)
			throws IOException {
		Files.write(jsonArray.toString(), offlineFile, Charsets.UTF_8);
	}

	/**
	 * @param offlineFile
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONArray read(final File offlineFile) throws JSONException,
			IOException {
		return new JSONArray(Files.toString(offlineFile, Charsets.UTF_8));
	}

	/**
	 * @param text
	 * @param contains
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	public static boolean containsIgnoreCase(final String text,
			final String contains) {
		return text.toLowerCase().contains(contains.toLowerCase());
	}

	/**
	 * @param date
	 * @return
	 */
	public static boolean isSameDate(final Date date1, final Date date2) {
		final Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		final Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE);
	}

	/**
	 * @param studyGroups
	 * @param study
	 * @return
	 */
	public static List<StudyGroup> filter(final List<StudyGroup> studyGroups,
			final Study study) {
		final List<StudyGroup> result = new ArrayList<StudyGroup>();
		for (final StudyGroup studyGroup : studyGroups) {
			if (studyGroup.getStudy() == study) {
				result.add(studyGroup);
			}
		}
		return result;
	}

	/**
	 * @param studyGroups
	 * @param study
	 * @param semester
	 * @return
	 */
	public static List<StudyGroup> filter(final List<StudyGroup> studyGroups,
			final Study study, final Semester semester) {
		final List<StudyGroup> result = new ArrayList<StudyGroup>();
		final List<StudyGroup> filterList = filter(studyGroups, study);
		for (final StudyGroup studyGroup : filterList) {
			if (!studyGroup.getSemester().isPresent()) {
				result.add(studyGroup);
			} else if (studyGroup.getSemester().get() == semester) {
				result.add(studyGroup);
			}
		}
		return result;
	}

	/**
	 * @param studyGroups
	 * @param study
	 * @param semester
	 * @param letter
	 * @return
	 */
	public static List<StudyGroup> filter(final List<StudyGroup> studyGroups,
			final Study study, final Semester semester, final Letter letter) {
		final List<StudyGroup> result = new ArrayList<StudyGroup>();
		final List<StudyGroup> filterList = filter(studyGroups, study, semester);
		for (final StudyGroup studyGroup : filterList) {
			if (!studyGroup.getLetter().isPresent()) {
				result.add(studyGroup);
			} else if (studyGroup.getLetter().get() == letter) {
				result.add(studyGroup);
			}
		}
		return result;
	}

	/**
	 * @param languageCode
	 * @return
	 */
	public static Locale toLocale(final String languageCode) {
		for (final Locale locale : Locale.getAvailableLocales()) {
			if (locale.getLanguage().equalsIgnoreCase(languageCode)) {
				return locale;
			}
		}
		throw new IllegalArgumentException("Not a valid language code: "
				+ languageCode);
	}

	/**
	 * @param data
	 * @return
	 * @throws JSONException
	 */
	public static <T> JSONArray toJsonArray(final List<T> data)
			throws JSONException {
		return new JSONArray(new Gson().toJson(data));
	}
}
