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
 * @author Fabio
 */
public class BlackBoardListAdapter extends RecyclerView.Adapter<BlackBoardListAdapter.ViewHolder> {

    private final List<BlackboardEntry> mData = new ArrayList<>();

    private Context mContext;

    private OnItemClickListener mListener;

    public BlackBoardListAdapter(@NonNull final Context context) {
        mContext = context;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void add(BlackboardEntry blackboardEntry) {
        mData.add(blackboardEntry);
        notifyItemInserted(mData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_blackboard, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final BlackboardEntry entry = mData.get(position);

        holder.mBlackboardEntry = entry;
        holder.mListener = mListener;
        holder.mTitle.setText(MarkdownUtil.toHtml(entry.getSubject()));
        holder.mAuthor.setText(entry.getAuthor().getName());
        if (!entry.getGroups().isEmpty()) {
            holder.mGroups.setText(entry.getGroups().toString().substring(1, entry.getGroups().toString().length() - 1));
            holder.mGroups.setVisibility(View.VISIBLE);
        } else {
            holder.mGroups.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClicked(@NonNull final BlackboardEntry entry);
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
            if (mListener != null) {
                mListener.onItemClicked(mBlackboardEntry);
            }
        }
    }
}
