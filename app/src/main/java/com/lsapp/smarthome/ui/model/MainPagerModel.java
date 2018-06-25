package com.lsapp.smarthome.ui.model;


import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.data.SceneData;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.DialogMessageHelpBinding;
import com.lsapp.smarthome.databinding.DialogWeatherBinding;
import com.lsapp.smarthome.event.WeatherEvent;
import com.lsapp.smarthome.network.WebService;
import com.lsapp.smarthome.network.WebServiceClient;
import com.lsapp.smarthome.ui.MainActivity;
import com.lsapp.smarthome.ui.MainFragment;
import com.lsapp.smarthome.ui.MainPagerFragment;
import com.lsapp.smarthome.utils.WeatherUtil;
import com.zuni.library.utils.zDateUtil;
import com.zuni.library.utils.zSharedPreferencesUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/8/30.
 */
public class MainPagerModel extends BaseModel<SceneData> {
    private MainPagerFragment view;
    private List<Scene> sceneList = null;
    private WebService webService;//

    public MainPagerModel(MainPagerFragment context) {
        super(context.getActivity());
        view = context;
        webService = WebServiceClient.getInstance().create(WebService.class);
    }

    public List<Scene> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<Scene> sceneList) {
        this.sceneList = sceneList;
    }

    public void get() {
        getScene(this::action);
    }

    public void weatherDetailDialog() {
        DialogWeatherBinding weatherBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_weather, null, false);
        weatherBinding.setData(BaseApplication.get(context).getWeatherData());
        weatherBinding.weatherImage.setImageResource(WeatherUtil.getWeatherImage(BaseApplication.get(context).getWeatherData().getResult().get(0).getWeather()));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = builder.create();
        alertDialog.setView(weatherBinding.getRoot());
        weatherBinding.weatherDismiss.setOnClickListener(view1 -> alertDialog.dismiss());
        weatherBinding.weatherRefresh.setOnClickListener(view12 -> {
            alertDialog.dismiss();
            view.prepareRefreshWeather();
            weather(BaseApplication.get(context).getWeatherData().getResult().get(0).getCity(), BaseApplication.get(context).getWeatherData().getResult().get(0).getProvince());
        });
        alertDialog.show();
    }


    public void weather(String city, String province) {
        webService.weather(Const.SERVICE_KEY, city, province)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherData -> {
                    if (weatherData.getRetCode().equals("200")) {
                        BaseApplication.get(context).setWeatherData(weatherData);
                        view.setWeatherView(weatherData);
                    } else {
                        zToastUtil.show(context, context.getString(R.string.weather_fail_port) + weatherData.getMsg());
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    zToastUtil.show(context, context.getString(R.string.weather_fail_text));
                });
    }

    @Override
    protected void success(SceneData model) {
        sceneList = model.getList();
        boolean delete = false;
        if(BaseApplication.get(context).getSceneList()!=null){
            if (BaseApplication.get(context).getSceneList().size() > sceneList.size()) {
                delete = true;
                BaseApplication.get(context).setSelectPosition(0);
            }
        }
        BaseApplication.get(context).setSceneList(sceneList);
        BaseApplication.get(context).setGuide(true);
     //   zSharedPreferencesUtil.save(context,Const.GUIDE_SP,Const.GUIDE_MAIN_LIMIT,"1");//guide limit
        view.setView(delete);
    }

    @Override
    protected void failed(SceneData model) {
        zToastUtil.showSnack(view.getBindingView(), model.getFailExecuteMsg());
        view.setFailedView();
    }

    @Override
    protected void connectFail() {
        //zToastUtil.showSnack(view.getBindingView(), "connectFail");
        view.setFailedView();
    }

    @Override
    protected void throwError() {
        zToastUtil.showSnack(view.getBindingView(), "连接错误，请稍后再试");
        view.setFailedView();
    }
}
