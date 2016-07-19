package com.antoinepourchet.pingme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class PersistentDataManager {

    public static final String TAG = "PersistentDataManager";

    public static final String DEFAULT_HOST = "104.155.134.17:1025";
    public static final String DEFAULT_CHANNELS = Arrays.toString(new String[0]);

    public static final String PREF_CHANNELS = "channels";
    public static final String PREF_HOST = "host";


    public static Set<String> getChannels(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String channelsString = sharedPref.getString(PREF_CHANNELS, DEFAULT_CHANNELS);

        String[] arr = channelsFromString(channelsString);
        return new HashSet<>(Arrays.asList(arr));
    }

    public static void addChannel(Context context, String newChannel) {
        Set<String> allChannels = getChannels(context);
        allChannels.add(newChannel);

        String[] arr = allChannels.toArray(new String[allChannels.size()]);
        String newChannelsString = Arrays.toString(arr);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(PREF_CHANNELS, newChannelsString);
        editor.apply();
    }

    public static void clearChannels(Context context) {
        Log.d(TAG, "CLEARING PREFERENCES");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(PREF_CHANNELS, Arrays.toString(new String[0]));
        editor.apply();
    }

    public static String getHost(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(PREF_HOST, DEFAULT_HOST);
    }

    public static void setHost(Context context, String host) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(PREF_HOST, host);
        editor.apply();
    }

    private static String[] channelsFromString(String s) {
        s = s.replace("[", "").replace("]", "");
        if (s.length() == 0) {
            return new String[0];
        }
        return s.split(", ");
    }
}
