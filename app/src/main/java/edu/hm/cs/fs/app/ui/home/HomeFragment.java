package edu.hm.cs.fs.app.ui.home;

import android.content.ActivityNotFoundException;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.OnButtonClickListener;
import com.dexafree.materialList.card.provider.BasicButtonsCardProvider;
import com.dexafree.materialList.card.provider.BasicListCardProvider;
import com.dexafree.materialList.card.provider.BigImageCardProvider;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.dexafree.materialList.card.provider.WelcomeCardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.view.MaterialListAdapter;
import com.dexafree.materialList.view.MaterialListView;
import com.fk07.R;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.HomePresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.ui.lostfound.LostFoundFragment;
import edu.hm.cs.fs.app.view.IHomeView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;

/**
 * @author Fabio
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView,
        SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, OnDismissCallback {

    private static final String APP_RATING = "app_rating";
    private static final String SEMESTER_START = "semester_start";
    private static final String HOLIDAY = "holiday";
    private static final String NEXT_LESSON = "next_lesson";
    private static final String BLACKBOARD = "blackboard";
    private static final String MENSA = "meals";
    private static final String LOSTFOUND = "lostfound";
    private static final String HOLIDAY_TIME_RANGE = "%1$ta %1$td.%1$tm.%1$tY - " +
            "%2$ta %2$td.%2$tm.%2$tY";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.listView)
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

        mSwipeRefreshLayout.setOnRefreshListener(this);
        initSwipeRefreshLayout(mSwipeRefreshLayout);

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
                getMainActivity().getNavigator().goTo(new HomePreferenceFragment());
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDismiss(@NonNull Card card, int position) {
        mPrefs.edit().putBoolean(card.getTag().toString(), false).apply();
    }

    @Override
    public void onRefresh() {
        getPresenter().loadHappenings(true);
    }

    @Override
    public void showSemesterStart() {
        if (isActive(SEMESTER_START, false) && isSemesterStart()) {
            Card card = new Card.Builder(getActivity())
                    .setTag(SEMESTER_START)
                    .withProvider(BigImageCardProvider.class)
                    .setTitle(R.string.semester_start)
                    .setDescription(R.string.semester_start_description)
                            // TODO Use an image where the rights belong to the student council
                    .setDrawable("http://cdn.meme.am/instances/55179646.jpg").endConfig().build();
            add(card, true);
        }
    }

    @Override
    public void showNextLesson(@Nullable Lesson lesson) {
        if (isActive(NEXT_LESSON, false) && lesson != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, lesson.getDay().getCalendarId());

            final SmallImageCardProvider provider = new Card.Builder(getActivity())
                    .setTag(NEXT_LESSON)
                    .setDismissible()
                    .withProvider(SmallImageCardProvider.class)
                    .setTitle(lesson.getModule().getName())
                    .setDrawable(R.drawable.ic_view_week_grey_600_24dp);
            if (lesson.getSuffix() != null && lesson.getSuffix().length() > 0) {
                provider.setDescription(
                        getString(R.string.next_lesson_suffix, calendar, // Monday
                                lesson.getTime().getStart(), // 08:15
                                lesson.getRoom(), // R2.007
                                lesson.getTeacher().getTitle() + " " +
                                        lesson.getTeacher().getLastName(), // Prof. Dr. Müller
                                lesson.getSuffix())); // Praktikum
            } else {
                provider.setDescription(
                        getString(R.string.next_lesson, calendar, // Monday
                                lesson.getTime().getStart(), // 08:15
                                lesson.getRoom(), // R2.007
                                lesson.getTeacher().getTitle() + " " +
                                        lesson.getTeacher().getLastName())); // Prof. Dr. Müller
            }
            Card card = provider.endConfig().build();
            add(card, true);
        }
    }

    @Override
    public void showBlackboardNews(@NonNull List<BlackboardEntry> news) {
        if (isActive(BLACKBOARD, false) && !news.isEmpty()) {
            Card card = new Card.Builder(getActivity())
                    .setTag(BLACKBOARD)
                    .setDismissible()
                    .withProvider(BasicListCardProvider.class)
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
                            author.setText(item.getAuthor().getTitle() + " "
                                    + item.getAuthor().getLastName());
                            final TextView groups = (TextView) view.findViewById(R.id.textGroups);
                            final List<Group> groupList = item.getGroups();
                            if (!groupList.isEmpty()) {
                                groups.setText(groupList.toString().substring(1,
                                        groupList.toString().length() - 1));
                            }
                            return view;
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
        if (isActive(MENSA, false) && !meals.isEmpty()) {
            Card card = new Card.Builder(getActivity())
                    .setTag(MENSA)
                    .setDismissible()
                    .withProvider(BasicListCardProvider.class)
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
        if (isActive(LOSTFOUND, false) && amountOfLostFound > 0) {
            Card card = new Card.Builder(getActivity())
                    .setTag(LOSTFOUND)
                    .setDismissible()
                    .withProvider(BasicButtonsCardProvider.class)
                    .setTitle(R.string.lostfound)
                    .setDescription(getResources()
                            .getQuantityString(R.plurals.lostfound_description,
                                    amountOfLostFound, amountOfLostFound))
                    .setDrawable(R.drawable.ic_loyalty_grey_600_24dp)
                    .setRightButtonText(R.string.view)
                    .setRightButtonTextResourceColor(R.color.colorAccent)
                    .setOnRightButtonClickListener(new OnButtonClickListener() {
                        @Override
                        public void onButtonClicked(View view, Card card) {
                            getMainActivity().getNavigator().goTo(new LostFoundFragment());
                        }
                    })
                    .endConfig()
                    .build();
            add(card, false);
        }
    }

    @Override
    public void showNextHoliday(@Nullable Holiday holiday) {
        if (isActive(HOLIDAY, false) && holiday != null) {
            Card card = new Card.Builder(getActivity())
                    .setTag(HOLIDAY)
                    .setDismissible()
                    .withProvider(SmallImageCardProvider.class)
                    .setTitle(holiday.getName())
                    .setDescription(String.format(HOLIDAY_TIME_RANGE,
                            holiday.getStart(), holiday.getEnd()))
                    .setDrawable(R.drawable.ic_flight_orange_800_36dp)
                    .endConfig()
                    .build();
            add(card, false);
        }
    }

    @Override
    public void showAppRate() {
        if (isActive(APP_RATING, true)) {
            Card card = new Card.Builder(getActivity())
                    .setTag(APP_RATING)
                    .withProvider(WelcomeCardProvider.class)
                    .setTitle(R.string.rate_title)
                    .setSubtitle(R.string.rate_subtitle)
                    .setDescription(R.string.rate_description)
                    .setButtonText(R.string.rate)
                    .setOnButtonPressedListener(new OnButtonClickListener() {
                        @Override
                        public void onButtonClicked(View view, Card card) {
                            final String appPackageName = getActivity().getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + appPackageName)));
                            } catch (ActivityNotFoundException ignored) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id="
                                                + appPackageName)));
                            }
                        }
                    })
                    .setBackgroundResourceColor(R.color.colorAccent)
                    .setDividerColor(Color.GRAY)
                    .endConfig()
                    .build();
            add(card, false);
        }
    }

    private boolean isActive(String pref, boolean defaultValue) {
        return mPrefs.getBoolean(pref, false) && getActivity() != null;
    }

    private boolean isSemesterStart() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) == Calendar.MARCH
                && calendar.get(Calendar.DAY_OF_MONTH) >= 9
                && calendar.get(Calendar.DAY_OF_MONTH) < 20
                || calendar.get(Calendar.MONTH) == Calendar.OCTOBER
                && calendar.get(Calendar.DAY_OF_MONTH) >= 1
                && calendar.get(Calendar.DAY_OF_MONTH) < 7;
    }

    private void add(@NonNull final Card card, final boolean atStart) {
        if (mListView != null) {
            MaterialListAdapter adapter = (MaterialListAdapter) mListView.getAdapter();
            for (int index = 0; index < adapter.getItemCount(); index++) {
                final Card foundCard = adapter.getCard(index);
                //noinspection ConstantConditions
                if (foundCard != null && foundCard.getTag().equals(card.getTag())) {
                    adapter.remove(foundCard, false);
                    break;
                }
            }
            if (atStart) {
                mListView.addAtStart(card);
            } else {
                mListView.add(card);
            }
        }
    }

    @Override
    public void clear() {
        mListView.clearAll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
