package nl.droidcon.WatchOut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class ControlServiceActivity extends MainActivity {

    private WatchOutApplication app;

    //private SharedPreferences preferences;
    //private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_service_control);

        app = (WatchOutApplication)getApplication();
        //editor = preferences.edit();

        final Button startStopButton = (Button) findViewById(R.id.controlServiceButton);
        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app.isAlertServiceRunning()){
                    app.setStopService(true);
                    //editor.putBoolean(WatchOutApplication.STOP_SERVICE, true);
                    //editor.commit();
                    startStopButton.setText(R.string.button_start_service);
                    Intent intent = new Intent(getApplicationContext(), nl.droidcon.WatchOut.services.AlertService.class);
                    stopService(intent);
                }
                else {
                    app.setStopService(false);
                    app.setBtcChangeThreshold(0.1);
                    app.setUpdateTime(10000);
                    //editor.putString(WatchOutApplication.CHANGE_THRESHOLD, "0.1");
                    //editor.putInt(WatchOutApplication.UPDATE_TIME, 10000);
                    //editor.commit();
                    startStopButton.setText(R.string.button_stop_service);
                    Intent intent = new Intent(getApplicationContext(), nl.droidcon.WatchOut.services.AlertService.class);
                    startService(intent);
                }
            }
        });
    }
}
