package com.antoinepourchet.pingme;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class Notifier implements Pingable {

    public static final String TAG = "Notifier";

    private Service service;

    public Notifier(Service service) {
        this.service = service;
    }

    public void displayNotification(String channelId, String parsedLine) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service)
                .setSmallIcon(R.drawable.icon36x36)
                .setContentTitle("Ping from '" + channelId + "'")
                .setContentText(parsedLine);

        NotificationManager mNotificationManager =
                (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        setNotificationIntent(MainActivity.class, builder);
        mNotificationManager.notify(ChannelUtil.simpleHash(channelId), builder.build());
        Log.d(TAG, "Displayed Notification");
    }

    public void setNotificationIntent(Class c, NotificationCompat.Builder builder) {
        Intent resultIntent = new Intent(service, c);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(service);
        stackBuilder.addParentStack(c);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
    }

    public void onPing(Ping ping) {
        displayNotification(ping.getChannelId(), ping.getMessage());
    }
}
