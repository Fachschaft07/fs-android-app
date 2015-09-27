package edu.hm.cs.fs.app.ui.fs.news;

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
import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private final Context mContext;
    private final List<News> mData = new ArrayList<>();
    private OnItemClickListener mListener;

    public NewsAdapter(final Context context) {
        mContext = context;
    }

    public void setData(@NonNull final List<News> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_news, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final News news = mData.get(position);

        holder.mNews = news;
        holder.mListener = mListener;

        holder.mTitle.setText(news.getTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClicked(@NonNull final News news);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textTitle)
        TextView mTitle;

        News mNews;
        OnItemClickListener mListener;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.textTitle)
        public void onItemClick() {
            if (mListener != null) {
                mListener.onItemClicked(mNews);
            }
        }
    }
}
