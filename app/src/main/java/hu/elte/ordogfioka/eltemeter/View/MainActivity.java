package hu.elte.ordogfioka.eltemeter.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import hu.elte.ordogfioka.eltemeter.R;
import hu.elte.ordogfioka.eltemeter.Service.SensorService;

public class MainActivity extends AppCompatActivity {
    public static final String RECEIVE_STATUS = " hu.elte.ordogfioka.eltemeter.RECEIVE_STATUS";
    private TextView textView = null;
    LocalBroadcastManager localBroadcastManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVE_STATUS);
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter);

        Intent intent = new Intent(this,SensorService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    public void startService(View view) {
        Intent intent = new Intent(this,SensorService.class);
        startService(intent);
    }

    public void stopService(View view) {
        Intent intent = new Intent(this,SensorService.class);
        stopService(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(RECEIVE_STATUS)) {
                String locationStatus = intent.getStringExtra("locationStatus");
                textView.append(locationStatus);
            }
        }
    };
}
