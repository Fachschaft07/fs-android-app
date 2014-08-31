package com.fk07.bb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fk07.Fachschaft07;
import com.fk07.R;
import com.fk07.backend.utils.NetworkUtils;
import com.fk07.info.InfoActivity;

public class BBActivity extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (NetworkUtils.isConnected(getApplicationContext())) {
			setContentView(R.layout.activity_blackboard);

			final WebView wv = (WebView) findViewById(R.id.webViewBB);
			wv.loadUrl("http://fs.cs.hm.edu/infoscreen-web/index.jsf");
			wv.getSettings().setJavaScriptEnabled(true);

			wv.setWebChromeClient(new WebChromeClient());
			wv.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
					view.loadUrl(url);
					return true;
				}
			});
		} else {
			setContentView(R.layout.no_network);
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
				Intent intent = new Intent(this, Fachschaft07.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;

			case R.id.menu_info:
				intent = new Intent(this, InfoActivity.class);
				startActivity(intent);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
