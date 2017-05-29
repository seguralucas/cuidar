package so.cuidar.manejadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by GAS on 28/05/2017.
 */
public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setusename(String usename) {
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString("usename", usename).commit();
        edit.commit();
    }

    public String getusename() {
        String usename = prefs.getString("usename","");
        return usename;
    }
}