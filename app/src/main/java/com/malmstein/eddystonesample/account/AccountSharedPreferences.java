package com.malmstein.eddystonesample.account;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.malmstein.eddystonesample.BuildConfig;

public class AccountSharedPreferences {

    public static final String EMPTY_VALUE = "";
    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".LOGIN_PREFERENCES";
    private static final String KEY_USERNAME = BuildConfig.APPLICATION_ID + ".KEY_USERNAME";
    private final SharedPreferences preferences;

    private AccountSharedPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public static AccountSharedPreferences newInstance(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new AccountSharedPreferences(preferences);
    }

    public void saveAccount(String accountName) {
        preferences.edit().putString(KEY_USERNAME, accountName).apply();
    }

    public String getAccount() {
        return preferences.getString(KEY_USERNAME, EMPTY_VALUE);
    }

    public boolean isLoggedIn() {
        return getAccount() != EMPTY_VALUE;
    }

    public void logout() {
        preferences.edit().putString(KEY_USERNAME, EMPTY_VALUE).apply();
    }

}
