package edu.hm.cs.fs.app.ui.home;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.fk07.R;

/**
 * @author Fabio
 */
public class HomePreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences_home);
    }
}
