package edu.hm.cs.fs.app.ui.timetable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fk07.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import edu.hm.cs.fs.app.presenter.TimetableEditorPresenter;
import edu.hm.cs.fs.common.model.LessonGroup;
import edu.hm.cs.fs.common.model.simple.SimplePerson;

/**
 * @author Fabio
 */
public class TimetableEditorAdapter extends RecyclerView.Adapter<TimetableEditorAdapter.ViewHolder> {

    private final List<LessonGroup> mData = new ArrayList<>();
    @NonNull
    private final Context mContext;
    @NonNull
    private final TimetableEditorPresenter mPresenter;

    public TimetableEditorAdapter(@NonNull final Context context,
                                  @NonNull final TimetableEditorPresenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    public void setData(@NonNull final List<LessonGroup> data) {
        mData.clear();
        notifyDataSetChanged();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.listitem_timetable_editor, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final LessonGroup lessonGroup = mData.get(position);
        final SimplePerson teacher = lessonGroup.getTeacher();

        holder.mLessonGroup = lessonGroup;
        holder.mPresenter = mPresenter;
        holder.init();

        holder.mModule.setText(lessonGroup.getModule().getName());
        if(teacher != null) {
            holder.mTeacher.setText(teacher.getName());
            holder.mTeacher.setVisibility(View.VISIBLE);
        } else {
            holder.mTeacher.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.textModule)
        TextView mModule;
        @Bind(R.id.textTeacher)
        TextView mTeacher;
        @Bind(R.id.checkBox)
        CheckBox mCheckBox;
        @Bind(R.id.pkGroups)
        RadioGroup mPkGroups;
        @Bind({R.id.pkGroup1, R.id.pkGroup2, R.id.pkGroup3})
        List<RadioButton> mPkGroupList;

        private LessonGroup mLessonGroup;
        private TimetableEditorPresenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void init() {
            mCheckBox.setChecked(mPresenter.isLessonGroupSelected(mLessonGroup));
            if (mCheckBox.isChecked() && !mLessonGroup.getGroups().isEmpty()) {
                mPkGroups.setVisibility(View.VISIBLE);

                // Select the pk which was selected previously
                final int amountOfGroups = mLessonGroup.getGroups().size();
                for (int index = 0; index < mPkGroupList.size(); index++) {
                    final RadioButton radioButton = mPkGroupList.get(index);
                    if (index < amountOfGroups) {
                        radioButton.setChecked(mPresenter
                                .isPkSelected(mLessonGroup, mLessonGroup.getGroups().get(index)));
                        radioButton.setVisibility(View.VISIBLE);
                    } else {
                        radioButton.setVisibility(View.GONE);
                    }
                }
            } else {
                mPkGroups.setVisibility(View.GONE);
            }
        }

        @OnCheckedChanged({R.id.pkGroup1, R.id.pkGroup2, R.id.pkGroup3})
        public void onCheckPk() {
            int pk = 1;
            for (RadioButton radioButton : mPkGroupList) {
                mPresenter.setPkSelected(mLessonGroup, pk++, radioButton.isChecked());
            }
        }

        @OnCheckedChanged(R.id.checkBox)
        public void onChecked() {
            final boolean selected = mCheckBox.isChecked();
            mPresenter.setLessonGroupSelected(mLessonGroup, selected);
            init();
        }
    }
}
