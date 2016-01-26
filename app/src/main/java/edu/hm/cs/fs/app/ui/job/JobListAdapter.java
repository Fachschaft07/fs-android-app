package edu.hm.cs.fs.app.ui.job;

import android.content.Context;
import android.support.annotation.NonNull;
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
import butterknife.OnClick;
import edu.hm.cs.fs.app.util.MarkdownUtil;
import edu.hm.cs.fs.common.model.simple.SimpleJob;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {

    private final List<SimpleJob> mData = new ArrayList<>();

    private Context mContext;

    private OnItemClickListener mListener;

    public JobListAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<SimpleJob> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        notifyItemRangeRemoved(0, mData.size());
        mData.clear();
    }

    public void add(SimpleJob item) {
        mData.add(item);
        notifyItemInserted(mData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_job, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final SimpleJob job = mData.get(position);

        viewHolder.mJob = job;
        viewHolder.mListener = mListener;
        viewHolder.mHeadline.setText(MarkdownUtil.toHtml(job.getTitle()));
        viewHolder.mSubHead.setText(job.getProvider());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClicked(@NonNull final SimpleJob job);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.headline)
        TextView mHeadline;

        @Bind(R.id.subhead)
        TextView mSubHead;

        SimpleJob mJob;

        OnItemClickListener mListener;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.headline, R.id.subhead})
        public void onItemClick() {
            if (mListener != null) {
                mListener.onItemClicked(mJob);
            }
        }
    }
}
