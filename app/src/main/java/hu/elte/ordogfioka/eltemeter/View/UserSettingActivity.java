package hu.elte.ordogfioka.eltemeter.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.Map;

import hu.elte.ordogfioka.eltemeter.R;


/**
 * Created by AMakoviczki on 2017. 05. 14..
 */

public class UserSettingActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new UserSettingFragment()).commit();
    }

    public static class UserSettingFragment extends PreferenceFragment{
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
