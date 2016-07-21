package com.antoinepourchet.pingme;


import android.content.Context;
import android.util.Base64;

import java.util.Locale;

public class Ping {

    private final String host;
    private final String channelId;
    private final String message;
    private final long time;

    public Ping(String host, String channelId, String message) {
        this.host = host;
        this.channelId = channelId;
        this.message = message;
        this.time = System.currentTimeMillis();
    }

    public Ping(String host, String channelId, String message, long time) {
        this.host = host;
        this.channelId = channelId;
        this.message = message;
        this.time = time;
    }

    public String getHost() {
        return host;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public String getChannelId() {
        return channelId;
    }

    public Ping save(Context context) {
        PersistentDataManager.addLastPing(context, this);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return host.hashCode() + channelId.hashCode() + message.hashCode() + (int) time;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "Ping { %s -> %s ; %s (%d) }", host, channelId, message, time);
    }

    public static String marshal(Ping ping) {
        StringBuilder builder = new StringBuilder();
        builder.append(ping.getHost()).append(";")
                .append(ping.getChannelId()).append(";")
                .append(new String(Base64.encode(ping.getMessage().getBytes(), Base64.URL_SAFE)))
                .append(";").append(ping.getTime());
        return builder.toString();
    }

    public static Ping unmarshal(String s) {
        String[] arr = s.split(";");
        if (arr.length != 4) {
            return null;
        }

        String host = arr[0];
        String channelId = arr[1];
        String message = new String(Base64.decode(arr[2], Base64.URL_SAFE));
        long time = Long.parseLong(arr[3]);

        return new Ping(host, channelId, message, time);
    }
}
