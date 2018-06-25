package com.lsapp.smarthome.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lsapp.smarthome.app.Log;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.databinding.DialogLoadingBinding;

import com.lsapp.smarthome.event.WeatherEvent;
import com.lsapp.smarthome.network.WebService;
import com.lsapp.smarthome.network.WebServiceClient;
import com.lsapp.smarthome.utils.GuideUtil;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.zuni.library.ui.SystemBarTintManager;
import com.zuni.library.utils.zContextUtil;
import com.zuni.library.utils.zSharedPreferencesUtil;
import com.zuni.library.utils.zToastUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/8.
 */
public abstract class BaseActivity extends AppCompatActivity implements AMapLocationListener {
    private Dialog dialog;
    private WebService webService;//

    protected abstract void initModel();

    public abstract View getBindingView();

    public AMapLocationClient mLocationClient = null;

    protected GuideUtil guideUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initModel();
        guideUtil = new GuideUtil(this);
        mLocationClient = new AMapLocationClient(getApplicationContext());

        webService = WebServiceClient.getInstance().create(WebService.class);


    }

    protected void setTitle(Toolbar toolbar, String strTitle, boolean showHome) {
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        setTitle(strTitle);
        getSupportActionBar().setDisplayShowHomeEnabled(showHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showHome);
    }

    protected void setStatusBarColor(int res) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(res);
        }
    }

    /**
     * 天气初始化过程：
     * app launch-> get location ->if success -> set application -> set sp -> get weather ->set view
     * ->if fail -> get sp -> set application -> get weather -> set view
     */
    private void initWeather() {
        Log.e("Base", "initWeather");

        if (BaseApplication.get(this).getCity() == null || BaseApplication.get(this).getProvince() == null) {
            BaseApplication.get(this).setProvince(zSharedPreferencesUtil.get(this, Const.SP, Const.SP_WEATHER_PROVINCE));
            BaseApplication.get(this).setCity(zSharedPreferencesUtil.get(this, Const.SP, Const.SP_WEATHER_CITY));
        }
        if (BaseApplication.get(this).getWeatherData() == null) {
            getWeather(BaseApplication.get(this).getProvince(), BaseApplication.get(this).getCity());
        }
    }

    protected void initLocation() {
        Log.e("Base", "initLocation");
        if (BaseApplication.get(this).getCity() == null || BaseApplication.get(this).getProvince() == null ||
                TextUtils.isEmpty(BaseApplication.get(this).getCity()) || TextUtils.isEmpty(BaseApplication.get(this).getProvince())) {
            if (zContextUtil.isLocationFunctionOn(this)) {
                mLocationClient.setLocationListener(this);
                AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //     mLocationOption.setOnceLocation(true);
                mLocationOption.setInterval(1000);
            //    mLocationOption.setMockEnable(true);
                mLocationOption.setNeedAddress(true);
                mLocationClient.setLocationOption(mLocationOption);
                mLocationClient.startLocation();
            } else zToastUtil.show(this, getString(R.string.location_off_weather_fail));
        } else initWeather();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                Log.d("location", aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet());
                if (BaseApplication.get(BaseActivity.this).getCity() == null) {// no location set it
                    BaseApplication.get(BaseActivity.this).setProvince(aMapLocation.getProvince());
                    BaseApplication.get(BaseActivity.this).setCity(aMapLocation.getCity());
                    BaseApplication.get(BaseActivity.this).setArea(aMapLocation.getDistrict());
                    BaseApplication.get(BaseActivity.this).setAddress(aMapLocation.getStreet());
                    BaseActivity.this.initWeather();
                } else {//has location,stop it
                    if (mLocationClient.isStarted())
                        mLocationClient.stopLocation();
                }
            } else {//error stop it,use sp
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo() + "detail:" + aMapLocation.getLocationDetail());
                mLocationClient.stopLocation();
                BaseActivity.this.initWeather();
                if (aMapLocation.getErrorCode() == 12)
                    zToastUtil.show(this, getString(R.string.location_off_weather_fail));
            }
        }
    }

    private void getWeather(String province, String city) {
        Log.e("Base", "getWeather");
        if (BaseApplication.get(this).getCity() != null && BaseApplication.get(this).getProvince() != null && !BaseApplication.get(this).getCity().equals("")) {
            webService.weather(Const.SERVICE_KEY, city.substring(0, city.length() - 1), province)
   //    if(true){
           // webService.weather(Const.SERVICE_KEY, "佛山", "广东")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weatherData -> {
                        if (weatherData.getRetCode().equals("200")) {
                            BaseApplication.get(this).setWeatherData(weatherData);
                            RxBus.getDefault().post(new WeatherEvent(weatherData));
                        } else {
                            zToastUtil.show(this, getString(R.string.weather_fail_port) + weatherData.getMsg());
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        zToastUtil.show(this, getString(R.string.weather_fail_text));
                    });
        } else {//get location fail ,run again
            /// initLocation();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        //    mLocationClient.stop();
        mLocationClient.stopLocation();

    }


    public void loadingDialog(String text) {
        DialogLoadingBinding loadingBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.dialog_loading, null, false);
        //   Dialog dialog = new Dialog(this,R.style.selectorDialog);
        loadingBinding.textView.setText(text);
        dialog = new AlertDialog.Builder(this).create();

        dialog.show();
        dialog.setContentView(loadingBinding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void viewVisibleAnim(View v, int visible) {
        Animation animation;
        if (visible == View.VISIBLE) {
            if (v.getVisibility() == View.GONE) {
                animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_center);
                v.setVisibility(View.VISIBLE);
                v.startAnimation(animation);
            }

        } else {
            if (v.getVisibility() == View.VISIBLE) {
                animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_center);
                v.setVisibility(View.GONE);
                v.startAnimation(animation);
            }
        }

    }

    protected void checkUpdate(boolean isToast) {
        PgyUpdateManager.register(BaseActivity.this, "lshome",
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                        Log.v(getClass().getName(), result + "");
                        final AppBean appBean = getAppBeanFromString(result);
                        new android.app.AlertDialog.Builder(BaseActivity.this)
                                .setTitle(getString(R.string.update_title))
                                .setMessage(getString(R.string.update_content))
                                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startDownloadTask(BaseActivity.this, appBean.getDownloadURL());
                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        Log.d("PGYER", "onNoUpdateAvailable: update to date");
                        if (isToast)
                            zToastUtil.show(BaseActivity.this, getString(R.string.update_up_to_date));
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        PgyUpdateManager.unregister();
    }
}
