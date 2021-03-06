package com.antoinepourchet.pingme;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ChannelListener implements Runnable {

    public static final String TAG = "ChannelListener";

    private String host;
    private String channelId;
    private URL url;

    private Pingable pingable;

    private boolean listening;
    private boolean requestedStop;
    private Thread thread;

    public ChannelListener(Pingable pingable, String host, String channelId) {
        this.host = host;
        this.channelId = channelId;
        this.pingable = pingable;
        this.requestedStop = false;
        this.listening = false;
        this.thread = new Thread(this);
    }

    public boolean isListening() {
        return listening;
    }

    public String getURLString() {
        return "http://" + host + "/listen";
    }

    public boolean isWellFormed() {
        try {
            url = new URL(getURLString());
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    private void readConnection(BufferedReader in) throws IOException {
        listening = true;
        String inputLine;
        while ((inputLine = in.readLine()) != null && !requestedStop) {
            if (inputLine.length() == 0) {
                continue;
            }
            onLineRead(inputLine);
        }
        in.close();
    }

    private void onLineRead(String inputLine) {
        try {
            String line = ChannelUtil.parseLine(inputLine);
            Ping ping = new Ping(host, channelId, line);
            pingable.onPing(ping);
        } catch (Exception e) {
            Log.e(TAG, "MALFORMED MESSAGE: " + inputLine);
        }
    }

    private void waitForRetry() {
        try {
            listening = false;
            Thread.sleep(5000);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private BufferedReader prepareConnectionStreams(HttpURLConnection conn) throws IOException {
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.write(("id=" + channelId).getBytes());
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        return in;
    }

    public void start() {
        thread.start();
    }

    public void stop() {
        Log.d(TAG, "Closing: " + this.host + " -> " + this.channelId);
        this.requestedStop = true;
    }

    @Override
    public void run() {
        if (!isWellFormed()) {
            Log.e(TAG, "MALFORMED URL: '" + this.host + "'");
            return;
        }
        Log.d(TAG, "Listening: " + this.host + " -> " + this.channelId);

        while (true) {
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                BufferedReader in = prepareConnectionStreams(conn);

                readConnection(in);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            waitForRetry();
        }
    }
}
