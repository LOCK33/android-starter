package net.bndy.ad.framework.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Set;

public class SharedPreferencesHelper {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSPEditor;

    public SharedPreferencesHelper(Context context, String name) {
       this.mContext = context;
       this.mSharedPreferences = this.mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
       this.mSPEditor = this.mSharedPreferences.edit();
    }

    public SharedPreferences getSharedPerferences() {
        return this.mSharedPreferences;
    }

    public String getString(String key) {
        return this.mSharedPreferences.getString(key, null);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return this.mSharedPreferences.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return this.mSharedPreferences.getFloat(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return this.mSharedPreferences.getLong(key, defaultValue);
    }

    public Set<String> getStringSet(String key) {
        return this.mSharedPreferences.getStringSet(key, Collections.<String>emptySet());
    }

    public <T> T get(String key, Class<T> classOfT) {
        String json = this.getString(key);
        if (json != null && !json.isEmpty()) {
            return new Gson().fromJson(json, classOfT);
        }
        return null;
    }

    public SharedPreferencesHelper set(String key, Object value) {
        this.mSPEditor.putString(key, new Gson().toJson(value));
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper set(String key, String value) {
        this.mSPEditor.putString(key, value);
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper set(String key, boolean value) {
        this.mSPEditor.putBoolean(key, value);
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper set(String key, float value) {
        this.mSPEditor.putFloat(key, value);
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper set(String key, long value) {
        this.mSPEditor.putLong(key, value);
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper set(String key, int value) {
        this.mSPEditor.putInt(key, value);
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper set(String key, Set<String> values) {
        this.mSPEditor.putStringSet(key, values);
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper remove(String key) {
        this.mSPEditor.remove(key);
        this.mSPEditor.commit();
        return this;
    }

    public SharedPreferencesHelper clear() {
        this.mSPEditor.clear();
        return this;
    }
}
