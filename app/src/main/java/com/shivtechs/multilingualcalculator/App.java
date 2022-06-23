package com.shivtechs.multilingualcalculator;

import android.app.Application;
import android.content.Context;

import java.util.Locale;

public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, Locale.getDefault().getLanguage(),Locale.getDefault().getDisplayLanguage()));
    }
}
