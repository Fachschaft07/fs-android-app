package com.fk07.news;

import com.fk07.backend.data.Article;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * @author Fabio
 * 
 */
public class NewsAdapter extends ArrayAdapter<Article> {
	/**
	 * @param context
	 */
	public NewsAdapter(final Context context) {
		super(context, android.R.layout.simple_list_item_1);
	}

	/**
	 * 
	 */
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);
		final TextView textView = (TextView) view.findViewById(android.R.id.text1);
		textView.setText(getItem(position).getTitle());
		return view;
	}
}
