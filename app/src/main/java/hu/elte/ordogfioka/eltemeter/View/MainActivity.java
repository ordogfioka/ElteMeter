package hu.elte.ordogfioka.eltemeter.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import hu.elte.ordogfioka.eltemeter.Model.PreferenceValues;
import hu.elte.ordogfioka.eltemeter.R;
import hu.elte.ordogfioka.eltemeter.Service.ConnectionInterface;
import hu.elte.ordogfioka.eltemeter.Service.SensorInterface;
import hu.elte.ordogfioka.eltemeter.Service.SensorService;

public class MainActivity extends AppCompatActivity implements SensorInterface {
    private TextView textView = null;
    private ConnectionInterface mService = null;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new PlaceholderFragment())
                    .commit();
        }
        textView = (TextView) findViewById(R.id.textView);

        PreferenceValues pv = new PreferenceValues(this);
        String s = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SensorService.LocalBinder binder = (SensorService.LocalBinder) service;
            mService = binder.getService();
            mService.registerListener(MainActivity.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onPause() {
        if (mBound) {
            mService.unregisterListener(MainActivity.this);
            mBound = false;
            unbindService(mConnection);
        }
        super.onPause();
    }

    public void startService(View view) {
        startService();
    }

    private void startService() {
        Intent intent = new Intent(this, SensorService.class);
        startService(intent);
        intent = new Intent(this, SensorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopService(View view) {
        if (mBound) {
            mService.unregisterListener(MainActivity.this);
            mBound = false;
        }
        unbindService(mConnection);
        Intent intent = new Intent(this, SensorService.class);
        stopService(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        textView.append(sensorEvent.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        textView.append("AccuracyChanged:" + sensor.toString() + "\n");
    }

    @Override
    public void onLocationChanged(Location location) {
        textView.append("Accuracy: " + location.getAccuracy() +
                "\nLongitude: " + location.getLongitude() +
                "\nLatitude: " + location.getLatitude() +
                "\nSpeed(Km/h): " + (location.getSpeed() * 3.6) +
                "\nSpeed(m/s): " + location.getSpeed() +
                "\n");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        textView.append(s.toString());
    }

    @Override
    public void onProviderEnabled(String s) {
        textView.append(s);
    }

    @Override
    public void onProviderDisabled(String s) {
        textView.append(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * react to the user tapping/selecting an options menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_thingy:
                Intent i = new Intent(MainActivity.this, UserSettingActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

    }
}
