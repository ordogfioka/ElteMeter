package hu.elte.ordogfioka.eltemeter.Service.ClientConnection;

import android.content.Context;

import com.sap.mobile.lib.configuration.Preferences;
import com.sap.mobile.lib.request.ConnectivityParameters;
import com.sap.mobile.lib.request.RequestManager;
import com.sap.mobile.lib.supportability.Logger;
import com.sap.smp.rest.ClientConnection;

/**
 * Created by AMakoviczki on 2017. 05. 02..
 */

public class ClientConnectionUtility {
    public static Logger logger;
    public static Preferences pref;
    public static ConnectivityParameters param;
    public static RequestManager reqManagerObject;
    public static ClientConnection clientConnection;

    public ClientConnectionUtility(Context context){
        logger = new Logger();
        pref = new Preferences(context,logger);
        param = new ConnectivityParameters();
        param.setUserName("");
        param.setUserPassword("");
        reqManagerObject = new RequestManager(logger,pref,param,1);

        //TODO: set endpoint parameters
        clientConnection = new ClientConnection(context,"","","",reqManagerObject);
        clientConnection.setConnectionProfile("");
        clientConnection.setApplicationConnectionID("");
    }

    public static  ClientConnection getClientConnection(){
        return clientConnection;
    }

}
