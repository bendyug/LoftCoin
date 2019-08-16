package com.dbendyug.loftcoin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class SettingsImplementation implements Settings {

    private SharedPreferences sharedPreferences;
    private static final String SHOW_WELCOME_SCREEN_KEY = "show_welcome_screen";

    SettingsImplementation(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean showWelcomeScreen() {
        return sharedPreferences.getBoolean(SHOW_WELCOME_SCREEN_KEY, true);
    }

    @Override
    public void doNotShowWelcomeScreenAgain() {
        sharedPreferences.edit().putBoolean(SHOW_WELCOME_SCREEN_KEY, false).apply();
    }
}
