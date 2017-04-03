package hu.elte.ordogfioka.eltemeter.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import hu.elte.ordogfioka.eltemeter.R;
import hu.elte.ordogfioka.eltemeter.Service.ConnectionInterface;
import hu.elte.ordogfioka.eltemeter.Service.SensorInterface;
import hu.elte.ordogfioka.eltemeter.Service.SensorService;

public class MainActivity extends AppCompatActivity implements SensorInterface{
    private TextView textView = null;
    private ConnectionInterface mService = null;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,IBinder service) {
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
        if(mBound){
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
        Intent intent = new Intent(this,SensorService.class);
        startService(intent);
        intent = new Intent(this, SensorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public void stopService(View view) {
        if(mBound){
            mService.unregisterListener(MainActivity.this);
            mBound = false;
        }
        unbindService(mConnection);
        Intent intent = new Intent(this,SensorService.class);
        stopService(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        textView.append(sensorEvent.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        textView.append("AccuracyChanged:" + sensor.toString()+"\n");
    }

    @Override
    public void onLocationChanged(Location location) {
        textView.append("Accuracy: "+location.getAccuracy() +
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
}
