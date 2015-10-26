package edu.hm.cs.fs.app.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.card.provider.ListCardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListAdapter;
import com.dexafree.materialList.view.MaterialListView;
import com.fk07.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.HomePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardDetailFragment;
import edu.hm.cs.fs.app.ui.lostfound.LostFoundFragment;
import edu.hm.cs.fs.app.view.IHomeView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;
import edu.hm.cs.fs.common.model.News;

/**
 * @author Fabio
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView,
        Toolbar.OnMenuItemClickListener, OnDismissCallback,
        RecyclerItemClickListener.OnItemClickListener {

    private static final String HOLIDAY = "holiday";
    private static final String NEXT_LESSON = "next_lesson";
    private static final String BLACKBOARD = "blackboard";
    private static final String MENSA = "meals";
    private static final String LOSTFOUND = "lostfound";
    private static final String FS_NEWS = "student_council";
    private static final String FS_NEWS_ITEM = "student_council_";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.materialCardListView)
    MaterialListView mListView;

    private SharedPreferences mPrefs;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(this);

        mListView.setOnDismissCallback(this);
        mListView.addOnItemTouchListener(this);

        mSwipeRefreshLayout.setEnabled(false);

        setPresenter(new HomePresenter(getActivity(), this));
        getPresenter().loadHappenings(false);
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                MainActivity.getNavigator().goTo(new HomePreferenceFragment());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDismiss(@NonNull Card card, int position) {
        //noinspection ConstantConditions
        mPrefs.edit().putBoolean(card.getTag().toString(), false).apply();
    }

    @Override
    public void onRefresh() {
        getPresenter().loadHappenings(true);
    }

    @Override
    public void showNextLesson(@Nullable Lesson lesson) {
        if (isActive(NEXT_LESSON) && lesson != null) {
            Calendar lessonStart = Calendar.getInstance();
            lessonStart.set(Calendar.DAY_OF_WEEK, lesson.getDay().getCalendarId());
            lessonStart.set(Calendar.HOUR_OF_DAY, lesson.getHour());
            lessonStart.set(Calendar.MINUTE, lesson.getMinute());

            Calendar lessonEnd = Calendar.getInstance();
            lessonEnd.setTimeInMillis(lessonStart.getTimeInMillis());
            lessonEnd.add(Calendar.MINUTE, 90);

            final Card card = new Card.Builder(getActivity())
                    .setTag(NEXT_LESSON)
                    .setDismissible()
                    .withProvider(new CardProviderNextLesson())
                    .setLayout(R.layout.card_next_lesson)
                    .setTitle(lesson.getModule().getName())
                    .setSubtitle(lesson.getTeacher().getName())
                    .setSubtitleColor(Color.GRAY)
                    .setTime(String.format(Locale.getDefault(), "%1$tH:%1$tM - %2$tH:%2$tM",
                            lessonStart, lessonEnd))
                    .setPlace(lesson.getRoom())
                    .endConfig()
                    .build();
            add(card, true);
        }
    }

    @Override
    public void showBlackboardNews(@NonNull List<BlackboardEntry> news) {
        if (isActive(BLACKBOARD) && !news.isEmpty()) {
            Card card = new Card.Builder(getActivity())
                    .setTag(BLACKBOARD)
                    .setDismissible()
                    .withProvider(new ListCardProvider())
                    .setLayout(R.layout.material_list_card_layout)
                    .setTitle(R.string.blackboard)
                    .setDescription(R.string.blackboard_current)
                    .setAdapter(new ArrayAdapter<BlackboardEntry>(
                            getActivity(), R.layout.listitem_blackboard, R.id.textTitle, news) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final View view = super.getView(position, convertView, parent);
                            final TextView textTitle = (TextView) view.findViewById(R.id.textTitle);
                            final BlackboardEntry item = getItem(position);
                            textTitle.setText(item.getSubject());
                            final TextView author = (TextView) view.findViewById(R.id.textAuthor);
                            author.setText(item.getAuthor().getName());
                            final TextView groups = (TextView) view.findViewById(R.id.textGroups);
                            final List<Group> groupList = item.getGroups();
                            if (!groupList.isEmpty()) {
                                groups.setText(groupList.toString().substring(1,
                                        groupList.toString().length() - 1));
                            }
                            return view;
                        }
                    })
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final ArrayAdapter<BlackboardEntry> adapter = (ArrayAdapter<BlackboardEntry>) parent.getAdapter();
                            final BlackboardEntry item = adapter.getItem(position);

                            BlackBoardDetailFragment fragment = new BlackBoardDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(BlackBoardDetailFragment.ARGUMENT_ID, item.getId());
                            fragment.setArguments(bundle);
                            MainActivity.getNavigator().goTo(fragment);
                        }
                    })
                    .setDividerVisible(true)
                    .endConfig()
                    .build();
            add(card, true);
        }
    }

    @Override
    public void showMealsOfToday(@NonNull List<Meal> meals) {
        if (isActive(MENSA) && !meals.isEmpty()) {
            Card card = new Card.Builder(getActivity())
                    .setTag(MENSA)
                    .setDismissible()
                    .withProvider(new ListCardProvider())
                    .setLayout(R.layout.material_list_card_layout)
                    .setTitle(R.string.mensa)
                    .setDescription(R.string.mensa_today)
                    .setAdapter(new ArrayAdapter<Meal>(
                            getActivity(), R.layout.listitem_meal, R.id.textView, meals) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            final View view = super.getView(position, convertView, parent);

                            final Meal meal = getItem(position);

                            int color;
                            switch (meal.getType()) {
                                case VEGAN:
                                    color = getResources().getColor(R.color.meal_vegan);
                                    break;
                                case MEATLESS:
                                    color = getResources().getColor(R.color.meal_meatless);
                                    break;
                                default:
                                    color = getResources().getColor(R.color.meal_meat);
                                    break;
                            }
                            TextDrawable drawable = TextDrawable.builder()
                                    .beginConfig()
                                    .withBorder(2)
                                    .fontSize(getResources()
                                            .getDimensionPixelSize(R.dimen.text_size_meal_type))
                                    .useFont(Typeface.DEFAULT_BOLD)
                                    .endConfig()
                                    .buildRound(meal.getType().toString(), color);

                            final ImageView image = (ImageView) view.findViewById(R.id.imageView);
                            image.setImageDrawable(drawable);
                            final TextView text = (TextView) view.findViewById(R.id.textView);
                            text.setText(meal.getName());

                            return view;
                        }
                    }).setDividerVisible(true).endConfig().build();
            add(card, true);
        }
    }

    @Override
    public void showLostAndFound(@NonNull Integer amountOfLostFound) {
        if (isActive(LOSTFOUND) && amountOfLostFound > 0) {
            Card card = new Card.Builder(getActivity())
                    .setTag(LOSTFOUND)
                    .setDismissible()
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.card_simple_text_button)
                    .setTitle(R.string.lostfound)
                    .setDescription(getResources()
                            .getQuantityString(R.plurals.lostfound_description,
                                    amountOfLostFound, amountOfLostFound))
                    .setDrawable(R.drawable.ic_loyalty_grey_600_24dp)
                    .addAction(R.id.right_text_button, new TextViewAction(getContext())
                            .setText(getString(R.string.view))
                            .setTextResourceColor(R.color.colorAccent)
                            .setListener(new OnActionClickListener() {
                                @Override
                                public void onActionClicked(final View view, final Card card) {
                                    MainActivity.getNavigator().goTo(new LostFoundFragment());
                                }
                            }))
                    .endConfig()
                    .build();
            add(card, false);
        }
    }

    @Override
    public void showNextHoliday(@Nullable Holiday holiday) {
        if (isActive(HOLIDAY) && holiday != null) {
            Calendar calendarToday = Calendar.getInstance();

            long timeDiff = holiday.getStart().getTime() - calendarToday.getTimeInMillis();
            int daysLeft = (int) (timeDiff / (1000 * 60 * 60 * 24));

            Card card = new Card.Builder(getActivity())
                    .setTag(HOLIDAY)
                    .setDismissible()
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.material_small_image_card)
                    .setTitle(holiday.getName())
                    .setDescription(getResources().getQuantityString(R.plurals.next_holidays,
                            daysLeft, daysLeft, holiday.getName(),
                            holiday.getStart(), holiday.getEnd()))
                    .setDrawable(R.drawable.ic_flight_orange_800_36dp)
                    .endConfig()
                    .build();
            add(card, false);
        }
    }

    @Override
    public void showFsNews(@NonNull final List<News> news) {
        if (isActive(FS_NEWS)) {
            int index = 0;
            for (final News newsItem : news) {
                final CardProvider cardConfig = new Card.Builder(getContext())
                        .setTag(FS_NEWS_ITEM + index++)
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.card_simple_text_button)
                        .setTitle(newsItem.getTitle())
                        .setDescription(newsItem.getDescription());
                if (!TextUtils.isEmpty(newsItem.getLink())) {
                    cardConfig.addAction(R.id.right_text_button, new TextViewAction(getContext())
                            .setText(getString(R.string.read_more))
                            .setTextResourceColor(R.color.colorAccent)
                            .setListener(new OnActionClickListener() {
                                @Override
                                public void onActionClicked(final View view, final Card card) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(newsItem.getLink()));
                                    startActivity(intent);
                                }
                            }));
                }
                Card card = cardConfig.endConfig().build();
                add(card, false);
            }
        }
    }

    private boolean isActive(String pref) {
        return mPrefs.getBoolean(pref, true) && getActivity() != null;
    }

    private void add(@NonNull final Card card, final boolean atStart) {
        if (mListView != null) {
            MaterialListAdapter adapter = mListView.getAdapter();
            for (int index = 0; index < adapter.getItemCount(); index++) {
                final Card foundCard = adapter.getCard(index);
                //noinspection ConstantConditions
                if (foundCard != null && foundCard.getTag().equals(card.getTag())) {
                    adapter.remove(foundCard, false);
                    break;
                }
            }
            if (atStart) {
                adapter.addAtStart(card);
            } else {
                adapter.add(card);
            }
        }
    }

    @Override
    public void onItemClick(@NonNull final Card card, final int i) {

    }

    @Override
    public void onItemLongClick(@NonNull final Card card, final int i) {

    }

    @Override
    public void clear() {
        mListView.getAdapter().clearAll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
