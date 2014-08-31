package com.fk07.mvv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fk07.R;
import com.fk07.backend.data.PublicTransport;

/**
 * @author Fabio
 * 
 */
public class MvvAdapter extends ArrayAdapter<PublicTransport> {
	/**
	 * @param context
	 */
	public MvvAdapter(final Context context) {
		super(context, R.layout.mvv_contentline);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.mvv_contentline, parent, false);

		final PublicTransport entry = getItem(position);

		final TextView line = (TextView) view.findViewById(R.id.textLine);
		line.setText(Integer.toString(entry.getLineNumber()));
		final TextView destination = (TextView) view.findViewById(R.id.textDestination);
		destination.setText(entry.getDestination());
		final TextView time = (TextView) view.findViewById(R.id.textTime);
		time.setText(Integer.toString(entry.getDepartureTime()) + " " + getContext().getString(R.string.minute_short));

		return view;
	}
}