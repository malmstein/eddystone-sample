package com.malmstein.eddystonesample;

import android.util.Base64;

public class StringUtils {

    private static final char[] HEX = "0123456789ABCDEF".toCharArray();

    public static byte[] base64Decode(String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

    public static String base64Encode(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT).trim();
    }

    public static String toHexString(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int c = bytes[i] & 0xFF;
            chars[i * 2] = HEX[c >>> 4];
            chars[i * 2 + 1] = HEX[c & 0x0F];
        }
        return new String(chars).toLowerCase();
    }

}
