package hu.elte.ordogfioka.eltemeter.Service;

/**
 * Created by Attila on 2017. 04. 02..
 */

public interface ConnectionInterface extends MiBandInterface {
    void registerListener(SensorInterface listener);
    void unregisterListener(SensorInterface listener);
}
