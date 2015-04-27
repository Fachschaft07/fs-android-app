package edu.hm.cs.fs.app.ui.roomsearch;

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
import edu.hm.cs.fs.app.datastore.model.Room;

/**
 * Created by Fabio on 27.04.2015.
 */
public class RoomSearchAdapter extends ArrayAdapter<Room> {
    public RoomSearchAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_room, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Room room = getItem(position);

        holder.image.setImageDrawable(TextDrawable.builder()
                .buildRound("", getContext().getResources().getColor(R.color.presence_available)));
        holder.name.setText(room.getName());

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.imageView)
        ImageView image;
        @InjectView(R.id.text)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
