package nl.droidcon.WatchOut;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Rob
 * Date: 11/24/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
class UserSettingActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}
