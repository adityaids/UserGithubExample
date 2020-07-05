package com.aditya.usergithub.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreference {
    private static final String PREFS_NAME = "AppPref";
    private static final String APP_FIRST_RUN = "app_first_run";
    private static final String SETTING_NOTIF = "setting_notif";
    private static final String FIRST_FAVORIT = "first_favorit";
    private final SharedPreferences prefs;

    public AppPreference(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setFirstRun(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(APP_FIRST_RUN, input);
        editor.apply();
    }

    public void setFirstRunFavorit(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(FIRST_FAVORIT, input);
        editor.apply();
    }

    public Boolean getFirstRun() {
        return prefs.getBoolean(APP_FIRST_RUN, true);
    }

    public void setNotif(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SETTING_NOTIF, input);
        editor.apply();
    }

    public Boolean getNotifState() {
        return prefs.getBoolean(SETTING_NOTIF, true);
    }

    public Boolean getFirstFavorit() {
        return prefs.getBoolean(FIRST_FAVORIT, true);
    }
}
