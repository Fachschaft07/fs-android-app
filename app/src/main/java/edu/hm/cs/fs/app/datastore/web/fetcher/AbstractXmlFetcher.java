package edu.hm.cs.fs.app.datastore.web.fetcher;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Fabio on 18.02.2015.
 */
public abstract class AbstractXmlFetcher<Builder extends AbstractXmlFetcher<Builder, T>, T> extends AbstractContentFetcher<Builder, T> {
	private static final String TAG = AbstractXmlFetcher.class.getSimpleName();
	private final XPath mXPath = XPathFactory.newInstance().newXPath();
	private final String mRootNode;
	private Document mXmlDoc;

	protected AbstractXmlFetcher(final Context context, final String url, String rootNode) {
		super(context, url);
		mRootNode = rootNode;
	}

	@Override
	protected List<T> read(final String url) {
		List<T> result = new ArrayList<>();

        final DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = factory.newDocumentBuilder();
            mXmlDoc = documentBuilder.parse(url);
        } catch (final Exception e) {
            Log.w(getClass().getSimpleName(), "", e);
        }

        if(mXmlDoc != null) {
            try {
                // 2014-09-18: BugFix: Wrong count with
                // mXmlDoc.getElementByTagName(...)
                // 2014-09-19: Put the getCountByXPah method outside the for-loop to
                // increase speed
                final int countElements = getCountByXPath(mRootNode);
                for (int index = 1; index <= countElements; index++) {
                    final String path = mRootNode + "[" + index + "]";
                    T value = onCreateItem(path);
                    if(value != null) {
                        if(value instanceof List) {
                            result.addAll((List<T>) value);
                        } else {
                            result.add(value);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
		return result;
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
