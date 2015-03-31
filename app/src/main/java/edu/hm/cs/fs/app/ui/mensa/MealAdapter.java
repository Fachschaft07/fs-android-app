package edu.hm.cs.fs.app.ui.mensa;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.model.Meal;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MealAdapter extends ArrayAdapter<Meal> implements StickyListHeadersAdapter {
    private static final String DATE_FORMAT = "%1$tA, %1$td.%1$tm.%1$tY";

	public MealAdapter(final Context context) {
		super(context, android.R.layout.simple_list_item_1);
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_meal, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

		final Meal meal = getItem(position);

        int color;
        switch (meal.getType()) {
            case VEGAN:
                color = Color.rgb(30, 200, 30);
                break;
            case MEATLESS:
                color = Color.rgb(240, 200, 20);
                break;
            default:
                color = Color.rgb(200, 10, 75);
                break;
        }
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(2)
                .fontSize(25)
                .endConfig()
                .buildRound(meal.getType().toString(), color);
        holder.image.setImageDrawable(drawable);

        holder.text.setText(meal.getName());

		return convertView;
	}

    @Override
    public View getHeaderView(final int position, View convertView, final ViewGroup viewGroup) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_sticky_header, viewGroup, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        holder.text.setText(String.format(Locale.getDefault(), DATE_FORMAT, getItem(position).getDate()));
        return convertView;
    }

    @Override
    public long getHeaderId(final int position) {
        return String.format(Locale.getDefault(), DATE_FORMAT, getItem(position).getDate()).hashCode();
    }

    static class HeaderViewHolder {
        @InjectView(android.R.id.text1)
        TextView text;

        public HeaderViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolder {
        @InjectView(R.id.imageView)
        ImageView image;
        @InjectView(R.id.textView)
        TextView text;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
