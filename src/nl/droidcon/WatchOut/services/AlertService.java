package nl.droidcon.WatchOut.services;

import android.app.IntentService;
import android.content.Intent;
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
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        Log.d("AlertService", "handle intent");

        double btcUsdNew = 0,
                btcUsdLast = 0;

        for (int i = 0; i < 10; i++){

            try {
                btcUsdLast = btcUsdNew;
                btcUsdNew = readBtcUsd();

                createNotification(btcUsdNew);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }





        /*Toast.makeText(getApplicationContext(),  "test", Toast.LENGTH_SHORT).show();
        long endTime = System.currentTimeMillis() + 2*1000;
        while (System.currentTimeMillis() < endTime) {
                try {
                    wait(endTime - System.currentTimeMillis());
                    Toast.makeText(getApplicationContext(),  "test", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
            }
        }    */
        //stopSelf();
    }

    private double readBtcUsd(){
        JSONObject jsonBtcUsd = readJsonFromUrl("https://btc-e.com/api/2/btc_usd/ticker");
        if (jsonBtcUsd != null){
            try {
                jsonBtcUsd = jsonBtcUsd.getJSONObject("ticker");
                return jsonBtcUsd.getDouble("sell");
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

            }
        }
        return 0;
            //System.out.printf("BTC/USD = %.3f\n", btcUsd);
    }

    private void createNotification(double btcUsd){
        Intent intentN = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intentN, 0);

        String btcUsdString = String.format("%.2f", btcUsd);
        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Bitcoin/USD")
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
        } finally {
            super.onDestroy();
        }
    }
}
