package com.lsapp.smarthome.ui.model;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.data.AccountData;
import com.lsapp.smarthome.data.ControlData;
import com.lsapp.smarthome.data.CustomData;
import com.lsapp.smarthome.data.CustomTaskData;
import com.lsapp.smarthome.data.DateCheckData;
import com.lsapp.smarthome.data.DeviceDetailData;
import com.lsapp.smarthome.data.DeviceListData;
import com.lsapp.smarthome.data.GuideDeviceData;
import com.lsapp.smarthome.data.MessageData;
import com.lsapp.smarthome.data.RecoveryDeviceData;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.SceneData;
import com.lsapp.smarthome.data.HistoryData;
import com.lsapp.smarthome.network.ApiService;
import com.lsapp.smarthome.network.RetrofitClient;
import com.lsapp.smarthome.ui.LoginActivity;
import com.zuni.library.utils.zContextUtil;
import com.zuni.library.utils.zNetUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/11.
 */
public abstract class BaseModel<B extends BaseData> {
    protected abstract void success(B model);

    protected abstract void failed(B model);

    protected abstract void connectFail();

    protected abstract void throwError();

    private ApiService service;
    protected Activity context;

    private Action1<Throwable> throwableAction1 = throwable -> {
        //  error();
        throwable.printStackTrace();
        throwError();
    };

    public BaseModel(Activity context) {
        this.context = context;
        service = RetrofitClient.getInstance().create(ApiService.class);

    }

    private boolean connect() {
        if (!zNetUtil.isConn(context)) {
            zToastUtil.show(context, context.getString(R.string.no_network_toast));
            connectFail();
            return false;
        } else return true;
    }

