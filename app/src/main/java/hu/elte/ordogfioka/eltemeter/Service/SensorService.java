package hu.elte.ordogfioka.eltemeter.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Attila on 2017. 03. 07..
 */

public class SensorService extends Service implements SensorInterface {
    private static final String logname = "SensorService";
    private LocationManager lm;
    private UpdateUI updateUI;
    List<SensorInterface> listeners = new ArrayList<>();

    @Override
    public void onCreate() {
        Log.i(logname, "onCreate");
        Toast.makeText(this,"Service Started",Toast.LENGTH_LONG).show();
        super.onCreate();

        updateUI = new UpdateUI(this);

        registerListener(updateUI);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You don't have permission for GPS position!", Toast.LENGTH_LONG).show();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(logname,"onDestroy");
        Toast.makeText(this,"Service stopped",Toast.LENGTH_LONG).show();
        if (lm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You don't have permission for GPS position!", Toast.LENGTH_LONG).show();
                return;
            }
            lm.removeUpdates(this);
        }
        unregisterListener(updateUI);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i(logname,sensorEvent.toString());
        Toast.makeText(this,sensorEvent.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.i(logname,sensor.toString() +" accuaracy: "+ i);
        for(SensorInterface si : listeners){
            si.onAccuracyChanged(sensor,i);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(logname,location.toString());
        for(SensorInterface si : listeners){
            si.onLocationChanged(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        for(SensorInterface si : listeners){
            si.onStatusChanged(s,i,bundle);
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        for(SensorInterface si : listeners){
            si.onProviderEnabled(s);
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        for (SensorInterface si : listeners){
            si.onProviderDisabled(s);
        }
    }

    public void registerListener(SensorInterface listener) {
        listeners.add(listener);
    }

    public void unregisterListener(SensorInterface  listener) {
        listeners.remove(listener);
    }
}
