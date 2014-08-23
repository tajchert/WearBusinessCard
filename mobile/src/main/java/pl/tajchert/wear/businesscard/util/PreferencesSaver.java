package pl.tajchert.wear.businesscard.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by michaltajchert on 23/08/14.
 */
public class PreferencesSaver {
    private SharedPreferences preferences;

    public PreferencesSaver(Context context){
        preferences = context.getSharedPreferences(ValuesCons.PREFS_KEY, Context.MODE_PRIVATE);
    }

    public void saveStringValue(String key, String value){
        preferences.edit().putString(key,value).apply();
    }

    public String getStringValue(String key){
        return preferences.getString(key, "");
    }
}
