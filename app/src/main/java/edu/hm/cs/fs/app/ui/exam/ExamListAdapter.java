package edu.hm.cs.fs.app.ui.exam;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.fk07.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.common.model.Exam;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

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

    public void clear() {
        notifyItemRangeRemoved(0, mData.size());
        mData.clear();
    }

    public void add(Exam item) {
        mData.add(item);
        notifyItemInserted(mData.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_exam, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Exam exam = mData.get(position);

        holder.mItem = exam;
        holder.mListener = new OnPinListener() {
            @Override
            public Observable<Boolean> onPin(@NonNull Exam exam) {
                return mListener.onPin(exam).doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        notifyItemChanged(position);
                    }
                });
            }

            @Override
            public Observable<Boolean> isPined(@NonNull Exam exam) {
                return mListener.isPined(exam);
            }
        };
        holder.mGroup.setImageDrawable(TextDrawable.builder().buildRound(exam.getStudy().toString(), Color.LTGRAY));
        holder.mSubject.setText(exam.getModule().getName());
        holder.mTeacher.setText(exam.getExaminers().get(0).getName());
        holder.load();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setListener(@NonNull final OnPinListener mListener) {
        this.mListener = mListener;
    }

    public interface OnPinListener {
        Observable<Boolean> onPin(@NonNull final Exam exam);

        Observable<Boolean> isPined(@NonNull final Exam exam);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements Action1<Boolean> {
        @Bind(R.id.group)
        ImageView mGroup;
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

        public void load() {
            mListener.isPined(mItem).subscribe(this);
        }

        @OnClick(R.id.pinButton)
        public void onPin() {
            mListener.onPin(mItem).subscribe(this);
        }

        @Override
        public void call(Boolean aBoolean) {
            if (aBoolean) {
                mPinButton.setImageResource(R.drawable.ic_star_amber_600_18dp);
            } else {
                mPinButton.setImageResource(R.drawable.ic_star_border_grey_600_18dp);
            }
            mPinButton.invalidate();
        }
    }
}
