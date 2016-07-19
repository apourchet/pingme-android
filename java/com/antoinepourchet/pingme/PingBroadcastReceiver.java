package com.antoinepourchet.pingme;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

public class PingBroadcastReceiver extends BroadcastReceiver {

    private ArrayList<Pingable> pingables;

    public PingBroadcastReceiver(Pingable pingable) {
        super();
        this.pingables = new ArrayList<>();
        this.pingables.add(pingable);
    }

    public PingBroadcastReceiver(List<Pingable> ls) {
        super();
        this.pingables = new ArrayList<>(ls);
    }

    public void listen(Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(this,
                new IntentFilter(ListenService.BROADCAST_PING)
        );
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        for (Pingable p : this.pingables) {
            String s = intent.getStringExtra(ListenService.MESSAGE_PING);
            p.onPing(new Ping("12345", "DEFAULT_MSG"));
        }
    }
}
