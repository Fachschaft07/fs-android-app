package edu.hm.cs.fs.app.ui.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.InfoPresenter;
import edu.hm.cs.fs.app.ui.BaseFragment;
import edu.hm.cs.fs.app.view.IInfoView;

/**
 * @author Fabio
 */
public class InfoFragment extends BaseFragment<InfoPresenter> implements IInfoView {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.textViewVersion)
    TextView mVersion;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(getMainActivity().getToolbar().getNavigationIcon());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openDrawer();
            }
        });

        setPresenter(new InfoPresenter(getActivity(), this));
        getPresenter().loadVersion();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_info;
    }

    @Override
    public boolean hasCustomToolbar() {
        return true;
    }

    @Override
    public void showVersion(@NonNull final String version) {
        mVersion.setText(mVersion.getText() + " " + version);
    }
}
