package com.dbendyug.loftcoin.util;

import android.content.Context;

public interface Settings {

    static Settings of(Context context){
        return new SettingsCustomization(context);
    }

    boolean showWelcomeScreen();
    void doNotShowWelcomeScreenAgain();
}
