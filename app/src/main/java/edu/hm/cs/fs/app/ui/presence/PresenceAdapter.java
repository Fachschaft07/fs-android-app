package edu.hm.cs.fs.app.ui.presence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fk07.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.helper.Callback;
import edu.hm.cs.fs.app.datastore.web.PresenceFetcher;

public class PresenceAdapter extends ArrayAdapter<PresenceFetcher> {
	public PresenceAdapter(final Context context) {
		super(context, android.R.layout.simple_list_item_1);
		DataSource.getInstance(context).getPresence(new Callback<PresenceFetcher>() {
			@Override
			public void onResult(final List<PresenceFetcher> result) {
				addAll(result);
			}
		});
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.presence_row, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PresenceFetcher person = getItem(position);

		holder.name.setText(person.getName());

		holder.status.setImageResource(
				person.isBusy() ?
				R.drawable.circle_yellow_light :
				R.drawable.circle_green_dark
		);

		return convertView;
	}

	static class ViewHolder {
		@InjectView(R.id.presenceName) TextView name;
		@InjectView(R.id.presenceStatus) ImageView status;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}
}
