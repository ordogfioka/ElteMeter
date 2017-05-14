package hu.elte.ordogfioka.eltemeter.Model;

import android.location.Location;

/**
 * Created by AMakoviczki on 2017. 05. 14..
 */

public class SensorModel {
    Float accuracy;
    Double longitude;
    Double latitude;
    Float speed;

    public SensorModel(Location location) {
        accuracy = location.getAccuracy();
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        speed = location.getSpeed();
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Float getSpeed() {
        return speed;
    }
}
