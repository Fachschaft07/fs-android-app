package com.fk07.mensa;

import java.util.Calendar;
import java.util.Date;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fk07.R;
import com.fk07.backend.data.Meal;
import com.fk07.backend.parser.MensaParser;
import com.fk07.backend.utils.Files;
import com.fk07.backend.utils.Urls;
import com.fk07.util.adapter.Sectionizer;
import com.fk07.util.adapter.SimpleSectionAdapter;

/**
 * @author Fabio
 * 
 */
public class MensaActivity extends ListActivity {
	private MensaAdapter adapter;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new MensaAdapter(this);

		final Sectionizer<Meal, Date> sectionizer = new Sectionizer<Meal, Date>() {
			public Date getSectionTitleForItem(final Meal instance) {
				final Calendar cal = Calendar.getInstance();
				cal.setTime(instance.getDate());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				return cal.getTime();
			}

			public Date getSortForItem(final Meal instance) {
				return instance.getDate();
			}
		};

		setListAdapter(new SimpleSectionAdapter<Meal, Date>(this, adapter, R.layout.section_header, android.R.id.text1,
				sectionizer, false));

		onRefresh();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.actionmenu_food, menu);
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
			case R.id.menu_legend:
				final InfoDialog infoDialog = new InfoDialog(this);
				infoDialog.show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void onRefresh() {
		new MensaParser(this, adapter).execute(Urls.MENSA, Files.MENSA);
	}
}
