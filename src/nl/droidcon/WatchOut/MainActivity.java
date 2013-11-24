package nl.droidcon.WatchOut;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

    Trader trader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trader = new Trader();

        Toast.makeText(this, "Test toast", Toast.LENGTH_SHORT).show();
    }
}
