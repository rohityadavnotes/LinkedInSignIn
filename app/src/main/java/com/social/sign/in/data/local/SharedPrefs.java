package com.social.sign.in.data.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

public class SharedPrefs {

    private static SharedPrefs instance = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @SuppressLint("CommitPrefEdits")
    private SharedPrefs(Context context, String preferenceFileName) {
        sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    /**
     * Initialize this class using application Context,
     * should be called once in the beginning by any application Component
     *
     * @param context application context
     */
    public static SharedPrefs getInstance(Context context) {
        if (context == null) {
            throw new NullPointerException("Provided application context is null");
        }
        if (instance == null) {
            synchronized (SharedPrefs.class) {
                if (instance == null) {
                    instance = new SharedPrefs(context, SharedPreferencesConstants.SHARED_PREFERENCES_FILE_NAME);
                }
            }
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private SharedPreferences.Editor getSharedPreferencesEditor() {
        return sharedPreferencesEditor;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass)
    {
        if (anonymousClass == String.class)
        {
            return (T) getSharedPreferences().getString(key, "");
        }
        else if (anonymousClass == Boolean.class)
        {
            return (T) Boolean.valueOf(getSharedPreferences().getBoolean(key, false));
        }
        else if (anonymousClass == Float.class)
        {
            return (T) Float.valueOf(getSharedPreferences().getFloat(key, 0));
        }
        else if (anonymousClass == Integer.class)
        {
            return (T) Integer.valueOf(getSharedPreferences().getInt(key, 0));
        }
        else if (anonymousClass == Long.class)
        {
            return (T) Long.valueOf(getSharedPreferences().getLong(key, 0));
        }
        else
        {
            return new Gson().fromJson(getSharedPreferences().getString(key, ""), anonymousClass);
        }
    }

    public <T> void put(String key, T data)
    {
        if (data instanceof String)
        {
            getSharedPreferencesEditor().putString(key, (String) data);
        }
        else if (data instanceof Boolean)
        {
            getSharedPreferencesEditor().putBoolean(key, (Boolean) data);
        }
        else if (data instanceof Float)
        {
            getSharedPreferencesEditor().putFloat(key, (Float) data);
        }
        else if (data instanceof Integer)
        {
            getSharedPreferencesEditor().putInt(key, (Integer) data);
        }
        else if (data instanceof Long)
        {
            getSharedPreferencesEditor().putLong(key, (Long) data);
        }
        else
        {
            getSharedPreferencesEditor().putString(key, new Gson().toJson(data));
        }
        getSharedPreferencesEditor().apply();
    }

    /**
     * Clears all data in SharedPreferences
     */
    public void clearPrefs() {
        getSharedPreferencesEditor().clear().apply();
    }

    public void removeKey(String key) {
        getSharedPreferencesEditor().remove(key).commit();
    }

    public boolean containsKey(String key) {
        return getSharedPreferences().contains(key);
    }
}