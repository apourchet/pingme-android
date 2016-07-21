package com.antoinepourchet.pingme;


import android.content.Context;
import android.util.Log;

import java.util.Locale;
import java.util.Objects;

public class ChannelPreference {

    public static final String TAG = "ChannelPreference";

    private String host;
    private String id;
    private boolean listen;

    public ChannelPreference(String host, String id, boolean doListen) {
        this.host = host;
        this.id = id;
        this.listen = doListen;
    }

    public ChannelPreference(String host, String id) {
        this.host = host;
        this.id = id;
        this.listen = true;
    }

    public boolean doListen() {
        return listen;
    }

    public String getHost() {
        return host;
    }

    public String getId() {
        return id;
    }

    public ChannelPreference setListen(boolean listen) {
        this.listen = listen;
        return this;
    }

    public ChannelPreference save(Context context) {
        PersistentDataManager.addChannel(context, this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        int code = host.hashCode() + id.hashCode();
        return code;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Channel { %s -> %s (%s) }",
                host, id, listen ? "ON" : "OFF");
    }

    public static ChannelPreference toggleListen(ChannelPreference c) {
        return new ChannelPreference(c.getHost(), c.getId(), !c.doListen());
    }

    public static String marshal(ChannelPreference channelPreference) {
        StringBuilder builder = new StringBuilder();
        builder.append(channelPreference.getHost()).append(";")
                .append(channelPreference.getId()).append(";")
                .append(channelPreference.doListen());
        return builder.toString();
    }

    public static ChannelPreference unmarshal(String s) {
        String[] arr = s.split(";");
        if (arr.length != 3) {
            return null;
        }

        String host = arr[0];
        String channelId = arr[1];
        boolean doListen = Boolean.parseBoolean(arr[2]);

        return new ChannelPreference(host, channelId, doListen);
    }
}
