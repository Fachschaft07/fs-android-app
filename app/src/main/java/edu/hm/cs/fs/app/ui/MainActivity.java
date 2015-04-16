package edu.hm.cs.fs.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import java.util.List;

import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.PresenceHelper;
import edu.hm.cs.fs.app.datastore.model.Job;
import edu.hm.cs.fs.app.datastore.model.News;
import edu.hm.cs.fs.app.datastore.model.Presence;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardDetailFragment;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardListFragment;
import edu.hm.cs.fs.app.ui.info.InfoFragment;
import edu.hm.cs.fs.app.ui.job.JobDetailFragment;
import edu.hm.cs.fs.app.ui.job.JobListFragment;
import edu.hm.cs.fs.app.ui.mensa.MealFragment;
import edu.hm.cs.fs.app.ui.mvv.MvvFragment;
import edu.hm.cs.fs.app.ui.presence.PresenceFragment;
import edu.hm.cs.fs.app.ui.timetable.TimetableFragment;
import edu.hm.cs.fs.app.util.multipane.FragmentMultiPane;
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
        header.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.logo_hm, 0, 0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                header.setText(nextProverb());
            }
        });
        setDrawerHeaderCustom(header);

        // ==========================================================================
        // Everything the student needs for his study
        addSection(newSection(
                getString(R.string.blackboard),
                R.drawable.ic_assignment_grey600_24dp,
                FragmentMultiPane.build(
                        BlackBoardListFragment.class,
                        BlackBoardDetailFragment.class,
                        News.class
                )
        ));
        addSection(newSection(
                getString(R.string.timetable),
                R.drawable.ic_view_week_grey600_24dp,
                new TimetableFragment()
        ));
        //addSection(newSection(getString(R.string.news), R.drawable.ic_bookmark_outline_grey600_24dp, new Fragment()));
        //addSection(newSection(getString(R.string.roomsearch), R.drawable.ic_search_grey600_24dp, new Fragment()));

        // ==========================================================================
        // Others
        addSubheader(getString(R.string.others));
        final MaterialSection mensaSection = newSection(
                getString(R.string.food),
                R.drawable.ic_local_restaurant_grey600_24dp,
                new MealFragment()
        );
        mensaSection.setSectionColor(getResources().getColor(R.color.mensa));
        addSection(mensaSection);
        addSection(newSection(
                getString(R.string.work_offer),
                R.drawable.ic_work_grey600_24dp,
                FragmentMultiPane.build(
                        JobListFragment.class,
                        JobDetailFragment.class,
                        Job.class
                )
        ));
        final MaterialSection mvvSection = newSection(
                getString(R.string.mvv),
                R.drawable.ic_directions_transit_grey600_24dp,
                new MvvFragment()
        );
        mvvSection.setSectionColor(getResources().getColor(R.color.mvv));
        addSection(mvvSection);

        // ==========================================================================
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

        // Listen to the drawer to update the presence item
        setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(final View drawerView, final float slideOffset) {
            }

            @Override
            public void onDrawerOpened(final View drawerView) {
                refresh();
            }

            @Override
            public void onDrawerClosed(final View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(final int newState) {
            }
        });
    }

    public void updatePresenceColor(int memberCount, int colorId) {
        presenceSection.setSectionColor(getResources().getColor(colorId));
        presenceSection.setNotifications(memberCount);
    }

    private void refresh() {
        PresenceHelper.listAll(this, new Callback<List<Presence>>() {
            @Override
            public void onResult(final List<Presence> result) {
                int sectionColorId;
                if (PresenceHelper.isPresent(result)) {
                    sectionColorId = R.color.presence_available;
                } else {
                    sectionColorId = R.color.presence_busy;
                }
                updatePresenceColor(result.size(), sectionColorId);
            }
        });
    }

    private String nextProverb() {
        final String[] stringArray = getResources().getStringArray(R.array.proverbs);
        return stringArray[((int) (Math.random() * stringArray.length))];
    }
}
