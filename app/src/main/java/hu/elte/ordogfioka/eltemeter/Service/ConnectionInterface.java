package hu.elte.ordogfioka.eltemeter.Service;

/**
 * Created by Attila on 2017. 04. 02..
 */

public interface ConnectionInterface {
    void registerListener(SensorInterface listener);
    void unregisterListener(SensorInterface listener);
}
