package edu.hm.cs.fs.app.ui.info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.hm.cs.fs.app.presenter.InfoPresenter;
import edu.hm.cs.fs.app.util.BaseFragment;
import edu.hm.cs.fs.app.view.IInfoView;

/**
 * @author Fabio
 */
public class InfoFragment extends BaseFragment<InfoPresenter> implements IInfoView {
    @Bind(R.id.textViewVersion)
    TextView mVersion;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setPresenter(new InfoPresenter(getActivity(), this));
        getPresenter().loadVersion();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_info;
    }

    @Override
    public void showVersion(@NonNull final String version) {
        mVersion.setText(mVersion.getText() + " " + version);
    }
}
