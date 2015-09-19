package edu.hm.cs.fs.app.ui.timetable;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
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

        // TODO Select already added modules/lessons
        holder.init();

        holder.mModule.setText(lessonGroup.getModule().getName());
        holder.mTeacher.setText(teacher.getTitle() + " " + teacher.getLastName()
                + " " + teacher.getFirstName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Context mContext;
        @Bind(R.id.textModule)
        TextView mModule;
        @Bind(R.id.textTeacher)
        TextView mTeacher;
        @Bind(R.id.checkBox)
        CheckBox mCheckBox;
        @Bind(R.id.listPks)
        ListView mListPks;

        private LessonGroup mLessonGroup;
        private TimetableEditorPresenter mPresenter;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);
        }

        public void init() {
            if(mPresenter.isLessonGroupSelected(mLessonGroup)) {
                mCheckBox.setChecked(true);
            }
        }

        @OnCheckedChanged(R.id.checkBox)
        public void onChecked() {
            final boolean selected = mCheckBox.isChecked();
            mPresenter.setLessonGroupSelected(mLessonGroup, selected);

            if (!mLessonGroup.getGroups().isEmpty() && selected) {
                mListPks.setVisibility(View.VISIBLE);

                mListPks.setAdapter(new ArrayAdapter<>(mContext,
                        android.R.layout.simple_list_item_single_choice, mLessonGroup.getGroups()));
                mListPks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Integer pk = mLessonGroup.getGroups().get(position);
                        final boolean selected = !mPresenter.isPkSelected(mLessonGroup, pk);
                        mPresenter.setPkSelected(mLessonGroup, pk, selected);
                        ((CheckedTextView) view).setChecked(selected);
                    }
                });
                calculateListHeight(mListPks);
            } else {
                mListPks.setVisibility(View.GONE);
            }
        }

        private void calculateListHeight(ListView listView) {
            final ListAdapter adapter = listView.getAdapter();

            // Calculate the height of the ListView to display all items
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View item = adapter.getView(i, null, listView);
                item.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                );
                totalHeight += item.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + listView.getDividerHeight() * (adapter.getCount() - 1);

            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}
