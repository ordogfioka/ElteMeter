package hu.elte.ordogfioka.eltemeter.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by AMakoviczki on 2017. 05. 14..
 */

public class PreferenceValues {
    String username;
    String password;
    String applicationid;
    String domain;
    String secconf;
    String hostname;
    String port;
    String appconnid;

    public PreferenceValues(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        username = prefs.getString("username", "");
        password = prefs.getString("password", "");
        applicationid = prefs.getString("appid", "");
        domain = prefs.getString("domain", "");
        secconf = prefs.getString("secconf", "");
        hostname = prefs.getString("hostname", "");
        port = prefs.getString("port", "");
        appconnid = prefs.getString("appconnid", "");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getApplicationid() {
        return applicationid;
    }

    public String getDomain() {
        return domain;
    }

    public String getSecconf() {
        return secconf;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return port;
    }

    public String getAppconnid() {
        return appconnid;
    }
}
