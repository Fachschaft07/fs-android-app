package edu.hm.cs.fs.app.ui.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.HomePresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IHomeView;

/**
 * Created by FHellman on 12.08.2015.
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

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
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
