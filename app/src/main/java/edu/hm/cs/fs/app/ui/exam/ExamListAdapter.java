package edu.hm.cs.fs.app.ui.exam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.common.model.Exam;

/**
 * @author Fabio
 */
public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.ViewHolder> {

    private final List<Exam> mData = new ArrayList<>();
    private final Context mContext;
    private OnPinListener mListener;

    public ExamListAdapter(Context context) {
        mContext = context;
    }

    public void setData(@NonNull final List<Exam> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_exam, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Exam exam = mData.get(position);

        holder.mItem = exam;
        holder.mListener = mListener;
        holder.mSubject.setText(exam.getModule().getName());
        holder.mTeacher.setText(exam.getExaminers().get(0).getName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListener(@NonNull final OnPinListener mListener) {
        this.mListener = mListener;
    }

    public interface OnPinListener {
        void onPin(@NonNull final Exam exam);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.examSubject)
        TextView mSubject;
        @Bind(R.id.examTeacher)
        TextView mTeacher;
        @Bind(R.id.pinButton)
        ImageButton mPinButton;

        private OnPinListener mListener;
        private Exam mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.pinButton)
        public void onPin() {
            if (mListener != null) {
                mListener.onPin(mItem);
            }
        }
    }
}
