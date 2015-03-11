package edu.hm.cs.fs.app.ui.mensa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.Meal;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MensaAdapter extends ArrayAdapter<Meal> implements StickyListHeadersAdapter {
	public MensaAdapter(final Context context) {
		super(context, android.R.layout.simple_list_item_1);
	}

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final View view = super.getView(position, convertView, parent);

		final Meal entry = getItem(position);

		final TextView textView1 = (TextView) view.findViewById(android.R.id.text1);
		textView1.setText(entry.getDescription());

		return view;
	}

    @Override
    public View getHeaderView(final int position, View convertView, final ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.section_header, viewGroup, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        //holder.text.setText(getItem(position).getDate());
        return convertView;
    }

    @Override
    public long getHeaderId(final int position) {
        //return getItem(position).getDate();
        return position;
    }

    static class HeaderViewHolder {
        @InjectView(android.R.id.text1)
        TextView text;

        public HeaderViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolder {
        @InjectView(android.R.id.text1)
        TextView text;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
