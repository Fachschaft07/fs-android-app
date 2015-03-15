package edu.hm.cs.fs.app.util.multipane;

/**
 * Created by Fabio on 20.12.2014.
 */
public interface OnMultiPaneListSegment<T> {
	void setDetailSegment(OnMultiPaneDetailSegment<T> detailSegment);

	int getSelectedPosition();

	T getItemAt(int position);
}
