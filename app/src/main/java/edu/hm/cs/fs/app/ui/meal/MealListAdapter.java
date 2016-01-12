package edu.hm.cs.fs.app.ui.meal;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.common.model.Meal;

/**
 * Created by Fabio on 12.07.2015.
 */
public class MealListAdapter extends ArrayAdapter<Meal> implements StickyListHeadersAdapter {

    private static final String DATE_FORMAT = "%1$tA, %1$td.%1$tm.%1$tY";

    private OnMealClickListener mListener;

    public MealListAdapter(final Context context) {
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
        holder.meal = meal;
        holder.listener = mListener;

        int color;
        switch (meal.getType()) {
            case VEGAN:
                color = getContext().getResources().getColor(R.color.meal_vegan);
                break;
            case MEATLESS:
                color = getContext().getResources().getColor(R.color.meal_meatless);
                break;
            default:
                color = getContext().getResources().getColor(R.color.meal_meat);
                break;
        }
        TextDrawable drawable = TextDrawable.builder().beginConfig().withBorder(2).fontSize(getContext().getResources().getDimensionPixelSize(R.dimen.text_size_meal_type)).useFont(Typeface.DEFAULT_BOLD).endConfig().buildRound(meal.getType().toString(), color);
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

    public OnMealClickListener getOnMealClickListener() {
        return mListener;
    }

    public void setOnMealClickListener(OnMealClickListener listener) {
        mListener = listener;
    }

    public interface OnMealClickListener {

        void onMealClick(@NonNull final Meal meal);
    }

    static class HeaderViewHolder {

        @Bind(android.R.id.text1)
        TextView text;

        public HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {

        @Bind(R.id.imageView)
        ImageView image;

        @Bind(R.id.textView)
        TextView text;

        Meal meal;

        OnMealClickListener listener;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.imageView, R.id.textView})
        public void onClick() {
            if (listener != null) {
                listener.onMealClick(meal);
            }
        }
    }
}
