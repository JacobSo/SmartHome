package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.example.smartlinklib.SmartLinkManipulator;
import com.hiflying.smartlink.ISmartLinker;
import com.hiflying.smartlink.OnSmartLinkListener;
import com.hiflying.smartlink.SmartLinkedModule;
import com.hiflying.smartlink.v3.SnifferSmartLinker;
import com.hiflying.smartlink.v7.MulticastSmartLinker;
import com.lsapp.smarthome.app.Log;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.DeviceListData;
import com.lsapp.smarthome.data.base.Device;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.DialogDeviceConnectBinding;
import com.lsapp.smarthome.databinding.DialogMacBackupBinding;
import com.lsapp.smarthome.ui.DeviceListActivity;
import com.lsapp.smarthome.ui.GuideActivity;
import com.lsapp.smarthome.utils.MacUtil;
import com.zuni.library.utils.zContextUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class DeviceListModel extends BaseModel<DeviceListData> {
    private DeviceListActivity view;
    private List<Device> deviceList;
    private Scene scene;
    private static final String TAG = "DeviceListModel";
    private SmartLinkManipulator sm;
    private String pwd = null;
    private int machineType = 1;
    private String mac;
    private boolean isBinding = true;
    private ISmartLinker mSnifferSmartLinker;
    private Handler handler = new Handler() {//first connect or reconnect

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            view.dismissDialog();

            if (msg.what == 0) {
                isBinding = true;
                view.dismissDialog();
                view.loadingDialog("发现设备:"+msg.getData().getString("mac"));
                mac = MacUtil.formatMac(msg.getData().getString("mac"));
                if (deviceList != null && deviceList.size() != 0) {
                    Device existDevice = null;
                    for (Device d : deviceList) {
                        if (d.getDeviceId().equals(mac)) {//reconnect
                            existDevice = d;
                            isBinding = false;
                        }
                    }
                    if (isBinding) {
                        bind();
                    } else {
                        view.dismissDialog();
                        Log.d(TAG, existDevice.getDeviceId() + ":connected，" + existDevice.getItemType());
                      //  zToastUtil.show(context,"发现设备:"+msg.getData().getString("mac") );
                      //  zToastUtil.show(context, "设备已连接");
                        zToastUtil.showSnackIndefiniteWithButton(view.getBindingView(), "设备已连接", "确定", view1 -> view.onRefresh());
                        existDevice.setIsConnect(1);
                        if (existDevice.getItemType() == 1)
                            BaseApplication.get(context).getSelectScene().setCheckairConnect(1);//check
                        else
                            BaseApplication.get(context).getSelectScene().setAirclearConnect(1);//action
                    }
                } else bind();//first connect
            } else {//1,2//success or fail ,never call?
                mSnifferSmartLinker.setOnSmartLinkListener(null);
                mSnifferSmartLinker.stop();
                if (!isBinding)
                    view.onRefresh();
                zToastUtil.show(context, msg.getData().getString("msg"));
              //  zToastUtil.showSnackIndefiniteWithButton(view.getBindingView(),msg.getData().getString("msg"), "确定", view1 -> view.onRefresh());

            }
        }
    };

    public DeviceListModel(DeviceListActivity context) {
        super(context);
        view = context;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }


    private void setDeviceList(List<Device> deviceList) {
        this.deviceList = deviceList;
        for (Device d : deviceList) {
            if (d.getItemType() == 1) {
                BaseApplication.get(context).getSelectScene().setDeviceId(d.getDeviceId());
                BaseApplication.get(context).getSelectScene().setCheckairConnect(d.getIsConnect());
            } else if (d.getItemType() == 2) {
                BaseApplication.get(context).getSelectScene().setActionId(d.getDeviceId());
                //      BaseApplication.get(context).getSelectScene().setAirClerState(d.getAirClerState());
                BaseApplication.get(context).getSelectScene().setAirclearConnect(d.getIsConnect());
            } else {
                BaseApplication.get(context).getSelectScene().setDeviceId(d.getDeviceId());
                BaseApplication.get(context).getSelectScene().setActionId(d.getDeviceId());
                //   BaseApplication.get(context).getSelectScene().setAirClerState(d.getAirClerState());
                BaseApplication.get(context).getSelectScene().setCheckairConnect(d.getIsConnect());
                BaseApplication.get(context).getSelectScene().setAirclearConnect(d.getIsConnect());
            }
        }
    }

    public void get() {
        getDeviceList(scene.getUserId(),scene.getSpaceId(), this::action);
    }

    private void bind() {
        view.dismissDialog();
        view.loadingDialog(context.getString(R.string.loading_binding));

        Log.d(TAG, "bind: " + mac);
        addDevice(scene.getUserId(),scene.getSpaceId(), mac, scene.getSpaceName(), 0, baseData -> {
            view.dismissDialog();
            if (baseData.isSuccess()) {
                zToastUtil.showSnackIndefiniteWithButton(view.getBindingView(), "设备已连接", "确定", view1 -> view.onRefresh());
            } else {
                zToastUtil.showSnackIndefiniteWithButton(view.getBindingView(), baseData.getFailExecuteMsg(), "确定", view1 -> view.onRefresh());
/*                if (baseData.getFailExecuteMsg().equals("0")) {
                    macNotFoundDialog();
                }*/
            }
        });
    }

    private void macNotFoundDialog() {
        machineType = 1;//init
        DialogMacBackupBinding macBackupBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_mac_backup, null, false);
        macBackupBinding.checkMachine.setOnClickListener(view1 -> {
            macBackupBinding.checkMachine.setBackgroundResource(R.drawable.calendar_select);
            macBackupBinding.actionMachine.setBackgroundResource(android.R.color.transparent);
            machineType = 1;
        });
        macBackupBinding.actionMachine.setOnClickListener(view12 -> {
            macBackupBinding.actionMachine.setBackgroundResource(R.drawable.calendar_select);
            macBackupBinding.checkMachine.setBackgroundResource(android.R.color.transparent);
            machineType = 2;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(macBackupBinding.getRoot());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        macBackupBinding.confirm.setOnClickListener(view14 -> submitNewMac(mac, machineType, baseData -> {
            dialog.dismiss();
            if (baseData.isSuccess())
                bind();
            else
                zToastUtil.show(context, baseData.getFailExecuteMsg());

        }));
        macBackupBinding.dismiss.setOnClickListener(view13 -> dialog.dismiss());
    }

    public void connectDialog() {
        DialogDeviceConnectBinding connectBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_device_connect, null, false);
        connectBinding.deviceHelp.setOnClickListener(view1 -> {
            connectBinding.deviceHelpContent.setVisibility(connectBinding.deviceHelpContent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            connectBinding.deviceHelp.setText(connectBinding.deviceHelpContent.getVisibility() == View.VISIBLE ?
                    context.getString(R.string.close_connect_help) : context.getString(R.string.expand_connect_help));
        });
        connectBinding.deviceQuick.setOnClickListener(view12 -> context.startActivity(new Intent(context, GuideActivity.class)));
        connectBinding.deviceSsid.setText(zContextUtil.getSSid(context));
        if (pwd != null) {
            connectBinding.devicePwd.setText(pwd);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(connectBinding.getRoot());
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        connectBinding.deviceConfirm.setOnClickListener(view1 -> {
            //   mac = "bb:aa:aa:aa:aa:aa";
            //    bind();
            if (TextUtils.isEmpty(connectBinding.devicePwd.getText().toString())) {
                connectBinding.devicePwd.setError(context.getString(R.string.password_null_toast));
                return;
            }
            pwd = connectBinding.devicePwd.getText().toString();
            dialog.dismiss();
            view.loadingDialog(context.getString(R.string.connecting_toast));
//update
            //init
            int smartLinkVersion = context.getIntent().getIntExtra("EXTRA_SMARTLINK_VERSION", 3);
            if(smartLinkVersion == 7) {
                mSnifferSmartLinker = MulticastSmartLinker.getInstance();
            }else {
                mSnifferSmartLinker = SnifferSmartLinker.getInstance();
            }
            //stop
/*            mSnifferSmartLinker.setOnSmartLinkListener(null);
            mSnifferSmartLinker.stop();
            mIsConncting = false;*/
//start
            try {
                mSnifferSmartLinker.setOnSmartLinkListener(new OnSmartLinkListener(){

                    @Override
                    public void onLinked(SmartLinkedModule smartLinkedModule) {
                        Log.d("find", "发现设备  " + smartLinkedModule.getMid() + "mac" + smartLinkedModule.getMac() + "IP" + smartLinkedModule.getModuleIP());
                        Message msg = handler.obtainMessage(0);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", context.getString(R.string.connect_title));
                        bundle.putString("mac", smartLinkedModule.getMac());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onCompleted() {
                        Message msg = handler.obtainMessage(2);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", context.getString(R.string.connect_success_toast));
                        msg.setData(bundle);
                    }

                    @Override
                    public void onTimeOut() {
                        Message msg = handler.obtainMessage(1);
                        Bundle bundle = new Bundle();
                        bundle.putString("msg", context.getString(R.string.connect_time_out_toast));
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                });
                //开始 smartLink
                mSnifferSmartLinker.start(context, connectBinding.devicePwd.getText().toString(),
                        connectBinding.deviceSsid.getText().toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Message msg = handler.obtainMessage(1);
                Bundle bundle = new Bundle();
                bundle.putString("msg", context.getString(R.string.connect_time_out_toast));
                msg.setData(bundle);
                handler.sendMessage(msg);
            }


        });
        connectBinding.deviceDismiss.setOnClickListener(view1 -> dialog.dismiss());
    }


    @Override
    protected void success(DeviceListData model) {
        setDeviceList(model.getList());
        view.setView();
    }

    @Override
    protected void failed(DeviceListData model) {
        view.setView();
        //   zToastUtil.showSnack(view.getBindingView(),model.getFailExecuteMsg());
    }

    @Override
    protected void connectFail() {
        view.setView();
    }

    @Override
    protected void throwError() {
        view.setView();
    }
}
