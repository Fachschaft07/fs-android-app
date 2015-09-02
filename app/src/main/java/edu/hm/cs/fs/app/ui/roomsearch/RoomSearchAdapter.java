package edu.hm.cs.fs.app.ui.roomsearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.common.model.Room;

/**
 * Created by FHellman on 10.08.2015.
 */
public class RoomSearchAdapter extends RecyclerView.Adapter<RoomSearchAdapter.ViewHolder> {

    private final List<Room> mData = new ArrayList<>();

    private Context mContext;

    public RoomSearchAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Room> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_room_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Room room = mData.get(position);

        viewHolder.mRoom.setText(room.getName());
        viewHolder.mFreeUntil.setText(mContext.getString(R.string.free_until, String.format("%1$tH:%1$tM", room.getFreeUntilEnd().getEnd())));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textRoom)
        TextView mRoom;

        @Bind(R.id.textFreeUntil)
        TextView mFreeUntil;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
