package edu.hm.cs.fs.app.ui.publictransport;

import android.content.Context;
import android.graphics.Color;
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
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.common.model.PublicTransport;

/**
 * Created by FHellman on 10.08.2015.
 */
public class PublicTransportAdapter extends RecyclerView.Adapter<PublicTransportAdapter.ViewHolder> {

    private final List<PublicTransport> mData = new ArrayList<>();

    private final Context mContext;

    public PublicTransportAdapter(@NonNull final Context context) {
        mContext = context;
    }

    public void setData(List<PublicTransport> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_public_transport, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final PublicTransport entry = mData.get(position);

        TextDrawable drawable = TextDrawable.builder().beginConfig().withBorder(2).fontSize(mContext.getResources().getDimensionPixelSize(R.dimen.text_size_mvv_line)).textColor(Color.DKGRAY).endConfig().buildRound(entry.getLine(), Color.TRANSPARENT);

        viewHolder.mLine.setImageDrawable(drawable);
        viewHolder.mDestination.setText(entry.getDestination());
        viewHolder.mTime.setText(mContext.getString(R.string.departure, entry.getDepartureIn(TimeUnit.MINUTES)));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imageLine)
        ImageView mLine;

        @Bind(R.id.textDestination)
        TextView mDestination;

        @Bind(R.id.textTime)
        TextView mTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
