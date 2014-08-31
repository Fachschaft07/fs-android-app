package com.fk07.mvv;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.fk07.R;
import com.fk07.backend.parser.MvvParser;
import com.fk07.backend.utils.Files;
import com.fk07.backend.utils.NetworkUtils;
import com.fk07.backend.utils.Urls;

public class MvvActivity extends Activity {
	private MvvAdapter lothAdapter;
	private MvvAdapter pasiAdapter;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mvv);

		final ListView lvLoth = (ListView) findViewById(R.id.listViewLoth);
		lvLoth.addHeaderView(getHeaderView(R.string.lothstraße));
		lothAdapter = new MvvAdapter(this);
		lvLoth.setAdapter(lothAdapter);

		final ListView lvPasi = (ListView) findViewById(R.id.listViewPasing);
		lvPasi.addHeaderView(getHeaderView(R.string.pasing));
		pasiAdapter = new MvvAdapter(this);
		lvPasi.setAdapter(pasiAdapter);

		if (NetworkUtils.isConnected(this)) {
			onRefresh();
		} else {
			setContentView(R.layout.no_network);
		}
	}

	private TextView getHeaderView(final int stringId) {
		final TextView tv = new TextView(this, null, android.R.style.TextAppearance_Medium);
		tv.setBackgroundResource(R.drawable.section_header_bg);
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(0, 10, 0, 10);
		tv.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tv.setText(stringId);
		return tv;
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		if (NetworkUtils.isConnected(this)) {
			getMenuInflater().inflate(R.menu.actionmenu_mvv, menu);
		} else {
			getMenuInflater().inflate(R.menu.actionmenu, menu);
		}

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
			case R.id.menu_refresh:
				onRefresh();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void onRefresh() {
		new MvvParser(this, lothAdapter).execute(Urls.MVV_LOTHSTR, Files.MVV_LOTHSTR);
		new MvvParser(this, pasiAdapter).execute(Urls.MVV_PASING, Files.MVV_PASING);
	}
}
