package hu.elte.ordogfioka.eltemeter.Service;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Attila on 2017. 05. 07..
 */

public interface MiBandInterface {
    void connect(BluetoothDevice device);
    void startHeartRate();
}
