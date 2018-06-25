package com.lsapp.smarthome.app;

import android.Manifest;

/**
 * Created by Administrator on 2016/8/11.
 */
public class Const {
    // public static final String test_uid = "18680006907";


    public static final String TAOBAO_APPKEY = "23435993";
    public static final String TAOBAO_APPSECERT = "f6c9bc0a37b47ed7ff21e448111946ff";

    public static final String SMS_APPKEY = "15fc50cf88609";
    public static final String SMS_APPSECRET = "c8c29f3e6ac7205ee856028810f030db";
    public static final String SERVICE_KEY = "160c7bfe217ec";

    public static final String SP = "smart_home";
    public static final String DEFAULT_SP = "com.lsapp.smarthome_preferences";
    public static final String SP_TOKEN = "token";
    public static final String SP_UID = "uid";
    public static final String SP_USER_NAME = "userName";
    public static final String SP_PARTY = "party";
    public static final String SP_PERMISSION = "u_permission";

    public static final String GUIDE_SP = "smart_home_guider";
    public static final String GUIDE_OUTLET = "GUIDE_OUTLET";

    public static final String GUIDE_INTRO = "guide_intro";
    public static final String GUIDE_MAIN_QUICK = "guide_main_quick";
    public static final String GUIDE_MAIN_SLIDE = "guide_main_slide";
    public static final String GUIDE_MAIN_PANEL = "guide_main_panel";
    public static final String GUIDE_MAIN_PANEL_SUB = "guide_main_panel_sub";
    public static final String GUIDE_MAIN_WEATHER = "guide_main_weather";

    public static final String GUIDE_ROOM_ADD = "guide_room_add";
    public static final String GUIDE_CUSTOMIZE_ADD = "guide_customize_add";
    public static final String GUIDE_MEMBER_ADD = "guide_member_add";
    public static final String GUIDE_DEVICE_ADD = "guide_device_add";
    public static final String GUIDE_CALENDAR = "guide_calendar";
    public static final String GUIDE_CHART = "guide_chart";


    public static final String SP_PUSH_WARM = "warm";
    public static final String SP_PUSH_COMMON = "common";
    public static final String SP_PUSH_NEWS = "news";
    public static final String SP_SETTING_UPDATE = "setting_update";
    public static final String SP_Y = "1";
    public static final String SP_N = "0";
    public static final String SP_LAST_MSG = "last_message";

    public static final String SP_WEATHER_PROVINCE = "province";
    public static final String SP_WEATHER_CITY = "city";
    public static final String HELP_HREF = "http://www.lsmuyprt.com/help/ ";
    public static final String WEBSITE = "http://www.linshimuye.com/";
    public static final String FEEDBACK_HREF = "http://www.lsmuyprt.com/help/request/request.html?";


    // 所需的全部权限
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
      //      Manifest.permission.READ_PHONE_STATE,
         //   Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECEIVE_SMS,
         //   Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
       //     Manifest.permission.ACCESS_COARSE_LOCATION,
         //   Manifest.permission.GET_ACCOUNTS
            // Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };

}
