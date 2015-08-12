package edu.hm.cs.fs.app.ui.publictransport;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fk07.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.util.BaseFragment;

/**
 * Created by FHellman on 10.08.2015.
 */
public class PublicTransportTabFragment extends BaseFragment {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mAdapter = new ViewPagerAdapter(getFragmentManager());
        mAdapter.addFrag(new LothstrFragment(), getString(R.string.lothstrasse));
        mAdapter.addFrag(new PasingFragment(), getString(R.string.pasing));
        mViewPager.setAdapter(mAdapter);

        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setTabsFromPagerAdapter(mAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        });

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_public_transport_tabs;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    protected int getTitle() {
        return R.string.mvv;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        for (int index = 0; index < mAdapter.getCount(); index++) {
            transaction.remove(mAdapter.getItem(index));
        }
        transaction.commit();
        getFragmentManager().executePendingTransactions();
    }

    private final class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
