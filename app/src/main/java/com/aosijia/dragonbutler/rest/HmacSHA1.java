package com.aosijia.dragonbutler.rest;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Jacky on 2015/12/21.
 * Version 1.0
 */
public class HmacSHA1 {

    private static final String HMAC_SHA1 = "HmacSHA1";


    /**
     * 将请求参数转换为签名
     *
     * @param parameter HTTP请求参数
     * @return 签名
     */
    public static String getSignature(Map<String, String> parameter) {

        ArrayList<String> list = new ArrayList<String>();
        for (String key : parameter.keySet()) {
            StringBuffer sb = new StringBuffer();
            sb.append(key);
            sb.append("=");
            sb.append(parameter.get(key));
            list.add(sb.toString());
        }
//        String timestamp = "timestamp="+(System.currentTimeMillis()/1000);
//        String timestamp = "timestamp=1449908478";
//        list.add(timestamp);
        Collections.sort(list);

        StringBuffer stringbuffer = new StringBuffer();
        for (String temp : list) {
            stringbuffer.append("&").append(temp);
        }
        if (stringbuffer.toString().contains("&")) {
            stringbuffer = stringbuffer.deleteCharAt(0);
        }


        String signature = "";

        Log.e("HmacSHA1", "stringbuffer:" + stringbuffer);
        try {
            signature = hmacSha1(stringbuffer.toString(), URLManager.API_SECRET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        Log.e("HmacSHA1", "signature:" + signature);

        return signature;
//        return "e70b4edb139c6c37533f8ad8d2b44ffd4884973a" ;

    }

    private static String hmacSha1(String value, String key)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {
        String type = "HmacSHA1";
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), type);
        Mac mac = Mac.getInstance(type);
        mac.init(secret);
        byte[] bytes = mac.doFinal(value.getBytes());
        return bytesToHex(bytes);
    }

    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
