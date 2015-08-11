package edu.hm.cs.fs.app.ui.job;

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
import edu.hm.cs.fs.common.model.Job;

/**
 * Created by FHellman on 10.08.2015.
 */
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private final List<Job> mData = new ArrayList<>();
    private Context mContext;

    public JobAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Job> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_job, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Job job = mData.get(position);

        viewHolder.mHeadline.setText(job.getTitle());
        viewHolder.mSubHead.setText(job.getProvider());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.headline)
        TextView mHeadline;
        @Bind(R.id.subhead)
        TextView mSubHead;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
