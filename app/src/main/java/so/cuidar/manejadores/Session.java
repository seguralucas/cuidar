package so.cuidar.manejadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import so.cuidar.entidades.User;

/**
 * Created by GAS on 28/05/2017.
 */
public class Session {

    private SharedPreferences prefs;
    private boolean isLogIn;
    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        if(prefs.getString("userName","").length()>0)
            setLogIn(true);
        else
            setLogIn(false);
    }

    public void setUser(String userName, String comunidad, String password) {
        if(!password.equalsIgnoreCase("123456"))
            return ;
        SharedPreferences.Editor edit= prefs.edit();
        edit.putString("userName", userName).commit();
        edit.putString("comunidad", comunidad).commit();
        edit.commit();
        this.setLogIn(true);
    }

    public User getUser() {
        User.getInstance().setUsuario(prefs.getString("userName",""));
        User.getInstance().setComunidad(prefs.getString("comunidad",""));
        return User.getInstance();
    }

    public void deleteSession(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("userName");
        editor.remove("comunidad");
        editor.apply();
        this.setLogIn(false);
    }

    public void initPreferenceManager(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public boolean isLogIn() {
        return isLogIn;
    }

    public void setLogIn(boolean logIn) {
        isLogIn = logIn;
    }
}