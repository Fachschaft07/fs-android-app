package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fk07.R;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.LessonHelper;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.constants.Study;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Fabio on 29.03.2015.
 */
public class LessonFragment extends Fragment {
    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mPageCallbacks;
    private Page mPage;

    @InjectView(R.id.wizardSideTitle)
    TextView mTitle;
    @InjectView(R.id.wizardListView)
    StickyListHeadersListView mList;
    @InjectView(R.id.progressBar)
    ProgressBar mProgress;

    private List<Lesson> mSelectedLessons = new ArrayList<>();

    private LessonAdapter mAdapter;
    private AbstractWizardModel mWizardModel;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ReviewFragment.Callbacks)) {
            throw new ClassCastException("Activity must implement ReviewFragment.Callbacks");
        }
        if(!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mPageCallbacks = (PageFragmentCallbacks) activity;

        final ReviewFragment.Callbacks callbacks = (ReviewFragment.Callbacks) activity;
        mWizardModel = callbacks.onGetModel();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        final String key = bundle.getString(ARG_KEY);
        mPage = mPageCallbacks.onGetPage(key);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wizard_page_lessons, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle.setText(R.string.wizard_lessons);

        mAdapter = new LessonAdapter(getActivity());
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                final Lesson lesson = mAdapter.getItem(position);
                if (mSelectedLessons.contains(lesson)) {
                    mSelectedLessons.remove(lesson);
                } else {
                    mSelectedLessons.add(lesson);
                }
                mAdapter.setSelections(mSelectedLessons);
                mAdapter.notifyDataSetChanged();

                ArrayList<String> selections = new ArrayList<>();
                for (Lesson item : mSelectedLessons) {
                    selections.add(item.getDay().toString() +
                            "|" + item.getTime().toString() +
                            "|" + item.getModule().getName());
                }

                mPage.getData().putStringArrayList(Page.SIMPLE_DATA_KEY, selections);
                mPage.notifyDataChanged();
            }
        });

        // Extract the previous results
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        for (Page page : mWizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }

        StringBuilder groupBuilder = new StringBuilder();
        int index = 0;
        for (ReviewItem reviewItem : reviewItems) {
            // While StudyGroupWizardModel does only have one Faculty to choose...
            if(TextUtils.isEmpty(reviewItem.getDisplayValue())) {// || index++ == 0) {
                continue;
            }
            int endIndex = 1;
            if(reviewItem.getDisplayValue().length() > 2) {
                endIndex = 2;
            }
            groupBuilder.append(reviewItem.getDisplayValue().substring(0, endIndex));
        }

        // While StudyGroupWizardModel does only have one Faculty to choose...
        /*
        String facultyStr = reviewItems.get(0).getDisplayValue().substring(0, 2).trim();
        if(facultyStr.length() == 1) {
            facultyStr = "0" + facultyStr;
        }

        final Faculty faculty = Faculty.of(facultyStr);
        */
        final Group group = GroupImpl.of(groupBuilder.toString());

        if(!GroupImpl.of("IF3C").equals(group) && !GroupImpl.of("IF4C").equals(group)) {
            mSelectedLessons.clear();
            mAdapter.clear();
            setProgressEnabled(true);
            LessonHelper.listAll(getActivity(), Faculty._07, group, new Callback<List<Lesson>>() {
                @Override
                public void onResult(final List<Lesson> result) {
                    for (Lesson lesson : result) {
                        mAdapter.add(lesson);
                    }
                    setProgressEnabled(false);
                }
            });

            if(group.getStudy() == Study.IB || group.getStudy() == Study.IN) {
                LessonHelper.listAll(getActivity(), Faculty._10, group, new Callback<List<Lesson>>() {
                    @Override
                    public void onResult(final List<Lesson> result) {
                        for (Lesson lesson : result) {
                            mAdapter.add(lesson);
                        }
                        setProgressEnabled(false);
                    }
                });
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPageCallbacks = null;
    }

    private void setProgressEnabled(boolean enabled) {
        mProgress.setVisibility(enabled ? View.VISIBLE : View.GONE);
        mList.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

    public static LessonFragment create(final String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        LessonFragment fragment = new LessonFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
