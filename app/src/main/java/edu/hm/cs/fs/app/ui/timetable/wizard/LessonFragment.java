package edu.hm.cs.fs.app.ui.timetable.wizard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fk07.R;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.model.ReviewItem;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.LessonHelper;
import edu.hm.cs.fs.app.datastore.model.Group;
import edu.hm.cs.fs.app.datastore.model.Lesson;
import edu.hm.cs.fs.app.datastore.model.constants.Faculty;
import edu.hm.cs.fs.app.datastore.model.impl.GroupImpl;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

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
    @InjectView(R.id.wizardListView)
    StickyListHeadersListView mList;

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

                mPage.getData(); // TODO put...
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
            /*
            if(index++ == 0) {
                continue;
            }
            */
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
        final Faculty faculty = Faculty._07;
        final Group group = GroupImpl.of(groupBuilder.toString());

        if(!GroupImpl.of("IF3C").equals(group) && !GroupImpl.of("IF4C").equals(group)) {
            mSelectedLessons.clear();
            mAdapter.clear();
            LessonHelper.listAll(getActivity(), faculty, group, new Callback<List<Lesson>>() {
                @Override
                public void onResult(final List<Lesson> result) {
                    Collections.sort(result, new Comparator<Lesson>() {
                        @Override
                        public int compare(final Lesson lhs, final Lesson rhs) {
                            return getDate(lhs).compareTo(getDate(rhs));
                        }

                        private Calendar getDate(Lesson lesson) {
                            Calendar cal = Calendar.getInstance();
                            cal.set(2015, 2, 30); // This is a monday
                            cal.set(Calendar.HOUR_OF_DAY, lesson.getTime().getHour());
                            cal.set(Calendar.MINUTE, lesson.getTime().getMinute());

                            int weekDay = lesson.getDay().getId();
                            switch (weekDay) {
                                case Calendar.TUESDAY:
                                    cal.add(Calendar.DATE, 1);
                                    break;
                                case Calendar.WEDNESDAY:
                                    cal.add(Calendar.DATE, 2);
                                    break;
                                case Calendar.THURSDAY:
                                    cal.add(Calendar.DATE, 3);
                                    break;
                                case Calendar.FRIDAY:
                                    cal.add(Calendar.DATE, 4);
                                    break;
                                case Calendar.SATURDAY:
                                    cal.add(Calendar.DATE, 5);
                                    break;
                                case Calendar.SUNDAY:
                                    cal.add(Calendar.DATE, 6);
                                    break;
                            }
                            return cal;
                        }
                    });

                    mSelectedLessons.clear();
                    mAdapter.clear();
                    for (Lesson lesson : result) {
                        mAdapter.add(lesson);
                    }
                }
            });
        }
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

    public static LessonFragment create(final String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        LessonFragment fragment = new LessonFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
