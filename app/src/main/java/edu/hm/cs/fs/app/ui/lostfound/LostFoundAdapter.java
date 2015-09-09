package edu.hm.cs.fs.app.ui.lostfound;

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
import edu.hm.cs.fs.app.util.MarkdownUtil;
import edu.hm.cs.fs.common.model.LostFound;

/**
 * @author Fabio
 */
public class LostFoundAdapter extends RecyclerView.Adapter<LostFoundAdapter.ViewHolder> {

    private final List<LostFound> mData = new ArrayList<>();

    private Context mContext;

    public LostFoundAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<LostFound> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.listitem_lostfound, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final LostFound lostFound = mData.get(position);

        viewHolder.mSubject.setText(MarkdownUtil.toHtml(lostFound.getSubject()));
        viewHolder.mDate.setText(String.format("%1$td.%1$tm.%1$tY", lostFound.getDate()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textSubject)
        TextView mSubject;

        @Bind(R.id.textDate)
        TextView mDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
