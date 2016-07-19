package com.antoinepourchet.pingme;


public class Ping {

    private String channelId;
    private String message;
    private long time;

    public Ping(String channelId, String message) {
        this.channelId = channelId;
        this.message = message;
        this.time = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    public String getChannelId() {
        return channelId;
    }

    // TODO
    public static String marshal(Ping ping) {
        return "";
    }

    // TODO
    public static Ping unmarshal(String s) {
        return new Ping("12345", "UNMARSHALED");
    }
}
