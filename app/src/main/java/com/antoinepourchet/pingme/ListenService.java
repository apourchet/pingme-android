package com.antoinepourchet.pingme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class ListenService extends Service implements Pingable {

    public static final String TAG = "ListenService";
    public static final String BROADCAST_PING = "broadcast_intent";
    public static final String MESSAGE_PING = "broadcast_message";

    private static ListenService instance;

    private HashMap<String, ChannelListener> channels;
    private Notifier notifier;

    private LocalBroadcastManager broadcaster;

    public static ListenService getInstance() {
        return instance;
    }

    @Override
    public void onPing(Ping ping) {
        ping.save(this);
        notifier.onPing(ping);
        broadcastPing(ping);
    }

    public void broadcastPing(Ping ping) {
        Intent intent = new Intent(BROADCAST_PING);
        intent.putExtra(MESSAGE_PING, Ping.marshal(ping));
        broadcaster.sendBroadcast(intent);
    }

    public synchronized boolean isListening(String host, String channelId) {
        String key = host + ";" + channelId;
        return channels.containsKey(key) && channels.get(key).isListening();
    }

    public synchronized void startListen(String host, String channelId) {
        String key = host + ";" + channelId;
        ChannelListener cl = new ChannelListener(this, host, channelId);
        channels.put(key, cl);
        cl.start();
    }

    public synchronized void stopListen(String host, String channelId) {
        String key = host + ";" + channelId;
        if (channels.containsKey(key)){
            channels.get(key).stop();
            channels.remove(key);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        channels = new HashMap<>();
        notifier = new Notifier(this);
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "OnStart");

        Set<ChannelPreference> allChannels = PersistentDataManager.getChannels(this);
        for (ChannelPreference c : allChannels) {
            if (c.doListen()) {
                startListen(c.getHost(), c.getId());
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}