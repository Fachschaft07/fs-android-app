package com.fk07.backend.parser;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

import com.fk07.backend.data.Article;
import com.fk07.backend.parser.async.AsyncDownloader;
import com.fk07.news.NewsAdapter;

/**
 * @author Fabio
 * 
 */
public class NewsParser extends AsyncDownloader<List<Article>> {
	private static final String TAG = NewsParser.class.getSimpleName();
	private static final SimpleDateFormat DATE_PARSER = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", new Locale(
			"en"));
	private final NewsAdapter adapter;

	/**
	 * @param adapter
	 */
	public NewsParser(final Context context, final NewsAdapter adapter) {
		super(context);
		this.adapter = adapter;
	}

	@Override
	public List<Article> onParse(final InputStream in) {
		final List<Article> newsItems = new ArrayList<Article>();

		try {
			final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final Document doc = builder.parse(in);

			final NodeList nodes = doc.getElementsByTagName("item");
			for (int index = 0; index < nodes.getLength(); index++) {
				final Element element = (Element) nodes.item(index);

				final String title = getElementValue(element, "title");
				final String link = getElementValue(element, "link");
				final String description = getElementValue(element, "description");
				final String author = getElementValue(element, "author");
				final Date pubDate = DATE_PARSER.parse(getElementValue(element, "pubDate"));

				newsItems.add(new Article(title, link, description, author, pubDate));
			}
		} catch (final IOException e) {
			Log.e(TAG, "", e);
		} catch (final ParserConfigurationException e) {
			Log.e(TAG, "", e);
		} catch (final SAXException e) {
			Log.e(TAG, "", e);
		} catch (final ParseException e) {
			Log.e(TAG, "", e);
		}

		return newsItems;
	}

	@Override
	protected void onPostExecute(final List<Article> result) {
		if (result != null) {
			adapter.clear();
			adapter.addAll(result);
		}
		super.onPostExecute(result);
	}

	private String getElementValue(final Element parent, final String label) {
		return parent.getElementsByTagName(label).item(0).getTextContent();
	}
}
