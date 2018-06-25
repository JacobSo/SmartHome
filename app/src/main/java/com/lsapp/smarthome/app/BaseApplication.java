package com.lsapp.smarthome.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.WeatherData;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.ui.LoginActivity;
import com.lsapp.smarthome.ui.MainActivity;
import com.mob.MobSDK;
import com.pgyersdk.crash.PgyCrashManager;
import com.zuni.library.utils.zSharedPreferencesUtil;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;


/**
 * Created by Administrator on 2016/8/8.
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "BaseApplication";
    private boolean isInterestingActivityVisible = false;
    //base info
    private String sessionKey = null;
    private String uid = null;
    //push need
    private String userName = null;
    private String party = null;

    private List<Scene> sceneList = null;
    private int selectPosition = 0;
    //weather
    private String city = null;
    private String province = null;
    private String area = null;
    private String address = null;
    private String permission = null;
    private WeatherData weatherData = null;
    private CloudPushService pushService;

    private boolean isGuide = false;

    //   private Control syncControl = null;
    //temp data
    public static BaseApplication get(Context context) {
        if (context == null)
            return null;
        else
            return (BaseApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
      //  ShareSDK.initSDK(this);
        MobSDK.init(this, "159ec0aa36c74", "2cbf0388f2a895ba60720f456f61a271");
        initCloudChannel();


    }

    public boolean isLoginActivityVisible() {
        return isInterestingActivityVisible;
    }

    /**
     * 初始化云推送通道
     */
    public void initCloudChannel() {
        try {
            PushServiceFactory.init(this);
            pushService = PushServiceFactory.getCloudPushService();
            pushService.register(this, new CommonCallback() {
                @Override
                public void onSuccess(String response) {
                    Log.d(TAG, "init cloudchannel success");
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
                }
            });
            pushService.setNotificationSmallIcon(R.drawable.notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initCloudAccount(String platform) {
        pushService.bindAccount(platform, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "bindAccount success" + platform);
            }

            @Override
            public void onFailed(String s, String s1) {
                Log.d(TAG, "bindAccount onFailed" + s + s1);
            }
        });

    }

    public void unbindCloudAccount() {
        pushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess: unbind");
            }

            @Override
            public void onFailed(String s, String s1) {
                Log.d(TAG, "onFailed: unbind");
            }
        });
    }

    public String getSessionKey() {
        if (sessionKey != null)
            return sessionKey;
        else
            return zSharedPreferencesUtil.get(this, Const.SP, Const.SP_TOKEN);
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_TOKEN, sessionKey);
    }

    public String getPermission() {
        if (permission != null)
            return permission;
        else
            return zSharedPreferencesUtil.get(this, Const.SP, Const.SP_PERMISSION);
    }

    public void setPermission(String permission) {
        this.permission = permission;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_PERMISSION, permission);
    }

    //logout execute
    public void removeSessionKey() {
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_TOKEN, null);
        this.sessionKey = null;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_UID, null);
        this.uid = null;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_USER_NAME, null);
        this.userName = null;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_PARTY, null);
        this.party = null;
    }

    public String getUserName() {
        if (userName != null)
            return userName;
        else
            return zSharedPreferencesUtil.get(this, Const.SP, Const.SP_USER_NAME);
    }

    public void setUserName(String userName) {
        this.userName = userName;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_USER_NAME, userName);
    }

    public String getParty() {
        if (party != null)
            return party;
        else
            return zSharedPreferencesUtil.get(this, Const.SP, Const.SP_PARTY);
    }

    public void setParty(String party) {
        this.party = party;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_PARTY, party);
    }

    public Scene getSelectScene() {
        if (getSceneList() == null||sceneList.size()==0)
            return null;
        else
            return sceneList.get(selectPosition);
    }


    public List<Scene> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<Scene> sceneList) {
        this.sceneList = sceneList;

    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_WEATHER_CITY, city);
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_WEATHER_PROVINCE, province);
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    public String getUid() {
        if (uid != null)
            return uid;
        else
            return zSharedPreferencesUtil.get(this, Const.SP, Const.SP_UID);
    }

    public void setUid(String uid) {
        this.uid = uid;
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_UID, uid);
    }

    public void removeUid() {
        zSharedPreferencesUtil.save(this, Const.SP, Const.SP_UID, null);
        this.uid = null;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isGuide() {
        return zSharedPreferencesUtil.get(this, Const.GUIDE_SP, Const.GUIDE_OUTLET).equals("1");
    }

    public void setGuide(boolean guide) {
        zSharedPreferencesUtil.save(this, Const.GUIDE_SP, Const.GUIDE_OUTLET, guide ? "1" : "0");
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof LoginActivity) {
            isInterestingActivityVisible = true;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof LoginActivity) {
            isInterestingActivityVisible = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
