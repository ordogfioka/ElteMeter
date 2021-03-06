package hu.elte.ordogfioka.eltemeter.Service;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.bitalino.comm.BITalinoDevice;
import com.bitalino.comm.BITalinoException;
import com.bitalino.comm.BITalinoFrame;
import com.zhaoxiaodan.miband.ActionCallback;
import com.zhaoxiaodan.miband.MiBand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Attila on 2017. 03. 07..
 */

public class SensorService extends Service implements SensorInterface,ConnectionInterface {
    private static final String logname = "SensorService";
    private LocationManager lm;
    List<SensorInterface> listeners = new ArrayList<>();
    private boolean isBitalinoInitialized = false;
    private static final UUID APP_UUID = UUID.randomUUID();
    private final IBinder mBinder = new LocalBinder();
    private MiBand miband;

    @Override
    public void onCreate() {
        Log.i(logname, "onCreate");
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You don't have permission for GPS position!", Toast.LENGTH_LONG).show();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, this);

        if (!isBitalinoInitialized) {
            new BitalinoAsyncTask().execute();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service started!",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(logname, "onDestroy");
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
        if (lm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You don't have permission for GPS position!", Toast.LENGTH_LONG).show();
                return;
            }
            lm.removeUpdates(this);
        }
        super.onDestroy();
    }

    @Override
    public void connect(BluetoothDevice device) {
        ActionCallback connectCallback = new ActionCallback() {
            @Override
            public void onSuccess(Object data) {
                Log.i(logname,"Connected to MIBAND.");
            }

            @Override
            public void onFail(int errorCode, String msg) {
                Log.i(logname,"Failed to connect to MIBAND");
            }
        };
        miband.connect(device,connectCallback);
    }

    @Override
    public void startHeartRate() {

    }

    public class LocalBinder extends Binder {
        public ConnectionInterface getService() {
            return SensorService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.i(logname, sensorEvent.toString());
        for (SensorInterface si : listeners) {
            if(si == null){
                unregisterListener(si);
            } else {
                si.onSensorChanged(sensorEvent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.i(logname, sensor.toString() + " accuaracy: " + i);
        for (SensorInterface si : listeners) {
            if(si == null){
                unregisterListener(si);
            } else {
                si.onAccuracyChanged(sensor, i);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(logname, location.toString());
        for (SensorInterface si : listeners) {
            if(si == null){
                unregisterListener(si);
            } else {
                si.onLocationChanged(location);
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.i(logname, s.toString());
        for (SensorInterface si : listeners) {
            if(si == null){
                unregisterListener(si);
            } else {
                si.onStatusChanged(s, i, bundle);
            }
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.i(logname, s);
        for (SensorInterface si : listeners) {
            if(si == null){
                unregisterListener(si);
            } else {
                si.onProviderEnabled(s);
            }
        }
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.i(logname, s);
        for (SensorInterface si : listeners) {
            if(si == null){
                unregisterListener(si);
            } else {
                si.onProviderDisabled(s);
            }
        }
    }

    @Override
    public void registerListener(SensorInterface listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(SensorInterface listener) {
        listeners.remove(listener);
    }

    private class BitalinoAsyncTask extends AsyncTask<Void, String, Void> {
        private BluetoothDevice dev = null;
        private BluetoothSocket sock = null;
        private InputStream is = null;
        private OutputStream os = null;
        private BITalinoDevice bitalino;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                ///Connect to bluetooth device
                //remote Bluetooth device address
                final String remoteDevice = "C0:F8:DA:10:78:5C";
                final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                dev = btAdapter.getRemoteDevice(remoteDevice);
                //prevent high energy consumption
                btAdapter.cancelDiscovery();
                sock = dev.createRfcommSocketToServiceRecord(APP_UUID);
                sock.connect();

                isBitalinoInitialized = true;

                //Start Bitalino
                bitalino = new BITalinoDevice(1000, new int[]{0, 1, 2, 3, 4, 5});
                bitalino.open(sock.getInputStream(), sock.getOutputStream());
                bitalino.start();

                //Get Bitalinoframes
                final int numOfSamples = 1000;
                for (int i = 0; i < numOfSamples; ++i) {
                    BITalinoFrame[] frames = bitalino.read(numOfSamples);
                    for (BITalinoFrame frame : frames) {
                        Log.i(logname, frame.toString());
                    }
                }
            } catch (IOException e) {
                publishProgress("Bluetooth device is off");
                Log.i(logname, "Bluetooth device is off");
                e.printStackTrace();
            } catch (BITalinoException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                publishProgress("Given Bluetooth address is not valid");
                Log.i(logname, "Given Bluetooth address is not valid");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            Toast.makeText(getApplicationContext(),progress[0],Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            try {
                bitalino.stop();
                sock.close();
            } catch (BITalinoException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
