package hu.elte.ordogfioka.eltemeter.Service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
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

public class SensorService extends Service implements SensorEventListener, LocationListener {
    private static final String logname = "SensorService";
    private LocationManager lm;
    private final IBinder mBinder = new LocalBinder();
    List<SensorInterface> listeners = new ArrayList<>();

    @Override
    public void onCreate() {
        Log.i(logname, "onCreate");
        super.onCreate();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You don't have permission for GPS position!", Toast.LENGTH_LONG).show();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    public void registerListener(SensorInterface listener) {
        listeners.add(listener);
    }

    public void unregisterListener(SensorInterface  listener) {
        listeners.remove(listener);
    }

    @Override
    public void onDestroy() {
        Log.i(logname,"onDestroy");
        if (lm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You don't have permission for GPS position!", Toast.LENGTH_LONG).show();
                return;
            }
            lm.removeUpdates(this);
        }
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void stopSensorService(){
        stopSelf();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i(logname,sensorEvent.toString());
        for(SensorInterface si : listeners){
            si.onSensorChanged(sensorEvent);
        }
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
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
