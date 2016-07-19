package com.antoinepourchet.pingme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootBroadcastReceiver extends BroadcastReceiver
{
    public void onReceive(Context arg0, Intent arg1) {
        Log.d("OnBootBroadcastReceiver", "Starting");
        Intent intent = new Intent(arg0, ListenService.class);
        arg0.startService(intent);
        Log.d("OnBootBroadcastReceiver", "Started");
    }
}