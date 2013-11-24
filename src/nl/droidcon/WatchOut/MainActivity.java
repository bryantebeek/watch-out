package nl.droidcon.WatchOut;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class MainActivity extends Activity {

    Trader trader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        trader = new Trader();

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
            String averagePrice = ticker.getString("avg");

            Toast.makeText(this, averagePrice, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}