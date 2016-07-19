package com.antoinepourchet.pingme;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class ListenService extends Service {

    public static final String TAG = "ListenService";
    public static final String BROADCAST_PING = "broadcast_intent";
    public static final String MESSAGE_PING = "broadcast_message";

    private static ListenService instance;

    private HashMap<String,ChannelListener> channels;
    private HashMap<String,Ping> lastPings;
    private Notifier notifier;

    private LocalBroadcastManager broadcaster;

    public static ListenService getInstance() {
        return instance;
    }

    public synchronized void onChannelProgress(String channelId, String lastLine) {
        try {
            String line = ChannelUtil.parseLine(lastLine);
            Ping ping = new Ping(channelId, line);
            lastPings.put(channelId, ping);
            notifier.onPing(ping);
            broadcastPing(ping);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void broadcastPing(Ping ping) {
        Intent intent = new Intent(BROADCAST_PING);
        intent.putExtra(MESSAGE_PING, "TEST");
        Log.d(TAG, "BROADCASTED");
        broadcaster.sendBroadcast(intent);
    }

    public synchronized boolean isListening(String channelid) {
        return channels.containsKey(channelid) && channels.get(channelid).isListening();
    }

    public synchronized List<Ping> getLastPings() {
        return new ArrayList<>(lastPings.values());
    }

    public synchronized void startListen(String channelId) {
        String host = PersistentDataManager.getHost(this);
        ChannelListener cl = new ChannelListener(this, host, channelId);
        cl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        channels.put(channelId, cl);
    }

    public synchronized void stopListen(String channelId) {
        if (channels.containsKey(channelId)){
            channels.get(channelId).cancel(true);
            channels.remove(channelId);
        }
    }

    public synchronized void removePing(String channelId) {
        lastPings.remove(channelId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        channels = new HashMap<>();
        lastPings = new HashMap<>();
        notifier = new Notifier(this);
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "OnStart");

        Set<String> allChannels = PersistentDataManager.getChannels(this);
        String host = PersistentDataManager.getHost(this);

        for (String c : allChannels) {
            ChannelListener cl = new ChannelListener(this, host, c);
            cl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            channels.put(c, cl);
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