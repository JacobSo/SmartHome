package com.lsapp.smarthome.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.event.NewFeedEvent;
import com.lsapp.smarthome.ui.MemberActivity;
import com.zuni.library.utils.zSharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/27.
 */
public class LsMessageReceiver extends MessageReceiver {
    private String TAG = getClass().getName();
    private static int messageCount = 0;


    @Override
    protected void onMessage(Context context, CPushMessage cPushMessage) {
        super.onMessage(context, cPushMessage);
        Log.d(TAG, "onMessage getAppId: " + cPushMessage.getAppId());
        Log.d(TAG, "onMessage getContent: " + cPushMessage.getContent());
        Log.d(TAG, "onMessage getMessageId: " + cPushMessage.getMessageId());
        Log.d(TAG, "onMessage getTitle: " + cPushMessage.getTitle());

    }

    @Override
    protected void onNotification(Context context, String s, String s1, Map<String, String> map) {
        super.onNotification(context, s, s1, map);
        Log.d(TAG, "onNotification s: " + s);
        Log.d(TAG, "onNotification s1: " + s1);
        Log.d(TAG, "onNotification map: " + map.toString());

    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: ");
    }


    @Override
    protected void onNotificationOpened(Context context, String s, String s1, String s2) {
        super.onNotificationOpened(context, s, s1, s2);
        Log.d(TAG, "onNotificationOpened s: " + s);
        Log.d(TAG, "onNotificationOpened s1: " + s1);
        Log.d(TAG, "onNotificationOpened s2: " + s2);
    }

    @Override
    protected void onNotificationRemoved(Context context, String s) {
        super.onNotificationRemoved(context, s);
        Log.d(TAG, "onNotificationRemoved s: " + s);
    }


    private void showNotification(Context context) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My notification")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentText("Hello World!");

        Intent resultIntent = new Intent(context, MemberActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MemberActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(messageCount++, mBuilder.build());
    }
}
