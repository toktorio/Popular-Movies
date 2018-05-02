package com.timotiusoktorio.popularmovies.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class PreferencesHelper {

    private static final String KEY_SELECTED_SECTION = "selected_section";

    private static PreferencesHelper sInstance;
    private final SharedPreferences mPreferences;

    public static PreferencesHelper getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private PreferencesHelper(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getSelectedSection() {
        return mPreferences.getInt(KEY_SELECTED_SECTION, 0);
    }

    public void setSelectedSection(int sectionId) {
        mPreferences.edit().putInt(KEY_SELECTED_SECTION, sectionId).apply();
    }
}