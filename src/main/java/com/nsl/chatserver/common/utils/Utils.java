package com.nsl.chatserver.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by sachin on 8/6/17.
 */

public class Utils {

    public static String DOMAIN_NAME;

    private static final String URL_SEPARATOR = "/";
    private static final String WEBSOCKET = "websocket";
    private static final String AVATAR = "avatar";

    public static String getDigest(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public static String generateRandomHexToken(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return new BigInteger(token).toString(16); //hex encoding
    }

    public static String shortUUID() {
        UUID uuid = UUID.randomUUID();
        long l = ByteBuffer.wrap(uuid.toString().getBytes()).getLong();
        return Long.toString(l, Character.MAX_RADIX);
    }

    public static String getFileTypeUsingName(String fileName) {
        return URLConnection.guessContentTypeFromName(fileName);
    }

    public static String getFileTypeUsingStream(File file) throws IOException {
        return URLConnection.guessContentTypeFromStream(new FileInputStream(file));
    }

    public static String getEndPointFromDomainName(String domainName) {
        if (domainName.substring(0, 2).equals("ws")) {
            DOMAIN_NAME = domainName.replaceFirst("ws", "http");
        } else {
            DOMAIN_NAME = domainName;
        }
        return DOMAIN_NAME + URL_SEPARATOR + WEBSOCKET;
    }

    public static String getAvatar(String username) {
        return DOMAIN_NAME + URL_SEPARATOR + AVATAR + URL_SEPARATOR + username;
    }
}
