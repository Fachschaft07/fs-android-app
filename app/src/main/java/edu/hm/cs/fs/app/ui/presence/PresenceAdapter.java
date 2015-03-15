package edu.hm.cs.fs.app.ui.presence;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.Presence;

public class PresenceAdapter extends ArrayAdapter<Presence> {
	public PresenceAdapter(final Context context) {
		super(context, android.R.layout.simple_list_item_1);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_presence, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Presence presence = getItem(position);

        holder.status.setImageDrawable(TextDrawable.builder()
                .buildRound(" ", presence.isBusy() ? R.color.presence_busy : R.color.presence_available));
		holder.name.setText(presence.getName());

		return convertView;
	}

	static class ViewHolder {
        @InjectView(R.id.presenceStatus) ImageView status;
		@InjectView(R.id.presenceName) TextView name;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}
}
