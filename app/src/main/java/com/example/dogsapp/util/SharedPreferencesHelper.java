package com.example.dogsapp.util;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

//store and retrieve data fromm the shared preferences
public class SharedPreferencesHelper {

    private static final String PREF_TIME="Pref Time";//key of the shared preference which will help to store the value
    private static SharedPreferencesHelper instance;
    private SharedPreferences prefs;

    private SharedPreferencesHelper(Context context){
        prefs= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferencesHelper getInstance(Context context){
        if(instance==null){
            instance=new SharedPreferencesHelper(context);
        }
        return instance;
    }

    public void saveUpdateTime(long time){
        prefs.edit().putLong(PREF_TIME,time).apply();
    }

    public long getUpdateTime(){
        return prefs.getLong(PREF_TIME,0);
    }
}
