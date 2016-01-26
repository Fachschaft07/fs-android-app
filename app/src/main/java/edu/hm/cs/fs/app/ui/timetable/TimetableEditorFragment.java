package edu.hm.cs.fs.app.ui.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fk07.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.hm.cs.fs.app.App;
import edu.hm.cs.fs.app.presenter.TimetableEditorPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.common.constant.Letter;
import edu.hm.cs.fs.common.constant.Semester;
import edu.hm.cs.fs.common.constant.Study;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.LessonGroup;

@PerActivity
public class TimetableEditorFragment extends BaseFragment<TimetableEditorComponent, TimetableEditorPresenter>
        implements TimetableEditorListView, Toolbar.OnMenuItemClickListener {

    private static final String PREF_STUDY = "study";
    private static final String PREF_SEMESTER = "semester";
    private static final String PREF_LETTER = "letter";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.listView)
    RecyclerView mListView;

    @Bind(R.id.spinnerStudy)
    AppCompatSpinner mStudySpinner;

    @Bind(R.id.spinnerSemester)
    AppCompatSpinner mSemesterSpinner;

    @Bind(R.id.spinnerLetter)
    AppCompatSpinner mLetterSpinner;

    private TimetableEditorAdapter mAdapter;
    private SharedPreferences mPrefs;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mPrefs = getContext().getSharedPreferences("TimetableEditor", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getNavigator().goOneBack();
            }
        });
        mToolbar.inflateMenu(R.menu.timetable_editor);
        mToolbar.setOnMenuItemClickListener(this);

        mStudySpinner.setAdapter(SimpleSpinnerAdapter.create(getContext(), Study.values(), false));
        mStudySpinner.setSelection(mPrefs.getInt(PREF_STUDY, 0));
        mSemesterSpinner.setAdapter(SimpleSpinnerAdapter.create(getContext(), Semester.values(), true));
        mSemesterSpinner.setSelection(mPrefs.getInt(PREF_SEMESTER, 0));
        mLetterSpinner.setAdapter(SimpleSpinnerAdapter.create(getContext(), Letter.values(), true));
        mLetterSpinner.setSelection(mPrefs.getInt(PREF_LETTER, 0));

        mAdapter = new TimetableEditorAdapter(getActivity(), getPresenter());
        mListView.setAdapter(mAdapter);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mSwipeRefreshLayout.setEnabled(false);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_timetable_editor;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.reset_timetable_title)
                        .setMessage(R.string.reset_timetable_message)
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                getPresenter().reset();
                                onRefresh();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
                return true;
            default:
                return false;
        }
    }

    @OnClick(R.id.search)
    @Override
    public void onRefresh() {
        final SharedPreferences.Editor editor = mPrefs.edit();

        final String study = mStudySpinner.getSelectedItem().toString();
        editor.putInt(PREF_STUDY, mStudySpinner.getSelectedItemPosition());
        final String semester = mSemesterSpinner.getSelectedItem().toString().replaceAll("-", "");
        editor.putInt(PREF_SEMESTER, mSemesterSpinner.getSelectedItemPosition());
        final String letter = mLetterSpinner.getSelectedItem().toString().replaceAll("-", "");
        editor.putInt(PREF_LETTER, mLetterSpinner.getSelectedItemPosition());

        editor.apply();

        getPresenter().loadModules(Group.of(study + semester + letter));

        /*
        final EditText editText = mTextGroup.getEditText();
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            final Group group = Group.of(editText.getText().toString());
            if (group.getStudy() != null) {
                mTextGroup.setError("");
                getPresenter().loadModules(group);
            } else {
                mTextGroup.setError(getString(R.string.group_format));
            }
        }
        */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected TimetableEditorComponent onCreateNonConfigurationComponent() {
        return DaggerTimetableEditorComponent.builder()
                .appComponent(App.getAppComponent(getMainActivity()))
                .build();
    }

    @Override
    public void clear() {
        mAdapter.clear();
    }

    @Override
    public void add(@NonNull LessonGroup item) {
        mAdapter.add(item);
    }

    private static class SimpleSpinnerAdapter extends ArrayAdapter<String> {
        public SimpleSpinnerAdapter(final Context context, final String[] objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, objects);
        }

        public static ArrayAdapter<String> create(@NonNull final Context context,
                                                  @NonNull final Enum[] type,
                                                  final boolean emptyItem) {
            int length = type.length;
            if (emptyItem) {
                length += 1;
            }
            final String[] items = new String[length];
            int index = 0;
            if (emptyItem) {
                items[index++] = "-";
            }
            for (Enum item : type) {
                String name = item.toString();
                if (item instanceof Semester) {
                    name = name.substring(1);
                }
                items[index++] = name;
            }
            return new SimpleSpinnerAdapter(context, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View view = super.getView(position, convertView, parent);
            setText(view).setTextColor(Color.WHITE);
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            final View view = super.getDropDownView(position, convertView, parent);
            setText(view);
            return view;
        }

        private TextView setText(View view) {
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }
    }
}
