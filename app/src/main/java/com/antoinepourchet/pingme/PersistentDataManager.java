package com.antoinepourchet.pingme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PersistentDataManager {

    public static final String TAG = "PersistentDataManager";

    public static final String DEFAULT_HOST = "104.155.134.17:1025";
    public static final String DEFAULT_CHANNELS = "";
    public static final String DEFAULT_LASTPINGS = "";

    public static final String PREF_CHANNELS = "channels";
    public static final String PREF_HOST = "host";
    public static final String PREF_LASTPINGS = "last_pings";


    public static Set<ChannelPreference> getChannels(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String channelsString = sharedPref.getString(PREF_CHANNELS, DEFAULT_CHANNELS);
        return channelsFromString(channelsString);
    }

    public static List<Ping> getLastPings(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String pingsString = sharedPref.getString(PREF_LASTPINGS, DEFAULT_LASTPINGS);
        return pingsFromString(pingsString);
    }

    public static String getHost(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(PREF_HOST, DEFAULT_HOST);
    }

    public static void addChannel(Context context, ChannelPreference newChannel) {
        Set<ChannelPreference> allChannels = getChannels(context);

        allChannels.remove(newChannel);
        allChannels.add(newChannel);

        String newChannelsString = stringFromChannels(allChannels);
        applyPrefString(context, PREF_CHANNELS, newChannelsString);
    }

    public static void addLastPing(Context context, Ping ping) {
        List<Ping> pings = getLastPings(context);
        Map<String, Ping> map = new HashMap<>();
        for (Ping p : pings) {
            map.put(p.getChannelId(), p);
        }
        map.put(ping.getChannelId(), ping);

        String newPingsString = stringFromPings(map.values());
        applyPrefString(context, PREF_LASTPINGS, newPingsString);
    }

    public static void setHost(Context context, String host) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(PREF_HOST, host);
        editor.apply();
    }

    public static void removeChannel(Context context, ChannelPreference channelPreference) {
        Set<ChannelPreference> allChannels = getChannels(context);
        allChannels.remove(channelPreference);
        String newChannelsString = stringFromChannels(allChannels);
        applyPrefString(context, PREF_CHANNELS, newChannelsString);
    }

    // TODO implement
    public static void removeLastPing(Context context) {

    }

    public static void clearChannels(Context context) {
        applyPrefString(context, PREF_CHANNELS, DEFAULT_CHANNELS);
    }

    public static void clearLastPings(Context context) {
        applyPrefString(context, PREF_LASTPINGS, DEFAULT_LASTPINGS);
    }

    public static void clearHost(Context context) {
        applyPrefString(context, PREF_HOST, DEFAULT_HOST);
    }


    // PRIVATE ------------------------------------------------------------------------------------
    private static void applyPrefString(Context context, String label, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(label, value);
        editor.apply();
    }

    private static Set<ChannelPreference> channelsFromString(String channelsString) {
        Set<ChannelPreference> set = new HashSet<>();
        if (channelsString.length() == 0) {
            return set;
        }

        String[] arr = channelsString.split(";");
        for (String s : arr) {
            String decoded = new String(Base64.decode(s, Base64.URL_SAFE));
            set.add(ChannelPreference.unmarshal(decoded));
        }
        return set;
    }

    private static String stringFromChannels(Collection<ChannelPreference> channels) {
        StringBuilder builder = new StringBuilder();
        for (ChannelPreference c : channels) {
            String channelString = new String(Base64.encode(ChannelPreference.marshal(c).getBytes(), Base64.URL_SAFE));
            builder.append(channelString).append(";");
        }
        return builder.toString();
    }

    private static List<Ping> pingsFromString(String pingsString) {
        List<Ping> ls = new ArrayList<>();
        if (pingsString.length() == 0) {
            return ls;
        }

        String[] arr = pingsString.split(";");
        for (String s : arr) {
            String decoded = new String(Base64.decode(s, Base64.URL_SAFE));
            ls.add(Ping.unmarshal(decoded));
        }
        return ls;
    }

    private static String stringFromPings(Collection<Ping> ls) {
        StringBuilder builder = new StringBuilder();
        for (Ping p : ls) {
            String s = Ping.marshal(p);
            s = new String(Base64.encode(s.getBytes(), Base64.URL_SAFE));
            builder.append(s).append(";");
        }
        return builder.toString();
    }
}
