package org.bihe.finalproject.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class PreferencesManager {
    private static final String PREF_FILE_NAME = "org.bihe.finalproject";
    public static final String PREF_KEY_USER_NAME = "userName";
    public static final String PREF_KEY_PASSWORD = "password";
    public static final String PREF_KEY_ACCOUNT_NUM = "accountNum";
    public static final String PREF_KEY_CARD_NUM = "cardNum";
    public static final String PREF_KEY_CVV2 = "cvv2s";

    public static final String PREF_KEY_BALANCE = "balance";
    public static final String PREF_KEY_LOGGED = "isLoggedIn";
    public static final String PREF_KEY_CURRENT_USER = "current_user";


    private static PreferencesManager sInstance;
    private Context mContext;
    private SharedPreferences preferences;
    private final Gson gson = new Gson();


    private PreferencesManager(Context context) {
        mContext = context;
        preferences = mContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
        return sInstance;
    }

    public <T> void put(String key, T value) {
        if (value instanceof String) {
            preferences.edit().putString(key, (String) value).apply();
        }
        if (value instanceof Boolean) {
            preferences.edit().putBoolean(key, (Boolean) value).apply();
        }
    }

    public <T> T get(String key, T defaultValue) {
        if (defaultValue instanceof String) {
            return (T) preferences.getString(key, (String) defaultValue);
        }

        if (defaultValue instanceof Boolean) {
            Boolean result = preferences.getBoolean(key, (Boolean) defaultValue);
            return (T) result;
        }

        if (defaultValue instanceof Float) {
            Float result = preferences.getFloat(key, (Float) defaultValue);
            return (T) result;
        }

        return null;
    }

    public <T> T getObj(String key, Class<T> t) {
        String jsonPreferences = preferences.getString(key, "");
        return gson.fromJson(jsonPreferences, t);
    }

    public <T> void putObj(String key, T t) {
        String json = gson.toJson(t);
        preferences.edit().putString(key, json).apply();
    }
}
