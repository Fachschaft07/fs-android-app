package edu.hm.cs.fs.app.ui.news;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Eine Anzeige der ausgewählten Neuigkeit. Damit diese Activity nicht zu komplex wird, wird auf eine WebView
 * zurückgegriffen, die den bereits vorhandenen Inhalt darstellt. Dazu muss keine neue Internetverbindung hergestellt
 * werden! (Ausnahme ist, wenn Bilder geladen werden müssen)
 * 
 * @author Fabio
 * @version 1
 */
public class NewsDetailActivity extends Activity {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String title = getIntent().getStringExtra("title");
		String description = getIntent().getStringExtra("description");
		description = description.replace("<![CDATA[", "<html><body>");
		description = description.replace("]]>", "</body></html>");

		setTitle(title);

		final WebView descriptionWebView = new WebView(this);
		descriptionWebView.loadData(description, "text/html", "ISO-8859-15");

		setContentView(descriptionWebView);
	}

	@Override
	public void setTitle(final CharSequence title) {
		super.setTitle(title);
		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 11) {
			getActionBar().setTitle(title);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
