package edu.hm.cs.fs.app.datastore.web.fetcher;

import android.content.Context;
import android.util.Log;

import org.apache.http.ParseException;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import edu.hm.cs.fs.app.datastore.web.utils.DataUtils;

/**
 * Created by Fabio on 18.02.2015.
 */
public abstract class AbstractXmlFetcher<Builder extends AbstractXmlFetcher<Builder, T>, T> extends AbstractContentFetcher<Builder, T> {
	private static final String TAG = AbstractXmlFetcher.class.getSimpleName();
	private final XPath mXPath = XPathFactory.newInstance().newXPath();
	private final String mRootNode;
	private Document mXmlDoc;

	protected AbstractXmlFetcher(final Context context, File offlineFile, final String url, String rootNode) {
		super(context, offlineFile, url);
		mRootNode = rootNode;
	}

	@Override
	protected JSONArray read(final String url) {
		mXmlDoc = DataUtils.read(url);

		if (mXmlDoc == null) {
			// Unable to parse xml --> exit
			throw new ParseException("Unable to parse url");
		}

		// Convert everything into json format
		final JSONArray jsonArray = new JSONArray();

		try {
			// 2014-09-18: BugFix: Wrong count with
			// mXmlDoc.getElementByTagName(...)
			// 2014-09-19: Put the getCountByXPah method outside the for-loop to
			// increase speed
			final int countElements = getCountByXPath(mRootNode);
			for (int index = 1; index <= countElements; index++) {
				final String path = mRootNode + "[" + index + "]";
				final T item = onCreateItem(path);
				jsonArray.put(mGson.toJson(item));
			}
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
		return jsonArray;
	}

	/**
	 * Create the next item at the specified index.
	 *
	 * @param rootPath
	 *            of the item.
	 * @return the item.
	 * @throws Exception
	 */
	protected abstract T onCreateItem(String rootPath) throws Exception;

	/**
	 * Find a element by using the {@link javax.xml.xpath.XPath}.
	 *
	 * @param xPath
	 *            for searching.
	 * @param name
	 *            of the type.
	 * @return the found value.
	 * @throws javax.xml.xpath.XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	protected <X> X findByXPath(final String xPath, final QName name)
			throws XPathExpressionException {
		return (X) mXPath.evaluate(xPath, mXmlDoc, name);
	}

	/**
	 * Get the count of elements of a name.
	 *
	 * @param xPath
	 *            to the element.
	 * @return the count.
	 * @throws javax.xml.xpath.XPathExpressionException
	 */
	protected int getCountByXPath(final String xPath)
			throws XPathExpressionException {
		return ((NodeList) findByXPath(xPath, XPathConstants.NODESET))
				.getLength();
	}
}