    private boolean checkSession() {
        if (TextUtils.isEmpty(BaseApplication.get(context).getSessionKey())) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        } else return true;
    }


    protected void action(B model) {
        if (model.isSuccess()) {
            success(model);
        } else if (!model.isSuccess() && model.getFailExecuteMsg().equals(context.getString(R.string.session_toast))) {
            if (!BaseApplication.get(context).isLoginActivityVisible())
                context.startActivity(new Intent(context, LoginActivity.class));
        } else {
            failed(model);
        }
    }

    void signIn(String account, int way, String RealName, String pwd, Action1<AccountData> action1) {
        if (!connect()) return;
        service.login(account, way, zContextUtil.getDeviceId(context), "android", RealName, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getCode(String phone, Action1<BaseData> action1) {
        if (!connect()) return;
        service.smsCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }


    void signOut(Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.logout(BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getUserInfo(Action1<AccountData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.getUserInfo(BaseApplication.get(context).getUid(), BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void updateUserInfo(String nick, String phone, String birth, String sex, String province, String city,
                        String area, String address, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.updateUserInfo(BaseApplication.get(context).getUid(), zContextUtil.getDeviceId(context), nick, phone, birth, sex, province, city,
                area, address, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void addScene(String sceneName, int type, Action1<SceneData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.addScene(BaseApplication.get(context).getUid(), sceneName, type, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    protected void getScene(Action1<SceneData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.getScene(BaseApplication.get(context).getUid(), BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void updateScene(String sid, String sceneName, int type, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.updateScene(BaseApplication.get(context).getUid(), sid, sceneName, type, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void deleteScene(String sid, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.deleteScene(BaseApplication.get(context).getUid(), sid, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void addMember(String mobile, String name, int opera, int sms, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.addMember(mobile, name, opera, sms, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getMembers(Action1<AccountData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.getMyMembers(BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void confirmMembers(int result, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.confirmBeMember(result, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void bindAccount(String uid, String name, int platform, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.bindAccount(uid, name, platform, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void unbindAccount(String uid, int platform, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.unbindAccount(uid, platform, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void unbindingMember(String account, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.unbindingMember(account, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getMessage(int time, Action1<MessageData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getMessage(BaseApplication.get(context).getUid(), time, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getNewFeed(Action1<BaseData> action1) {

        service.getNewFeed(BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getPushConfig(Action1<AccountData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getPushConfig(BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void setPushConfig(int warn, int log, int news, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.setPushConfig(warn, log, news, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getDeviceList(String uid,String space, Action1<DeviceListData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getDeviceList(uid, space, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getDeviceDetail(String mac, int type, Action1<DeviceListData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getDeviceDetail(BaseApplication.get(context).getSessionKey(), mac, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void setDeviceTime(int seconds, String did, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.setDeviceTime(seconds, did, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getCustomCheckData(Action1<CustomData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getCustiomCheckData(BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void deleteCustomTask(int time, String device, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.deleteCustomTask(time, device, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void addCustomTask(String TriggerName, String DeviceId, boolean Enable, int TriggerType,
                       int RunMin, int Gear, int Anion, String Condition, String mac, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.addCustomTask(BaseApplication.get(context).getSessionKey(), TriggerName, DeviceId, Enable ? 1 : 0, TriggerType, RunMin, Gear, Anion, Condition, mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getCustomList(String device, Action1<CustomTaskData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getCustomList(device, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void setCustomEnable(String device, int time, boolean enable, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.setCustomEnable(BaseApplication.get(context).getSessionKey(), device, enable ? 1 : 0, time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getChartDetail(int data, int hours, String device, Action1<DeviceDetailData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getChartDetail(BaseApplication.get(context).getSessionKey(), device, data, hours)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void ariControl(String device, int wind, int anon, int time, int light, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.airControl(BaseApplication.get(context).getSessionKey(), device, wind, anon, time, light)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void lightControl(String device, int wind, int work, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.lightControl(BaseApplication.get(context).getSessionKey(), device, wind, work)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getCalendar(String device, String action, String date, Action1<DateCheckData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getCalendar(BaseApplication.get(context).getSessionKey(), device, action, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getDateData(String device, String date, Action1<HistoryData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getDateData(BaseApplication.get(context).getSessionKey(), device, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }


    void addDevice(String uid,String sid, String mac, String name, int recovery, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.addDevice(sid, uid, mac, name, recovery, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void deleteDevice(String uid,String sid, String mac, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.deleteDevice(uid, sid, mac, BaseApplication.get(context).getSessionKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    protected void getAirStatus(String airMac, Action1<ControlData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getAirStatus(BaseApplication.get(context).getSessionKey(), airMac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void quickTurn(String mac, int level, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.quickTurn(BaseApplication.get(context).getSessionKey(), level, mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    protected void getCheckLight(String mac, Action1<ControlData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;
        service.getCheckLightState(BaseApplication.get(context).getSessionKey(), mac)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void deleteMessage(String name, String party, String time, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.deleteMessage(BaseApplication.get(context).getSessionKey(), name, time, party)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getWeekChart(String device, int type, Action1<DeviceDetailData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.getWeekChart(BaseApplication.get(context).getSessionKey(), device, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void updatePermission(String userName, int party, int permission, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.modifyPermission(BaseApplication.get(context).getSessionKey(), userName, party, permission)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getSceneModify(Action1<SceneData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.getSceneModify(BaseApplication.get(context).getSessionKey(), BaseApplication.get(context).getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    //guide
    void getGuideDevice(String mac, Action1<GuideDeviceData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.guideGetDeviceList(BaseApplication.get(context).getSessionKey(), mac, BaseApplication.get(context).getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void bindGuideDevice(String uid,String spaceId, String spaceName, int spaceType, String deviceId, String actionId, String deviceSku, String actionSku, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.guideBinging(BaseApplication.get(context).getSessionKey(), uid, spaceId, spaceName, spaceType, deviceId, actionId, deviceSku, actionSku)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void getUnbindingList(Action1<RecoveryDeviceData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.getUnbindingList(BaseApplication.get(context).getSessionKey(), BaseApplication.get(context).getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void submitNewMac(String mac, int type, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.macSubmit(BaseApplication.get(context).getSessionKey(), mac, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

    void finishFilter(String id, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.finishFilter(BaseApplication.get(context).getSessionKey(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }


    void passwordSet(String pwd, Action1<BaseData> action1) {
        if (!checkSession()) return;
        if (!connect()) return;

        service.passwordSet(BaseApplication.get(context).getSessionKey(), BaseApplication.get(context).getUserName(), pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }


    Subscription realTimeData(String id, Action1<SceneData> action1) {
        if (!checkSession()) return null;
        if (!connect()) return null;
        return service.realTimeData(BaseApplication.get(context).getSessionKey(), id)
                .repeatWhen(observable -> observable.delay(5, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, throwableAction1);
    }

}
