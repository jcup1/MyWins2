package com.theandroiddev.mywins.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jakub on 04.11.17.
 */

@Singleton
public class PreferencesHelper {
    //TODO IMPLEMENT?

    public static final String PREF_FILE_NAME = "android_mywins_pref_file";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }
}
