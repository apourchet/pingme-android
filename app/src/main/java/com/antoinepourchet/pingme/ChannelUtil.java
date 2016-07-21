package com.antoinepourchet.pingme;

import android.util.Base64;

import java.util.Random;


public class ChannelUtil {

    public static final int RANDOM_ID_SIZE = 4;

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String getNewId() {
        byte[] b = new byte[RANDOM_ID_SIZE];
        new Random().nextBytes(b);
        return bytesToHex(b);
    }

    public static String parseLine(String line) throws Exception {
        if (!line.contains("data: ")) {
            throw new Exception("Malformed EventStream Data");
        }
        String parsedLine = line.split("data: ")[1];
        parsedLine = new String(Base64.decode(parsedLine, Base64.URL_SAFE));
        return parsedLine;
    }

    public static int simpleHash(String s) {
        int hash = 7;
        for (int i = 0; i < s.length(); i++) {
            hash = hash * 31 + s.charAt(i);
        }
        return hash;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
