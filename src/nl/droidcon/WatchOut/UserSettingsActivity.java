package nl.droidcon.WatchOut;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;

/**
 * Created with IntelliJ IDEA.
 * User: Rob
 * Date: 11/24/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserSettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        addPreferencesFromResource(R.xml.settings);

    }
}
