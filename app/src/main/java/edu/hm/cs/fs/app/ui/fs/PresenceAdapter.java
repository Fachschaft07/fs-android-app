package edu.hm.cs.fs.app.ui.fs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.common.model.Presence;

/**
 * Created by FHellman on 11.08.2015.
 */
public class PresenceAdapter extends RecyclerView.Adapter<PresenceAdapter.ViewHolder> {
    private final List<Presence> mData = new ArrayList<>();
    private final Context mContext;

    public PresenceAdapter(Context context) {
        mContext = context;
    }

    public void setData(@NonNull final List<Presence> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_presence, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Presence presence = mData.get(position);

        final int color;
        if ("busy".equalsIgnoreCase(presence.getStatus())) {
            color = mContext.getResources().getColor(R.color.presence_busy);
        } else {
            color = mContext.getResources().getColor(R.color.presence_available);
        }
        holder.mStatus.setImageDrawable(TextDrawable.builder().buildRound("", color));
        holder.mName.setText(presence.getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.presenceStatus)
        ImageView mStatus;
        @Bind(R.id.presenceName)
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
