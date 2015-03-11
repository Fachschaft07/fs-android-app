package edu.hm.cs.fs.app.ui.mvv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.PublicTransport;


/**
 * @author Fabio
 * 
 */
public class MvvAdapter extends ArrayAdapter<PublicTransport> {
	public MvvAdapter(final Context context) {
		super(context, android.R.layout.simple_list_item_1);
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_mvv, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PublicTransport entry = getItem(position);

		holder.line.setText(Integer.toString(entry.getLine()));
		holder.destination.setText(entry.getDestination());
		
		long timeDifferenceInMs = entry.getDeparture().getTime() - System.currentTimeMillis();
		long timeDifferenceInMin = timeDifferenceInMs / 1000 / 60;
		
		holder.time.setText(timeDifferenceInMin + " " + getContext().getString(R.string.minute_short));

		return convertView;
	}
	
	static class ViewHolder {
		@InjectView(R.id.textLine) TextView line;
		@InjectView(R.id.textDestination) TextView destination;
		@InjectView(R.id.textTime) TextView time;
		
		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}
}