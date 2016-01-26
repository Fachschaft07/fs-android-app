package edu.hm.cs.fs.app.ui.info;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.fk07.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Fabio
 */
public class InfoActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.textViewVersion)
    TextView mVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            final String version = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            mVersion.setText(getString(R.string.version, version));
        } catch (final PackageManager.NameNotFoundException e) {
            mVersion.setText(getString(R.string.version, "?????"));
        }
    }
}
