package hu.elte.ordogfioka.eltemeter.Service;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import hu.elte.ordogfioka.eltemeter.View.MainActivity;

/**
 * Created by Attila on 2017. 03. 18..
 */

public class UpdateUI implements SensorInterface {
    private Context myContext = null;

    public UpdateUI(Context context){
        myContext = context;
    }

    public void updateUI(String status){
        Intent intent = new Intent(MainActivity.RECEIVE_STATUS);
        intent.putExtra("locationStatus", status);
        LocalBroadcastManager.getInstance(myContext).sendBroadcast(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        updateUI(sensorEvent.toString());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        updateUI(sensor.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        updateUI(location.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        updateUI(s);
    }

    @Override
    public void onProviderEnabled(String s) {
        updateUI(s);
    }

    @Override
    public void onProviderDisabled(String s) {
        updateUI(s);
    }
}
