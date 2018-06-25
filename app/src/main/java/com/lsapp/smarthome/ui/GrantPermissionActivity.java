package com.lsapp.smarthome.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.databinding.ActivityPermissionGrantBinding;
import com.lsapp.smarthome.utils.PermissionUtils;

/**
 * Created by Administrator on 2016/9/27.
 */
public class GrantPermissionActivity extends AppCompatActivity {
    ActivityPermissionGrantBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permission_grant);
        permission();

    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.System.canWrite(this)){
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }*/

    }

    private void permission() {
        if (PermissionUtils.lacksPermissions(this, Const.PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, Const.PERMISSIONS, 0);
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void readme() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.readme_grant));
        builder.setMessage(getString(R.string.detail_grant));
        builder.setPositiveButton(getString(R.string.begin_grant), (dialogInterface, i) -> permission());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int p : grantResults) {
                if (p != PackageManager.PERMISSION_GRANTED) {
                    readme();
                    return;
                }
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
