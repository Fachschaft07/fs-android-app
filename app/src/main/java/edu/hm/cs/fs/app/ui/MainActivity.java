package edu.hm.cs.fs.app.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fk07.R;

import edu.hm.cs.fs.app.datastore.helper.Callback;
import edu.hm.cs.fs.app.datastore.helper.PresenceHelper;
import edu.hm.cs.fs.app.ui.blackboard.BlackBoardFragment;
import edu.hm.cs.fs.app.ui.info.InfoFragment;
import edu.hm.cs.fs.app.ui.presence.PresenceFragment;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class MainActivity extends MaterialNavigationDrawer<Fragment> {
    private MaterialSection presenceSection;

	@Override
	public void init(final Bundle bundle) {
		addSection(newSection(getString(R.string.blackboard), new BlackBoardFragment()));
		addSection(newSection(getString(R.string.news), new Fragment()));
		addSection(newSection(getString(R.string.timetable), new Fragment()));
		addSection(newSection(getString(R.string.roomsearch), new Fragment()));
		addSection(newSection(getString(R.string.mvv), new Fragment()));
		addSection(newSection(getString(R.string.food), new Fragment()));
        addDivisor();
        presenceSection = newSection(
                getString(R.string.presence),
                R.drawable.circle_yellow_light,
                new PresenceFragment()
        );
        addBottomSection(presenceSection);
        addBottomSection(newSection(getString(R.string.info), new InfoFragment()));
	}

    @Override
    protected void onResume() {
        super.onResume();

        PresenceHelper.isPresent(this, new Callback<Boolean>() {
            @Override
            public void onResult(final Boolean result) {
                int drawableId;
                if (result) {
                    drawableId = R.drawable.circle_green_dark;
                } else {
                    drawableId = R.drawable.circle_yellow_light;
                }
                presenceSection.setIcon(getResources().getDrawable(drawableId));
            }
        });
    }
}
