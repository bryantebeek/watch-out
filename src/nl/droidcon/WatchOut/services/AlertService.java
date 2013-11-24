package nl.droidcon.WatchOut.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import nl.droidcon.WatchOut.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class AlertService extends IntentService{

    private WatchOutApplication app;

    //private SharedPreferences preferences;
    // SharedPreferences.Editor editor;

    public AlertService() {
        super("AlertService");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("AlertService", "Service created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("AlertService", "handle intent");
        app = (WatchOutApplication)getApplication();
        //editor = preferences.edit();

        //double changeThreshold = Double.parseDouble(preferences.getString(WatchOutApplication.CHANGE_THRESHOLD, "0.1"));
        //int updateTime = preferences.getInt(WatchOutApplication.UPDATE_TIME, 10000);
        double changeThreshold = app.getBtcChangeThreshold();
        int updateTime = app.getUpdateTime();

        app.setAlertServiceRunning(true);
        double btcUsdNew = 0,
                btcUsdLast = 0;

        while (true){
            if (app.isStopService()){
                Log.e("Service" , "stop");
                app.setAlertServiceRunning(false);
                stopSelf();

            }
            Log.e("Service" , "busy");
            try {
                btcUsdLast = btcUsdNew;
                btcUsdNew = readBtcUsd();

                if (btcUsdLast != 0 && btcUsdNew != 0){
                    double btcUsdChange = ((btcUsdNew - btcUsdLast) / btcUsdLast) * 100;
                    if (btcUsdChange > (changeThreshold * -1) || btcUsdChange > changeThreshold){
                            createNotification(btcUsdChange);
                    }
                    else {
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(0);
                    }
                    Log.e("Service" , btcUsdChange + " " + changeThreshold);
                }
                Thread.sleep(updateTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //stopSelf();
    }

    private double readBtcUsd(){
        JSONObject jsonBtcUsd = readJsonFromUrl("http://vps.bryantebeek.nl/");
        if (jsonBtcUsd != null){
            try {
                jsonBtcUsd = jsonBtcUsd.getJSONObject("ticker");
                return jsonBtcUsd.getDouble("last");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private void createNotification(double btcUsd){
        Intent intentN = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intentN, 0);

        String btcUsdString = String.format("%.2f", btcUsd);
        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle(getString(R.string.text_btc_change))
                .setContentText(btcUsdString)
                .setSmallIcon(R.drawable.ic_launcher).build();
        //.setContentIntent(pIntent)
        //.setAutoCancel(true)
        //.addAction(R.drawable.icon, "Call", pIntent)
        //.addAction(R.drawable.icon, "More", pIntent)
        //.addAction(R.drawable.icon, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    private JSONObject readJsonFromUrl(String url){
        try {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1){
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void onDestroy(){
        try {
            Log.d("TCPService", "AlertService stopped");
            app.setAlertServiceRunning(false);
        } finally {
            super.onDestroy();
        }
    }
}
