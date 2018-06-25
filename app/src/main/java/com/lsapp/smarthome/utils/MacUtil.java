package com.lsapp.smarthome.utils;

/**
 * Created by Administrator on 2016/12/24.
 */

public class MacUtil {
    //b4db9823cfac
    //ac:cf:23:98:db:b4
    public static String formatFadeMac(String mac){
        StringBuilder sb = new StringBuilder();

        for(int i = 0;i<mac.length()/2;i++){
            sb.append(mac.substring(i*2,i*2+2)).append(":");
        }
        String[] temp = sb.toString().split(":");
        sb = new StringBuilder();
        for(int i = temp.length-1;i>=0;i--){
            sb.append(temp[i]).append(":");
        }
        return sb.toString().substring(0,sb.length()-1);
    }

    public static String formatMac(String mac){
        if(!mac.contains(":")){
            StringBuilder sb = new StringBuilder();

            for(int i = 0;i<mac.length()/2;i++){
                sb.append(mac.substring(i*2,i*2+2)).append(":");
            }
            return sb.toString().substring(0,sb.length()-1).toLowerCase();
        }else return mac.toLowerCase();

    }
}
