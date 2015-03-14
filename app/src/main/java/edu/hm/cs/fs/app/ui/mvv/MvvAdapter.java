package edu.hm.cs.fs.app.ui.mvv;

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

import java.util.concurrent.TimeUnit;

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

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(2)
                .fontSize(29)
                .textColor(Color.DKGRAY)
                .endConfig()
                .buildRound(Integer.toString(entry.getLine()), Color.TRANSPARENT);

        holder.line.setImageDrawable(drawable);
        holder.destination.setText(entry.getDestination());
        holder.time.setText(getContext().getString(R.string.departure, entry.getDepartureIn(TimeUnit.MINUTES)));

        return convertView;
    }
	
	static class ViewHolder {
		@InjectView(R.id.imageLine) ImageView line;
		@InjectView(R.id.textDestination) TextView destination;
		@InjectView(R.id.textTime) TextView time;
		
		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}
}