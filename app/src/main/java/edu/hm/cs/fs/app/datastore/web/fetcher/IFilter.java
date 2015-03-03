package edu.hm.cs.fs.app.datastore.web.fetcher;

/**
 * @author Fabio
 *
 * @param <T>
 */
public interface IFilter<T> {
	/**
	 * Only accept data which is not filtered.
	 *
	 * @param data
	 *            to filter.
	 * @return <code>true</code> if the data is accepted.
	 */
	boolean apply(T data);
}
