package hu.elte.ordogfioka.eltemeter.Service;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.sap.smp.rest.*;

import static hu.elte.ordogfioka.eltemeter.Service.ClientConnection.ClientConnectionUtility.clientConnection;

/**
 * Created by AMakoviczki on 2017. 05. 02..
 */

public class UserConnection extends Activity implements SMPClientListeners.ISMPUserRegistrationListener {
    UserManager um;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView();
        um = new UserManager(clientConnection);
        um.setUserRegistrationListener(UserConnection.this);
        try {
            um.registerUser(false);
        } catch (SMPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAsyncRegistrationResult(State state, ClientConnection clientConnection, int i, String s) {
        if(state == State.SUCCESS){
            Log.i("RegState Success", clientConnection.getApplicationID());
            try {
                Log.i("RegState Success", "APPCID" + um.getApplicationConnectionId());
            } catch (SMPException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("RegState Failure",i + s);
        }
    }
}
