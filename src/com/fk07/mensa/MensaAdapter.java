package com.fk07.mensa;

import com.fk07.backend.data.Meal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Fabio
 * 
 */
public class MensaAdapter extends ArrayAdapter<Meal> {
	/**
	 * @param context
	 */
	public MensaAdapter(final Context context) {
		super(context, android.R.layout.simple_list_item_1);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);

		final Meal entry = getItem(position);

		final TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
		textView1.setText(entry.getDescription());

		return view;
	}
}
