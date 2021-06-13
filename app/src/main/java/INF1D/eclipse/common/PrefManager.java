package INF1D.eclipse.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "eclips-mirror";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String USER_TOKEN = "userToken";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public String getUserToken(){
        return pref.getString(USER_TOKEN, null);
    }

    public void setUserToken(String token){
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }
}