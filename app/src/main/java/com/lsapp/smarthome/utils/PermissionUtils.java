package com.lsapp.smarthome.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Administrator on 2016/9/6.
 */
public class PermissionUtils {

    public static boolean lacksPermissions(Context context,String... permissions) {
        for (String permission : permissions) {

                if (ContextCompat.checkSelfPermission(context, permission) !=
                    PackageManager.PERMISSION_GRANTED) {

                    return true;
            }
        }
        return false;
    }

}
