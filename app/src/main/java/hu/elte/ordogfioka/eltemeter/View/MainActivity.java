package hu.elte.ordogfioka.eltemeter.View;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import hu.elte.ordogfioka.eltemeter.R;
import hu.elte.ordogfioka.eltemeter.Service.SensorInterface;
import hu.elte.ordogfioka.eltemeter.Service.SensorService;

public class MainActivity extends AppCompatActivity implements SensorInterface{
    SensorService mService;
    boolean mBound = false;
    private static final String logname = "MainActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
    }
    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SensorService.LocalBinder binder = (SensorService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.registerListener(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void startService(View view) {super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, SensorService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    public void stopService(View view) {
        if(mService != null){
            unbindService(mConnection);
        }
        Intent serviceIntent = new Intent(this,SensorService.class);
        stopService(serviceIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(logname,"onLocationChanged");
        textView.append(location.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.i(logname,"onStatusChanged");
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i(logname,"onProviderEnabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i(logname,"onProviderDisabled");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i(logname,"onSensorChanged");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.i(logname,"onAccuracyChanged");
    }
}
