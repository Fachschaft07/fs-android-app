package com.fk07.backend.web.data.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.fk07.backend.web.data.utils.DataUtils;

import android.content.Context;

/**
 * @author Fabio
 *
 * @param <B>
 *            The Builder
 * @param <T>
 *            The Content type
 */
public abstract class AbstractBuilder<B extends AbstractBuilder<B, T>, T> {
	private final Context mContext;
	private final String mUrl;
	private File mOfflineFile;
	private Document xmlDoc;
	private final List<IFilter<T>> mFilterList = new ArrayList<IFilter<T>>();
	private final String mRootNode;
	private boolean mOfflineMode;

	protected AbstractBuilder(final Context context, final String url,
			final String rootNode) {
		mContext = context;
		this.mUrl = url;
		this.mRootNode = rootNode;
	}

	/**
	 * The file to store the data in when loading the content from the internet.
	 * If the internet is not available, then the data will be loaded from this
	 * file.
	 *
	 * @param file
	 *            save and load content from.
	 * @return the builder.
	 */
	@SuppressWarnings("unchecked")
	public B setOfflineStorage(final File file) {
		mOfflineFile = file;
		return (B) this;
	}

	/**
	 * Set the offline mode to <code>true</code> to work with the existing
	 * offline storage file.
	 *
	 * @param offlineMode
	 *            should set to <code>true</code> to work offline and don't
	 *            fetch with every data request the content from the internet.
	 * @return the builder.
	 */
	@SuppressWarnings("unchecked")
	public B setWorkOffline(final boolean offlineMode) {
		this.mOfflineMode = offlineMode;
		return (B) this;
	}

	/**
	 * Downloads and reads the content from the web if a connection is
	 * available. Otherwise the offline file will be read.
	 *
	 * @return a list with the content.
	 * @throws Exception
	 */
	public final List<T> build() throws Exception {
		final List<T> content = new ArrayList<T>();
		final boolean online = DataUtils.isOnline(mContext) && !mOfflineMode;

		if (!online && mOfflineFile == null) {
			// No internet connection and no offline file --> exit
			throw new IllegalArgumentException(
					"You can not work offline due to the fact that there is no offline storage file");
		}

		xmlDoc = DataUtils.read(online ? mUrl : mOfflineFile.getAbsolutePath());

		if (xmlDoc == null) {
			// Unable to parse xml --> exit
			throw new ParseException("Unable to parse url");
		}

		if (mOfflineFile != null && online) {
			// Only save the xml if the device is connected to the internet
			try {
				DataUtils.save(xmlDoc, mOfflineFile);
			} catch (final Exception e) {
				throw e;
			}
		}

		for (int index = 1; index <= xmlDoc.getElementsByTagName(mRootNode)
				.getLength(); index++) {
			final T item = onCreateItem(index);

			if (apply(item)) {
				content.add(item);
			}
		}

		return content;
	}

	/**
	 * Create the next item at the specified index position.
	 *
	 * @param index
	 *            of the item.
	 * @return the item.
	 * @throws Exception
	 */
	protected abstract T onCreateItem(int index) throws Exception;

	/**
	 * Find a element by using the {@link XPath}.
	 *
	 * @param xPath
	 *            for searching.
	 * @param name
	 *            of the type.
	 * @return the found value.
	 * @throws XPathExpressionException
	 */
	@SuppressWarnings("unchecked")
	protected <X> X findByXPath(final String xPath, final QName name)
			throws XPathExpressionException {
		final XPathFactory xPathfactory = XPathFactory.newInstance();
		final XPath xpath = xPathfactory.newXPath();
		final XPathExpression expr = xpath.compile(xPath);
		return (X) expr.evaluate(xmlDoc, name);
	}

	/**
	 * Get the count of elements of a name.
	 *
	 * @param xPath
	 *            to the element.
	 * @return the count.
	 * @throws XPathExpressionException
	 */
	protected int getCountByXPath(final String xPath)
			throws XPathExpressionException {
		final XPathFactory xPathfactory = XPathFactory.newInstance();
		final XPath xpath = xPathfactory.newXPath();
		final XPathExpression expr = xpath.compile(xPath);
		final NodeList nodeList = (NodeList) expr.evaluate(xmlDoc,
				XPathConstants.NODESET);
		return nodeList.getLength();
	}

	/**
	 * Add a filter which every created item has to pass before it will be added
	 * to the build list.
	 *
	 * @param filter
	 *            to pass.
	 */
	protected void addFilter(final IFilter<T> filter) {
		mFilterList.add(filter);
	}

	private boolean apply(final T item) {
		boolean apply = true;
		for (final IFilter<T> filter : mFilterList) {
			apply &= filter.apply(item);
		}
		return apply;
	}
}
