package edu.hm.cs.fs.app.ui.blackboard;

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
import edu.hm.cs.fs.common.model.BlackboardEntry;

/**
 * Created by FHellman on 12.08.2015.
 */
public class BlackBoardAdapter extends RecyclerView.Adapter<BlackBoardAdapter.ViewHolder> {
    private final List<BlackboardEntry> mData = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public BlackBoardAdapter(@NonNull final Context context) {
        mContext = context;
    }

    public void setData(@NonNull final List<BlackboardEntry> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_blackboard, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BlackboardEntry entry = mData.get(position);

        holder.mBlackboardEntry = entry;
        holder.mListener = mListener;
        holder.mTitle.setText(MarkdownUtil.toHtml(entry.getSubject()));
        holder.mAuthor.setText(entry.getAuthor());
        if(!entry.getGroups().isEmpty()) {
            holder.mGroups.setText(entry.getGroups().toString()
                    .substring(1, entry.getGroups().toString().length() - 1));
            holder.mGroups.setVisibility(View.VISIBLE);
        } else {
            holder.mGroups.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textTitle)
        TextView mTitle;
        @Bind(R.id.textAuthor)
        TextView mAuthor;
        @Bind(R.id.textGroups)
        TextView mGroups;
        BlackboardEntry mBlackboardEntry;
        OnItemClickListener mListener;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick({R.id.textTitle, R.id.textAuthor, R.id.textGroups})
        public void onItemClick() {
            if(mListener != null) {
                mListener.onItemClicked(mBlackboardEntry);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(@NonNull final BlackboardEntry entry);
    }
}
