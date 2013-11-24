package nl.droidcon.WatchOut;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

   private float last = -1;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        new RequestTask().execute("http://vps.bryantebeek.nl");
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri[0]);

            try {
                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream inputstream = entity.getContent();
                    BufferedReader bufferedreader =
                            new BufferedReader(new InputStreamReader(inputstream));
                    StringBuilder stringbuilder = new StringBuilder();

                    String currentline = null;
                    while ((currentline = bufferedreader.readLine()) != null) {
                        stringbuilder.append(currentline + "\n");
                    }

                    String result = stringbuilder.toString();
                    inputstream.close();

                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Failed!";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            update(result);
        }
    }

    public void update(String response)
    {
        try {
            JSONObject data = new JSONObject(response);
            JSONObject ticker = data.getJSONObject("ticker");
            getAndSetValues(ticker);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Intent intent = new Intent(getApplicationContext(), nl.droidcon.WatchOut.services.AlertService.class);
        //startService(intent);
    }
    
    private void getAndSetValues(JSONObject ticker) throws JSONException {
        //Bitcoin Average
        int color = Color.BLACK;
        String lastPrice = ticker.getString("last");
        if(last == -1){
            last = Float.parseFloat(lastPrice);
        }
        else{
            float previous = last;
            last =  Float.parseFloat(lastPrice);
            if(previous < last){
                //green
                color = Color.GREEN;
            }
            else if(last == previous)   {
                //black
            }
            else{
                //red
                color = Color.RED;
            }
        }
        TextView lastTV = (TextView) findViewById(R.id.lastTV);
        lastTV.setText("$" + lastPrice);
        lastTV.setTextColor(color);


        //Bitcoin Low
        String lowPrice = ticker.getString("low");
        float low = Float.parseFloat(lowPrice);
        lowPrice = String.format("%.1f", low);
        TextView lowTV = (TextView) findViewById(R.id.lowTV);
        lowTV.setText("$" + lowPrice);
        //Bitcoin High
        String highPrice = ticker.getString("high");
        float high = Float.parseFloat(highPrice);
        highPrice = String.format("%.1f", high);
        TextView highTV = (TextView) findViewById(R.id.highTV);
        highTV.setText("$" + highPrice);

    }

    public void syncButton(View v){
        new RequestTask().execute("http://vps.bryantebeek.nl");
        Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
    }

    public void launchOrderbook(View v){
        Intent intent = new Intent(this, OrderbookActivity.class);
        startActivity(intent);
    }

}
