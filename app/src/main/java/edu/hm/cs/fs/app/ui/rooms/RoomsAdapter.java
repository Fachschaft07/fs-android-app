package edu.hm.cs.fs.app.ui.rooms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fk07.R;
import com.fk07.backend.data.Room;

/**
 * @author Fabio
 * 
 */
public class RoomsAdapter extends ArrayAdapter<Room> {
	/**
	 * @param context
	 */
	public RoomsAdapter(final Context context) {
		super(context, R.layout.rooms_contentline);
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.rooms_contentline, parent, false);

		final Room room = getItem(position);

		final TextView textViewType = (TextView) view.findViewById(R.id.textType);
		textViewType.setText(getContext().getString(room.getType().getNameId()));

		final TextView textViewRoom = (TextView) view.findViewById(R.id.textRoom);
		textViewRoom.setText(room.getName());

		final TextView textViewFreeTimeUntil = (TextView) view.findViewById(R.id.textUntil);
		textViewFreeTimeUntil.setText(room.getFreeTime());

		return view;
	}
}