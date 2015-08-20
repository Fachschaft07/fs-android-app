package edu.hm.cs.fs.app.presenter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BasicListCardProvider;
import com.dexafree.materialList.card.provider.SmallImageCardProvider;
import com.fk07.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.hm.cs.fs.app.database.ICallback;
import edu.hm.cs.fs.app.database.error.IError;
import edu.hm.cs.fs.app.database.model.BlackBoardModel;
import edu.hm.cs.fs.app.database.model.HomeModel;
import edu.hm.cs.fs.app.database.model.ModelFactory;
import edu.hm.cs.fs.app.view.IHomeView;
import edu.hm.cs.fs.common.model.BlackboardEntry;
import edu.hm.cs.fs.common.model.Group;
import edu.hm.cs.fs.common.model.Meal;

/**
 * @author Fabio
 */
public class HomePresenter extends BasePresenter<IHomeView, HomeModel> {
    private final Context mContext;

    /**
     * @param view
     */
    public HomePresenter(Context context, IHomeView view) {
        this(context, view, ModelFactory.getHome());
    }

    /**
     * Needed for testing!
     *
     * @param context
     * @param view
     * @param model
     */
    public HomePresenter(Context context, IHomeView view, HomeModel model) {
        super(view, model);
        mContext = context;
    }

    public void loadHappenings() {
        getView().showLoading();
        getView().clear();

        final BackgroundTaskListener listener = new BackgroundTaskListener(2) {
            @Override
            public void onFinished() {
                getView().hideLoading();
            }
        };

        getModel().getNewBlackboardEntries(new ICallback<List<BlackboardEntry>>() {
            @Override
            public void onSuccess(@NonNull List<BlackboardEntry> data) {
                if (!data.isEmpty()) {
                    Card card = new Card.Builder(mContext)
                            .setDismissible()
                            .withProvider(BasicListCardProvider.class)
                            .setTitle(R.string.blackboard)
                            .setDescription(R.string.blackboard_current)
                            .setAdapter(new ArrayAdapter<BlackboardEntry>(
                                    mContext,
                                    R.layout.listitem_blackboard,
                                    R.id.textTitle,
                                    data) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    final View view = super.getView(position, convertView, parent);
                                    final TextView textTitle = (TextView) view.findViewById(R.id.textTitle);
                                    final BlackboardEntry item = getItem(position);
                                    textTitle.setText(item.getSubject());
                                    final TextView textAuthor = (TextView) view.findViewById(R.id.textAuthor);
                                    textAuthor.setText(item.getAuthor().getTitle() + " " + item.getAuthor().getLastName());
                                    final TextView textGroups = (TextView) view.findViewById(R.id.textGroups);
                                    final List<Group> groups = item.getGroups();
                                    if (!groups.isEmpty()) {
                                        textGroups.setText(groups.toString().substring(1, groups.toString().length() - 1));
                                    }
                                    return view;
                                }
                            })
                            .setDividerVisible(true)
                            .endConfig()
                            .build();
                    getView().showContent(card);
                }
                listener.finished();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
            }
        });

        getModel().getMealsOfToday(new ICallback<List<Meal>>() {
            @Override
            public void onSuccess(@NonNull List<Meal> data) {
                if(!data.isEmpty()) {
                    Card card = new Card.Builder(mContext)
                            .setDismissible()
                            .withProvider(BasicListCardProvider.class)
                            .setTitle(R.string.mensa)
                            .setDescription(R.string.mensa_today)
                            .setAdapter(new ArrayAdapter<Meal>(
                                    mContext,
                                    R.layout.listitem_meal,
                                    R.id.textView,
                                    data) {
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
                                            .fontSize(25)
                                            .useFont(Typeface.DEFAULT_BOLD)
                                            .endConfig()
                                            .buildRound(meal.getType().toString(), color);

                                    final ImageView image = (ImageView) view.findViewById(R.id.imageView);
                                    image.setImageDrawable(drawable);
                                    final TextView text = (TextView) view.findViewById(R.id.textView);
                                    text.setText(meal.getName());

                                    return view;
                                }
                            })
                            .setDividerVisible(true)
                            .endConfig()
                            .build();
                    getView().showContent(card);
                }
                listener.finished();
            }

            @Override
            public void onError(@NonNull IError error) {
                getView().showError(error);
            }
        });
    }

    private abstract class BackgroundTaskListener {
        private int mTasks;

        public BackgroundTaskListener(final int tasks) {
            mTasks = tasks;
        }

        public synchronized void finished() {
            mTasks--;
            if(mTasks == 0) {
                onFinished();
            }
        }

        public abstract void onFinished();
    }
}
