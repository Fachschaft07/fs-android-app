package edu.hm.cs.fs.app.presenter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.fk07.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import edu.hm.cs.fs.app.ui.MainActivity;
import edu.hm.cs.fs.app.ui.PerActivity;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardDetailFragment;
import edu.hm.cs.fs.app.ui.home.CardProviderNextLesson;
import edu.hm.cs.fs.app.ui.home.HomeView;
import edu.hm.cs.fs.app.ui.lostfound.LostFoundListFragment;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Exam;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Holiday;
import edu.hm.cs.fs.common.model.Lesson;
import edu.hm.cs.fs.common.model.Meal;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

@PerActivity
public class HomePresenter extends BasePresenter<HomeView> {
    private static final String HOLIDAY = "holiday";
    private static final String NEXT_LESSON = "next_lesson";
    private static final String BLACKBOARD = "blackboard";
    private static final String MENSA = "meals";
    private static final String LOSTFOUND = "lostfound";
    private static final String FS_NEWS = "student_council";
    private static final String FS_NEWS_ITEM = "student_council_";
    private static final String EXAMS = "exams";

    @Inject
    SharedPreferences mPrefs;

    @Inject
    public HomePresenter() {
    }

    public void loadHappenings() {
        if (checkSubscriber()) {
            return;
        }
        getView().clear();
        getView().showLoading();

        final Observable<Card> cardObservable = Observable.create(new Observable.OnSubscribe<Card>() {
            @Override
            public void call(Subscriber<? super Card> subscriber) {
                Observable.concat(
                        //##################################################################################
                        // Next Lesson
                        getModel().nextLesson()
                                .flatMap(new Func1<Lesson, Observable<Card>>() {
                                    @Override
                                    public Observable<Card> call(Lesson lesson) {
                                        final Calendar lessonStart = Calendar.getInstance();
                                        lessonStart.set(Calendar.DAY_OF_WEEK, lesson.getDay().getCalendarId());
                                        lessonStart.set(Calendar.HOUR_OF_DAY, lesson.getHour());
                                        lessonStart.set(Calendar.MINUTE, lesson.getMinute());

                                        final Calendar lessonEnd = Calendar.getInstance();
                                        lessonEnd.setTimeInMillis(lessonStart.getTimeInMillis());
                                        lessonEnd.add(Calendar.MINUTE, 90);

                                        final Card card = new Card.Builder(getContext())
                                                .setTag(NEXT_LESSON)
                                                .setDismissible()
                                                .withProvider(new CardProviderNextLesson())
                                                .setLayout(R.layout.card_next_lesson)
                                                .setTitle(lesson.getModule().getName())
                                                .setSubtitle(lesson.getTeacher().getName())
                                                .setSubtitleColor(Color.GRAY)
                                                .setTime(String.format(Locale.getDefault(), "%1$ta %1$tH:%1$tM - %2$tH:%2$tM",
                                                        lessonStart, lessonEnd))
                                                .setPlace(lesson.getRoom())
                                                .endConfig()
                                                .build();
                                        return Observable.just(card);
                                    }
                                }),
                        //##################################################################################
                        // Blackboard
                        getModel().blackboardEntriesSinceYesterday(true)
                                .collect(new Func0<ArrayList<BlackboardEntry>>() {
                                    @Override
                                    public ArrayList<BlackboardEntry> call() {
                                        return new ArrayList<>();
                                    }
                                }, new Action2<ArrayList<BlackboardEntry>, BlackboardEntry>() {
                                    @Override
                                    public void call(ArrayList<BlackboardEntry> blackboardEntries, BlackboardEntry blackboardEntry) {
                                        blackboardEntries.add(blackboardEntry);
                                    }
                                })
                                .filter(new Func1<ArrayList<BlackboardEntry>, Boolean>() {
                                    @Override
                                    public Boolean call(ArrayList<BlackboardEntry> data) {
                                        return !data.isEmpty();
                                    }
                                })
                                .flatMap(new Func1<ArrayList<BlackboardEntry>, Observable<Card>>() {
                                    @Override
                                    public Observable<Card> call(ArrayList<BlackboardEntry> blackboardEntries) {
                                        final Card card = new Card.Builder(getContext())
                                                .setTag(BLACKBOARD)
                                                .setDismissible()
                                                .withProvider(new ListCardProvider())
                                                .setLayout(R.layout.material_list_card_layout)
                                                .setTitle(R.string.blackboard)
                                                .setAdapter(new ArrayAdapter<BlackboardEntry>(
                                                        getContext(), R.layout.listitem_blackboard, R.id.textTitle, blackboardEntries) {
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
                                        return Observable.just(card);
                                    }
                                }),
                        //##################################################################################
                        // Meals
                        getModel().mealsOfToday(true)
                                .collect(new Func0<ArrayList<Meal>>() {
                                    @Override
                                    public ArrayList<Meal> call() {
                                        return new ArrayList<>();
                                    }
                                }, new Action2<ArrayList<Meal>, Meal>() {
                                    @Override
                                    public void call(ArrayList<Meal> meals, Meal meal) {
                                        meals.add(meal);
                                    }
                                })
                                .filter(new Func1<ArrayList<Meal>, Boolean>() {
                                    @Override
                                    public Boolean call(ArrayList<Meal> data) {
                                        return !data.isEmpty();
                                    }
                                })
                                .flatMap(new Func1<ArrayList<Meal>, Observable<Card>>() {
                                    @Override
                                    public Observable<Card> call(ArrayList<Meal> meals) {
                                        final Card card = new Card.Builder(getContext())
                                                .setTag(MENSA)
                                                .setDismissible()
                                                .withProvider(new ListCardProvider())
                                                .setLayout(R.layout.material_list_card_layout)
                                                .setTitle(R.string.mensa)
                                                .setDescription(R.string.mensa_today)
                                                .setAdapter(new ArrayAdapter<Meal>(
                                                        getContext(), R.layout.listitem_meal, R.id.textView, meals) {
                                                    @Override
                                                    public View getView(int position, View convertView, ViewGroup parent) {
                                                        final View view = super.getView(position, convertView, parent);

                                                        final Meal meal = getItem(position);

                                                        int color;
                                                        switch (meal.getType()) {
                                                            case VEGAN:
                                                                color = getContext().getResources().getColor(R.color.meal_vegan);
                                                                break;
                                                            case MEATLESS:
                                                                color = getContext().getResources().getColor(R.color.meal_meatless);
                                                                break;
                                                            default:
                                                                color = getContext().getResources().getColor(R.color.meal_meat);
                                                                break;
                                                        }
                                                        TextDrawable drawable = TextDrawable.builder()
                                                                .beginConfig()
                                                                .withBorder(2)
                                                                .fontSize(getContext().getResources()
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
                                        return Observable.just(card);
                                    }
                                }),
                        //##################################################################################
                        // Lost & Found
                        getModel().lostfound(true).count()
                                .filter(new Func1<Integer, Boolean>() {
                                    @Override
                                    public Boolean call(Integer integer) {
                                        return integer <= 0;
                                    }
                                })
                                .flatMap(new Func1<Integer, Observable<Card>>() {
                                    @Override
                                    public Observable<Card> call(Integer amountOfLostFound) {
                                        final Card card = new Card.Builder(getContext())
                                                .setTag(LOSTFOUND)
                                                .setDismissible()
                                                .withProvider(new CardProvider())
                                                .setLayout(R.layout.card_simple_text_button)
                                                .setTitle(R.string.lostfound)
                                                .setDescription(getContext().getResources()
                                                        .getQuantityString(R.plurals.lostfound_description,
                                                                amountOfLostFound, amountOfLostFound))
                                                .setDrawable(R.drawable.ic_loyalty_grey_600_24dp)
                                                .addAction(R.id.right_text_button, new TextViewAction(getContext())
                                                        .setText(getContext().getString(R.string.view))
                                                        .setTextResourceColor(R.color.colorAccent)
                                                        .setListener(new OnActionClickListener() {
                                                            @Override
                                                            public void onActionClicked(final View view, final Card card) {
                                                                MainActivity.getNavigator().goTo(new LostFoundListFragment());
                                                            }
                                                        }))
                                                .endConfig()
                                                .build();
                                        return Observable.just(card);
                                    }
                                }),
                        //##################################################################################
                        // Holiday
                        getModel().nextHolidays(true)
                                .flatMap(new Func1<Holiday, Observable<Card>>() {
                                    @Override
                                    public Observable<Card> call(Holiday holiday) {
                                        final Calendar calendarToday = Calendar.getInstance();

                                        final long timeDiff = holiday.getStart().getTime() - calendarToday.getTimeInMillis();
                                        final int daysLeft = (int) (timeDiff / (1000 * 60 * 60 * 24));

                                        final Card card = new Card.Builder(getContext())
                                                .setTag(HOLIDAY)
                                                .setDismissible()
                                                .withProvider(new CardProvider())
                                                .setLayout(R.layout.material_small_image_card)
                                                .setTitle(holiday.getName())
                                                .setDescription(getContext().getResources().getQuantityString(R.plurals.next_holidays,
                                                        daysLeft, daysLeft, holiday.getName(),
                                                        holiday.getStart(), holiday.getEnd()))
                                                .setDrawable(R.drawable.ic_flight_orange_800_36dp)
                                                .endConfig()
                                                .build();
                                        return Observable.just(card);
                                    }
                                }),
                        //##################################################################################
                        // FS News
                /*
                getModel().fsNewsTop3(true)
                        .collect(new Func0<ArrayList<News>>() {
                            @Override
                            public ArrayList<News> call() {
                                return new ArrayList<>();
                            }
                        }, new Action2<ArrayList<News>, News>() {
                            @Override
                            public void call(ArrayList<News> newsList, News news) {
                                newsList.add(news);
                            }
                        })
                        .filter(new Func1<ArrayList<News>, Boolean>() {
                            @Override
                            public Boolean call(ArrayList<News> data) {
                                return !data.isEmpty();
                            }
                        })
                .flatMap(new Func1<ArrayList<News>, Observable<Card>>() {
                    @Override
                    public Observable<Card> call(ArrayList<News> newses) {
                        return Observable.from(newses)
                                .flatMap(new Func1<News, Observable<Card>>() {
                                    @Override
                                    public Observable<Card> call(final News news) {
                                        final CardProvider cardConfig = new Card.Builder(getContext())
                                                .setTag(FS_NEWS_ITEM)
                                                .setDismissible()
                                                .withProvider(new CardProvider())
                                                .setLayout(R.layout.card_simple_text_button)
                                                .setTitle(news.getTitle())
                                                .setDescription(news.getDescription());
                                        if (!TextUtils.isEmpty(news.getLink())) {
                                            cardConfig.addAction(R.id.right_text_button, new TextViewAction(getContext())
                                                    .setText(getContext().getString(R.string.read_more))
                                                    .setTextResourceColor(R.color.colorAccent)
                                                    .setListener(new OnActionClickListener() {
                                                        @Override
                                                        public void onActionClicked(final View view, final Card card) {
                                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                                            intent.setData(Uri.parse(news.getLink()));
                                                            mActivity.startActivity(intent);
                                                        }
                                                    }));
                                        }
                                        final Card card = cardConfig.endConfig().build();
                                        return Observable.just(card);
                                    }
                                });
                    }
                }),
                */
                        //##################################################################################
                        // Exams
                        getModel().examsOfUser()
                                .collect(new Func0<ArrayList<Exam>>() {
                                    @Override
                                    public ArrayList<Exam> call() {
                                        return new ArrayList<>();
                                    }
                                }, new Action2<ArrayList<Exam>, Exam>() {
                                    @Override
                                    public void call(ArrayList<Exam> examList, Exam exam) {
                                        examList.add(exam);
                                    }
                                })
                                .filter(new Func1<ArrayList<Exam>, Boolean>() {
                                    @Override
                                    public Boolean call(ArrayList<Exam> exams) {
                                        return !exams.isEmpty();
                                    }
                                })
                                .flatMap(new Func1<ArrayList<Exam>, Observable<Card>>() {
                                    @Override
                                    public Observable<Card> call(ArrayList<Exam> exams) {
                                        final Card card = new Card.Builder(getContext())
                                                .setTag(EXAMS)
                                                .setDismissible()
                                                .withProvider(new ListCardProvider())
                                                .setLayout(R.layout.material_list_card_layout)
                                                .setBackgroundResourceColor(R.color.exam)
                                                .setTitle(R.string.exams)
                                                .setTitleColor(Color.WHITE)
                                                .setAdapter(new ArrayAdapter<Exam>(
                                                        getContext(), R.layout.listitem_exam_card, R.id.textTitle, exams) {
                                                    @Override
                                                    public View getView(int position, View convertView, ViewGroup parent) {
                                                        final View view = super.getView(position, convertView, parent);

                                                        final Exam exam = getItem(position);

                                                        final TextView textTitle = (TextView) view.findViewById(R.id.textTitle);
                                                        textTitle.setText(exam.getModule().getName());

                                                        final RelativeTimeTextView daysLeft = (RelativeTimeTextView) view.findViewById(R.id.textTimeLeft);
                                                        daysLeft.setReferenceTime(exam.getDate().getTime());
                                                        StringBuilder rooms = new StringBuilder();
                                                        int index = 0;
                                                        for (String room : exam.getRooms()) {
                                                            if (index++ > 0) {
                                                                rooms.append(", ");
                                                            }
                                                            rooms.append(room);
                                                        }
                                                        daysLeft.setSuffix(" in " + rooms);

                                                        return view;
                                                    }
                                                })
                                                .endConfig()
                                                .build();
                                        return Observable.just(card);
                                    }
                                })
                )
                        .filter(new Func1<Card, Boolean>() {
                            @Override
                            public Boolean call(Card card) {
                                // Is Card active
                                return mPrefs.getBoolean(card.getTag().toString(), true);
                            }
                        })
                        .subscribe(subscriber);
            }
        });

        setSubscriber(cardObservable.subscribe(new BasicSubscriber<Card>(getView()) {
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                super.onError(e);
            }

            @Override
            public void onNext(Card card) {
                getView().showCard(card);
            }
        }));
    }
}
