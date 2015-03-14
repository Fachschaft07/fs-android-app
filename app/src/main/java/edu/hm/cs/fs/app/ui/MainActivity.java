package edu.hm.cs.fs.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import java.util.List;

import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.PresenceHelper;
import edu.hm.cs.fs.app.datastore.model.Presence;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardDetailFragment;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardListFragment;
import edu.hm.cs.fs.app.ui.info.InfoFragment;
import edu.hm.cs.fs.app.ui.mensa.MealFragment;
import edu.hm.cs.fs.app.ui.mvv.MvvFragment;
import edu.hm.cs.fs.app.util.multipane.FragmentMultiPane;
import edu.hm.cs.fs.app.ui.presence.PresenceFragment;
import edu.hm.cs.fs.app.ui.timetable.TimetableDayActivity;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainActivity extends MaterialNavigationDrawer<Fragment> {
    private MaterialSection presenceSection;

	@Override
	public void init(final Bundle bundle) {
        // Style Toolbar
        getSupportActionBar().setElevation(10f);

		// Header View
        final TextView header = new TextView(this);
        header.setText(nextProverb());
        header.setGravity(Gravity.CENTER);
        header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        header.setBackgroundResource(R.drawable.border_background);
        header.setCompoundDrawablePadding(getResources().getDimensionPixelSize(R.dimen.textappearance_large_img_padding));
        header.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hotnews_3, 0, 0, 0);
        int padding = getResources().getDimensionPixelSize(R.dimen.textappearance_large_img_padding);
        header.setPadding(padding, padding, padding, padding);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                header.setText(nextProverb());
            }
        });
        setDrawerHeaderCustom(header);
		
		// Main Sections
		addSection(newSection(getString(R.string.blackboard), R.drawable.ic_blackboard, FragmentMultiPane.build(
                BlackBoardListFragment.class,
                BlackBoardDetailFragment.class
        )));
		//addSection(newSection(getString(R.string.news), R.drawable.ic_news, new Fragment()));
		addSection(newSection(getString(R.string.timetable), R.drawable.ic_view_week_grey600_24dp, new Intent(this, TimetableDayActivity.class)));
		//addSection(newSection(getString(R.string.roomsearch), R.drawable.ic_roomsearch, new Fragment()));
		addSection(newSection(getString(R.string.mvv), R.drawable.ic_directions_train_grey600_24dp, new MvvFragment()));
		addSection(newSection(getString(R.string.food), R.drawable.ic_local_restaurant_grey600_24dp, new MealFragment()));

		// Bottom Sections
        presenceSection = newSection(
                getString(R.string.presence),
                R.drawable.ic_people_grey600_24dp,
                new PresenceFragment()
        );
        addBottomSection(presenceSection);
        
        addBottomSection(newSection(getString(R.string.info), R.drawable.ic_info_outline_grey600_24dp, new InfoFragment()));

        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"app@fs.cs.hm.edu"});
        addBottomSection(newSection(getString(R.string.help_feedback), R.drawable.ic_local_post_office_grey600_24dp, intent));
	}

    @Override
    protected void onResume() {
        super.onResume();

        PresenceHelper.listAll(this, new Callback<List<Presence>>() {
            @Override
            public void onResult(final List<Presence> result) {
                int drawableId;
                int sectionColorId;
                if (PresenceHelper.isPresent(result)) {
                    sectionColorId = R.color.green;
                    Log.d(getClass().getSimpleName(), "Presence green");
                } else {
                    sectionColorId = R.color.yellow;
                    Log.d(getClass().getSimpleName(), "Presence yellow");
                }
                presenceSection.setSectionColor(getResources().getColor(sectionColorId));

                Log.d(getClass().getSimpleName(), "Presence count = " + result.size());
                presenceSection.setNotifications(result.size());
            }
        });
    }

    private String nextProverb() {
        final String[] stringArray = getResources().getStringArray(R.array.proverbs);
        return stringArray[((int) (Math.random() * stringArray.length))];
    }
}