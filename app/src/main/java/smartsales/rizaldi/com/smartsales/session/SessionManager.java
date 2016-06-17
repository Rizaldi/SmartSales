package smartsales.rizaldi.com.smartsales.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Toshiba-PC on 4/14/2016.
 */
public class SessionManager {
    private static String TAG=SessionManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE=0;
    private static final String Pref_Name="SmartSalesLogin";

    private static final String KEY_IS_LOGGEDIN="isLoggedIn";
    public SessionManager(Context _context) {
        this._context = _context;
        pref=_context.getSharedPreferences(Pref_Name,PRIVATE_MODE);
        editor=pref.edit();
    }
    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN,isLoggedIn);
        editor.commit();
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
