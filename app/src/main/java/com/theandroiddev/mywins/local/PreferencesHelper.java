package com.theandroiddev.mywins.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.theandroiddev.mywins.UI.Helpers.Constants.PACKAGE_NAME;

/**
 * Created by jakub on 04.11.17.
 */

@Singleton
public class PreferencesHelper {

    private final SharedPreferences pref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        pref = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        pref.edit().clear().apply();
    }

    public boolean isFirstSuccessAdded() {
        return pref.getBoolean("firstsuccess", false);
    }

    public void setFirstSuccessAdded() {
        pref.edit().putBoolean("firstsuccess", true).apply();
    }
}
