package com.zimpassflow.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "ZimpassPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_ONBOARDING_FINISHED = "onboarding_finished";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setOnboardingFinished(boolean finished) {
        editor.putBoolean(KEY_ONBOARDING_FINISHED, finished);
        editor.apply();
    }

    public boolean isOnboardingFinished() {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_FINISHED, false);
    }

    public void clear() {
        editor.clear();
        editor.apply();
    }
}