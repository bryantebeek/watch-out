package nl.droidcon.WatchOut;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrderbookActivity extends Activity {

    ListView asks, bids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orderbook);

        asks = (ListView) findViewById(R.id.asks);
        bids = (ListView) findViewById(R.id.bids);

        new RequestTask().execute("http://vps.bryantebeek.nl/trades.php");
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

    public void update(String response) {
        ArrayList<String> _asks_string = new ArrayList<String>();
        ArrayList<String> _bids_string = new ArrayList<String>();

        HashMap<Double, Double> _asks = new HashMap<Double, Double>();
        HashMap<Double, Double> _bids = new HashMap<Double, Double>();

        try {
            JSONArray trades = new JSONArray(response);

            for (int i = 0; i < trades.length(); i++) {
                JSONObject trade = trades.getJSONObject(i);
                double amount = trade.getDouble("amount");
                double price = trade.getDouble("price");

                String type = trade.getString("trade_type");

                if (type.equals("ask")) {
                    if (_asks.containsKey(price)) {
                        _asks.put(price, _asks.get(price) + amount);
                    } else {
                        _asks.put(price, amount);
                    }
                } else {
                    if (_bids.containsKey(price)) {
                        _bids.put(price, _bids.get(price) + amount);
                    } else {
                        _bids.put(price, amount);
                    }
                }
            }

            for (Map.Entry<Double, Double> _ask : _asks.entrySet()) {
                _asks_string.add(_ask.getKey() + " - " + String.format("%.2f", _ask.getValue()));
            }

            for (Map.Entry<Double, Double> _bid : _bids.entrySet()) {
                _bids_string.add(_bid.getKey() + " - " + String.format("%.2f", _bid.getValue()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(_asks_string);
        Collections.sort(_bids_string);

        asks.setAdapter(new ArrayAdapter<String>(this,
                R.layout.orderbook_list_item, R.id.order, _asks_string));

        bids.setAdapter(new ArrayAdapter<String>(this,
                R.layout.orderbook_list_item, R.id.order, _bids_string));
    }
}
