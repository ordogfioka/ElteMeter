package hu.elte.ordogfioka.eltemeter.Service.ClientConnection;

import android.content.Context;

import com.sap.mobile.lib.configuration.Preferences;
import com.sap.mobile.lib.request.ConnectivityParameters;
import com.sap.mobile.lib.request.RequestManager;
import com.sap.mobile.lib.supportability.Logger;
import com.sap.smp.rest.ClientConnection;

import hu.elte.ordogfioka.eltemeter.Model.PreferenceValues;

/**
 * Created by AMakoviczki on 2017. 05. 02..
 */

public class ClientConnectionUtility {
    public static Logger logger;
    public static Preferences pref;
    public static ConnectivityParameters param;
    public static RequestManager reqManagerObject;
    public static ClientConnection clientConnection;
    public PreferenceValues pf;

    public ClientConnectionUtility(Context context){
        logger = new Logger();
        pref = new Preferences(context,logger);
        param = new ConnectivityParameters();
        param.setUserName(pf.getUsername());
        param.setUserPassword(pf.getPassword());
        reqManagerObject = new RequestManager(logger,pref,param,1);

        clientConnection = new ClientConnection(context,pf.getApplicationid(),pf.getDomain(),pf.getSecconf(),reqManagerObject);
        clientConnection.setConnectionProfile(pf.getHostname() + ":" + pf.getPort());
        clientConnection.setApplicationConnectionID(pf.getAppconnid());
    }

    public static  ClientConnection getClientConnection(){
        return clientConnection;
    }

}
