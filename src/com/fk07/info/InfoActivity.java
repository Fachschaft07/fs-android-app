package com.fk07.info;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

public class InfoActivity extends Activity {
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		final TextView textViewVersion = (TextView) findViewById(R.id.textViewVersion);
		try {
			final String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			textViewVersion.setText(textViewVersion.getText() + " " + versionName);
		} catch (final NameNotFoundException e) {
			Log.e(getClass().getSimpleName(), "", e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.actionmenu_info, menu);
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

	public void openMail(final View view) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "app@fs.cs.hm.edu" });
		startActivity(Intent.createChooser(intent, getString(R.string.send_mail)));
	}
}
