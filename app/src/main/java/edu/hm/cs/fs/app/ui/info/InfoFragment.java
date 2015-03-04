package edu.hm.cs.fs.app.ui.info;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fk07.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Fabio on 04.03.2015.
 */
public class InfoFragment extends Fragment {
    @InjectView(R.id.textViewVersion)
    TextView mVersion;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            final String versionName = getActivity()
                    .getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0)
                    .versionName;
            mVersion.setText(mVersion.getText() + " " + versionName);
        } catch (final PackageManager.NameNotFoundException e) {
            Log.e(getClass().getSimpleName(), "", e);
        }
    }

    @OnClick(R.id.textViewMail)
    public void openMail() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "app@fs.cs.hm.edu" });
        startActivity(Intent.createChooser(intent, getString(R.string.send_mail)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
