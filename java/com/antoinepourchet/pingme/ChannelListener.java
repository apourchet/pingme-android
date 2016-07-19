package com.antoinepourchet.pingme;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ChannelListener extends AsyncTask<String, String, Object> {

    public static final String TAG = "ChannelListener";

    private String host;
    private String channelId;
    private ListenService service;
    private URL url;
    private boolean listening = false;

    public ChannelListener(ListenService service, String host, String channelId) {
        this.host = host;
        this.channelId = channelId;
        this.service = service;
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
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.length() == 0) {
                continue;
            }
            service.onChannelProgress(this.channelId, inputLine);
        }
        in.close();
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

    @Override
    protected Object doInBackground(String... params) {
        if (!isWellFormed()) {
            return null;
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
