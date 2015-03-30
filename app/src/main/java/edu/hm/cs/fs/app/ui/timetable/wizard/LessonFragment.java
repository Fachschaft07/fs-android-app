package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fk07.R;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.LessonHelper;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;

/**
 * Created by Fabio on 29.03.2015.
 */
public class LessonFragment extends Fragment implements ModelCallbacks {
    private static final String ARG_KEY = "key";

    private ReviewFragment.Callbacks mCallbacks;
    private PageFragmentCallbacks mPageCallbacks;
    private String mKey;
    private LessonPage mPage;

    @InjectView(R.id.wizardSideTitle)
    TextView mTitle;
    @InjectView(android.R.id.list)
    ListView mList;

    private List<Lesson> mSelectedLessons = new ArrayList<>();

    private WizardListAdapter mAdapter;
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
        mCallbacks = (ReviewFragment.Callbacks) activity;

        mWizardModel = mCallbacks.onGetModel();
        mWizardModel.registerListener(this);
        onPageTreeChanged();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        mKey = bundle.getString(ARG_KEY);
        mPage = (LessonPage) mPageCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wizard_page, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle.setText(R.string.wizard_lessons);

        mAdapter = new WizardListAdapter(getActivity());
        mList.setAdapter(mAdapter);

        // Extract the previous results
        ArrayList<ReviewItem> reviewItems = new ArrayList<>();
        for (Page page : mWizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }

        StringBuilder groupBuilder = new StringBuilder();
        int index = 0;
        for (ReviewItem reviewItem : reviewItems) {
            if(index++ == 0) {
                continue;
            }
            int endIndex = 1;
            if(reviewItem.getDisplayValue().length() > 2) {
                endIndex = 2;
            }
            groupBuilder.append(reviewItem.getDisplayValue().substring(0, endIndex));
            Log.i("LessonFragment", "ReviewItem: "+ reviewItem.getTitle() + " -> " + reviewItem.getDisplayValue());
        }

        Log.i("LessonFragment", "Group: "+ groupBuilder.toString());

        String facultyStr = reviewItems.get(0).getDisplayValue().substring(0, 2).trim();
        if(facultyStr.length() == 1) {
            facultyStr = "0" + facultyStr;
        }

        final Faculty faculty = Faculty.of(facultyStr);
        final Group group = GroupImpl.of(groupBuilder.toString());

        if(!GroupImpl.of("IF3C").equals(group) && !GroupImpl.of("IF4C").equals(group)) {
            LessonHelper.listAll(getActivity(), faculty, group, new Callback<List<Lesson>>() {
                @Override
                public void onResult(final List<Lesson> result) {
                    mAdapter.clear();
                    for (Lesson lesson : result) {
                        mAdapter.add(lesson);
                    }
                }
            });
        }
    }

    @OnItemClick(android.R.id.list)
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        final Lesson lesson = mAdapter.getItem(position);
        if (mSelectedLessons.contains(lesson)) {
            mSelectedLessons.remove(lesson);
        } else {
            mSelectedLessons.add(lesson);
        }

        mPage.getData(); // TODO put...
        mPage.notifyDataChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

        mWizardModel.unregisterListener(this);
    }

    @Override
    public void onPageDataChanged(final Page page) {

    }

    @Override
    public void onPageTreeChanged() {
        onPageDataChanged(null);
    }

    private final class WizardListAdapter extends ArrayAdapter<Lesson> {
        public WizardListAdapter(final Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ((TextView) view.findViewById(android.R.id.text1)).setText(getItem(position).getModule().getName());
            return view;
        }
    }

    public static LessonFragment create(final String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        LessonFragment fragment = new LessonFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
