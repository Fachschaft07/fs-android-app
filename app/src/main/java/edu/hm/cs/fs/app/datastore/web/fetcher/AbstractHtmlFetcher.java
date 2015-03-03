package edu.hm.cs.fs.app.datastore.web.fetcher;

import android.content.Context;

import java.io.File;

/**
 * Created by Fabio on 18.02.2015.
 */
public abstract class AbstractHtmlFetcher<Builder extends AbstractHtmlFetcher<Builder, T>, T> extends AbstractContentFetcher<Builder, T> {
	protected AbstractHtmlFetcher(final Context context, File offlineFile, final String url) {
		super(context, offlineFile, url);
	}
}
