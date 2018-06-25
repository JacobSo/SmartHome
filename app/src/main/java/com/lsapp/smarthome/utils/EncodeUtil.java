package com.lsapp.smarthome.utils;

import android.util.Log;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/8/4.
 */
public class EncodeUtil {


    public static String Signing(Map<String, String> params, String secret,String encode) throws Exception {

// 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
// 第二步：把所有参数名和参数值串在一起
        StringBuilder strBuilder = new StringBuilder();
        if(encode.equals("MD5")){
            strBuilder.append(secret);
        }
        for (String key : keys) {
            System.out.println(key+"");
            if(!key.equals("top_sign")&&!key.equals("view")){
                String value = params.get(key);
                strBuilder.append(key).append(value);

                System.out.println(value);
            }

        }
        // 第三步：使用MD5/HMAC加密

        byte[] bytes=null;
        if(encode.equals("MD5")){
            strBuilder.append(secret);
            System.out.println(strBuilder.toString());
            bytes = MD5Util.getMD5String(strBuilder.toString());
        }else{
            bytes = encryptHMAC1(strBuilder.toString(), secret,encode);
            System.out.println(strBuilder.toString());
        }
// 第四步：把二进制转化为大写的十六进制
        return byte2hex1(bytes);
    }
    private static byte[] encryptHMAC1(String data, String secret,String encode) throws IOException {
        byte[] bytes = null;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes("UTF-8"),encode);
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes("UTF-8"));
            //query.append(secret);
          //  bytes = encryptMD5(query.toString());
        } catch (GeneralSecurityException gse) {
            Log.e("TB_ERR", gse.getMessage());
        }
        return bytes;
    }


    public static String byte2hex1(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
}
