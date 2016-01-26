package edu.hm.cs.fs.app.ui.roomsearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.common.constant.RoomType;
import edu.hm.cs.fs.common.model.simple.SimpleRoom;

/**
 * @author Fabio
 */
public class RoomSearchListAdapter extends RecyclerView.Adapter<RoomSearchListAdapter.ViewHolder> {

    private final List<SimpleRoom> mData = new ArrayList<>();

    private Context mContext;

    public RoomSearchListAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<SimpleRoom> data) {
        mData.clear();
        Collections.sort(data, new Comparator<SimpleRoom>() {
            @Override
            public int compare(SimpleRoom lhs, SimpleRoom rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        notifyItemRangeRemoved(0, mData.size());
        mData.clear();
    }

    public void add(SimpleRoom item) {
        mData.add(item);
        notifyItemInserted(mData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_room_search, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final SimpleRoom room = mData.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, room.getHour());
        calendar.set(Calendar.MINUTE, room.getMinute());

        viewHolder.mRoom.setText(room.getName());
        StringBuilder freeUntilText = new StringBuilder();
        if (RoomType.AUDITORIUM == room.getRoomType()) {
            freeUntilText.append(mContext.getString(R.string.auditorium));
        } else if (RoomType.LABORATORY == room.getRoomType()) {
            freeUntilText.append(mContext.getString(R.string.laboratory));
        } else if (RoomType.LOUNGE == room.getRoomType()) {
            freeUntilText.append(mContext.getString(R.string.lounge));
        }
        freeUntilText.append(" ");
        freeUntilText.append(mContext.getString(R.string.free_until,
                String.format("%1$tH:%1$tM", calendar)));
        viewHolder.mFreeUntil.setText(freeUntilText.toString());
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
