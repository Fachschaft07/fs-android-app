package edu.hm.cs.fs.app.datastore.web.fetcher;

import android.content.Context;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fabio on 18.02.2015.
 */
public abstract class AbstractHtmlFetcher<Builder extends AbstractHtmlFetcher<Builder, T>, T> extends AbstractContentFetcher<Builder, T> {
	protected AbstractHtmlFetcher(final Context context, final String url) {
		super(context, url);
	}

    @Override
    protected List<T> read(final String url) {
        List<T> result = new ArrayList<>();
        try {
            final Document document = Jsoup.parse(
                    new URL(url), // URL to parse
                    (int) TimeUnit.MILLISECONDS.convert(10, TimeUnit.SECONDS) // Timeout
            );
            result.addAll(readFromDoc(document));
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
        return result;
    }

    protected abstract List<T> readFromDoc(final Document document);
}
