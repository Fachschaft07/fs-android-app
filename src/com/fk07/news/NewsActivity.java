package com.fk07.news;

import java.util.Calendar;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.fk07.R;
import com.fk07.backend.data.Article;
import com.fk07.backend.parser.NewsParser;
import com.fk07.backend.utils.Files;
import com.fk07.backend.utils.Urls;
import com.fk07.util.adapter.Sectionizer;
import com.fk07.util.adapter.SimpleSectionAdapter;

/**
 * Activity für News.
 * 
 * @author Fabio
 * @version 2
 */
public class NewsActivity extends ListActivity {
	private NewsAdapter adapter;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new NewsAdapter(this);
		final Sectionizer<Article, Date> sectionizer = new Sectionizer<Article, Date>() {
			public Date getSectionTitleForItem(final Article instance) {
				final Calendar cal = Calendar.getInstance();
				cal.setTime(instance.getReleaseDate());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				return cal.getTime();
			}

			public Date getSortForItem(final Article instance) {
				return instance.getReleaseDate();
			}
		};

		setListAdapter(new SimpleSectionAdapter<Article, Date>(this, adapter, R.layout.section_header,
				android.R.id.text1, sectionizer, true));

		updateNews();
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.actionmenu_with_refresh, menu);
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
				updateNews();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(final ListView listView, final View view, final int position, final long id) {
		final Intent intent = new Intent(this, NewsDetailActivity.class);
		final Article newsEntry = (Article) getListAdapter().getItem(position);
		intent.putExtra("title", newsEntry.getTitle());
		intent.putExtra("link", newsEntry.getLink());
		intent.putExtra("description", newsEntry.getDescription());
		intent.putExtra("author", newsEntry.getAuthor());
		startActivity(intent);
	}

	private void updateNews() {
		new NewsParser(this, adapter).execute(Urls.NEWS, Files.NEWS);
	}
}
