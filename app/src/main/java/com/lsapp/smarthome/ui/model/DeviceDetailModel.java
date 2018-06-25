package com.lsapp.smarthome.ui.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.Device;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.DialogTimeUploadBinding;
import com.lsapp.smarthome.ui.DeviceDetailActivity;
import com.zuni.library.utils.zToastUtil;


/**
 * Created by Administrator on 2016/9/20.
 */
public class DeviceDetailModel extends BaseModel<BaseData> {
    private DeviceDetailActivity view;
    private Device device;
    private Device controlDevice;
    private Scene scene;
    private int timeSet;


    public DeviceDetailModel(DeviceDetailActivity context) {
        super(context);
        view = context;
    }

    public Device getControlDevice() {
        return controlDevice;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    private void set(String sec) {
        setDeviceTime(timeSet = ((Integer.valueOf(sec))), device.getDeviceId(), this::action);
    }


    public void get() {
        getDeviceDetail(device.getDeviceId(), device.getItemType(), deviceListData -> {
            if (deviceListData.isSuccess()) {
                controlDevice = deviceListData.getList().get(0);
         //       controlDevice.getAirClerState().setFilter(1);
                view.setControlView();
            }
        });
    }

    public void unbind() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.unbinding));
        builder.setMessage(context.getString(R.string.unbinding_content));
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        builder.setPositiveButton(context.getString(R.string.unbinding), (dialogInterface, i) -> {
            deleteDevice(scene.getUserId(),scene.getSpaceId(), device.getDeviceId(), baseData -> {
                if (baseData.isSuccess()) {
                    if (device.getDeviceId().equals(BaseApplication.get(context).getSelectScene().getDeviceId())) {
                        BaseApplication.get(context).getSelectScene().setDeviceId(null);
                    } else {
                        BaseApplication.get(context).getSelectScene().setActionId(null);
                    }
                    view.finish();
                } else {
                    zToastUtil.showSnack(view.getBindingView(), baseData.getFailExecuteMsg());
                }
            });
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void lightControl(int wind, int work) {
        lightControl(device.getDeviceId(), wind, work, baseData -> {
            view.dismissDialog();
            if (baseData.isSuccess()) {
                controlDevice.setFanOn(wind);
                controlDevice.setWorklightOn(work);
            }
            view.setLightView();
        });
    }

    public void airControl(int value) {
        ariControl(device.getDeviceId(), controlDevice.getAirClerState().getPurifier(),
                controlDevice.getAirClerState().getIonMode(), controlDevice.getAirClerState().getTimer() * 60, value, baseData -> {
                    if (baseData.isSuccess()) {
                        controlDevice.getAirClerState().setPanelLight(value);
                    }
                    view.setLightView();
                });
    }

    public void setTimeDialog() {
        int[] number = {3, 30, 60};
        DialogTimeUploadBinding dialogBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dialog_time_upload, null, false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.time_list, R.layout.text_view);
        adapter.setDropDownViewResource(R.layout.text_view);
        dialogBinding.timeSpinner.setAdapter(adapter);
        dialogBinding.timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dialogBinding.timeEdit.setVisibility(i == 3 ? View.VISIBLE : View.GONE);
                dialogBinding.timeEdit.setText(i == 3 ? "" : number[i] + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogBinding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        dialogBinding.sceneDismiss.setOnClickListener(view1 -> alertDialog.dismiss());
        dialogBinding.sceneConfirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(dialogBinding.timeEdit.getText().toString()))
                dialogBinding.timeEdit.setError(context.getString(R.string.time_null_toast));
            else {
                set(dialogBinding.timeEdit.getText().toString());
                alertDialog.dismiss();
            }
        });
    }


    public void filter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确认滤网已更换？");
        builder.setMessage("更换滤网需要手动去操作，如果确定滤网已经更换完成，请点击【完成】，然后滤网更换提示会消失");
        builder.setPositiveButton(context.getString(R.string.finish), (dialogInterface, i) -> finishFilter(device.getDeviceId(), baseData -> {
            if (baseData.isSuccess()){
                controlDevice.getAirClerState().setFilter(0);
                view.setControlView();
            }
            else zToastUtil.show(context, baseData.getFailExecuteMsg());
        }));
        builder.setNegativeButton(context.getString(R.string.cancel), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void success(BaseData model) {
        controlDevice.setUpInterval(timeSet);
        view.setView();
        zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.set_success_toast));
    }

    @Override
    protected void failed(BaseData model) {
        zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.set_failed_toast));
    }

    @Override
    protected void connectFail() {

    }

    @Override
    protected void throwError() {
        zToastUtil.showSnack(view.getBindingView(), context.getString(R.string.exception_toast));
        //    view.dismissDialog();
    }
}
