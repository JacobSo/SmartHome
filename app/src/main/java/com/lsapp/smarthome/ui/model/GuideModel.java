package com.lsapp.smarthome.ui.model;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.data.GuideDeviceData;
import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.GuideDevice;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.ui.GuideActivity;
import com.lsapp.smarthome.utils.MacUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by Administrator on 2016/12/30.
 */

public class GuideModel extends BaseModel<BaseData> {
    private final static String TAG = "GuideModel";
    private GuideActivity view;
    private ArrayList<GuideDevice> devices = null;
    private ArrayList<Scene> scenes;
    private String mac;

    public GuideModel(GuideActivity context) {
        super(context);
        view = context;
    }

    public void setDevices(ArrayList<GuideDevice> devices) {
        this.devices = devices;
    }

    public void setScenes(ArrayList<Scene> scenes) {
        this.scenes = scenes;
    }

    public ArrayList<GuideDevice> getDevices() {
        return devices;
    }

    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    public void getByCode(String code) {
        view.loadingDialog(context.getString(R.string.loading));
        if (!code.contains(":")) {
            code = MacUtil.formatMac(code);
        }
        Log.d(TAG, "bind: " + code);
        mac = code;

        getGuideDevice(code, guideDeviceData -> {
            view.dismissDialog();
            if (guideDeviceData.isSuccess()) {
                if (devices == null) {
                    devices = guideDeviceData.getList();
                    scenes = guideDeviceData.getSpaceList();
                    if (devices.get(0).getItemType() == 1)//check
                        view.setStepTwo();
                    else
                        view.setDeviceFragment();//action
                } else {
                    if (devices.get(0).getMac().equals(guideDeviceData.getList().get(0).getMac())) {
                        view.setDeviceFragment();
                        //zToastUtil.show(context, context.getString(R.string.guide_page_same_error));
                    } else {
                        devices.add(guideDeviceData.getList().get(0));
                        view.setDeviceFragment();
                    }
                }
            } else {
/*                if (guideDeviceData.getFailExecuteMsg().equals("0")) {
                    submitNewMac(mac, devices == null || devices.size() == 0 ? 1 : 2, baseData -> {
                        if (baseData.isSuccess())
                            getByCode(mac);
                        else
                            zToastUtil.show(context, baseData.getFailExecuteMsg());

                    });
                }else*/
           //     zToastUtil.show(context, guideDeviceData.getFailExecuteMsg());
                zToastUtil.showSnackIndefiniteWithButton(view.getBindingView(), guideDeviceData.getFailExecuteMsg(), "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
        });

    }

    public void submit(int position, boolean isExist) {
        GuideDevice action = null, check = null;
        for (GuideDevice d : devices) {
            if (d.getItemType() == 1) {
                check = d;
            } else {
                action = d;
            }
        }
        bindGuideDevice(
                isExist ?scenes.get(position).getUserId(): BaseApplication.get(context).getUid(),
                isExist ? scenes.get(position).getSpaceId() : "",
                isExist ? scenes.get(position).getSpaceName() : context.getResources().getStringArray(R.array.scene_list)[position],
                isExist ? scenes.get(position).getSpaceType() : position,
                check == null ? "" : check.getMac(),
                action == null ? "" : action.getMac(),
                check == null ? "" : check.getSkuCode(),
                action == null ? "" : action.getSkuCode(),
                this::action
        );
    }

    @Override
    protected void success(BaseData model) {
        zToastUtil.show(context, "设置成功");
        context.finish();
    }

    @Override
    protected void failed(BaseData model) {
        zToastUtil.showSnack(view.getBindingView(), model.getFailExecuteMsg());
    }

    @Override
    protected void connectFail() {

    }

    @Override
    protected void throwError() {

    }
}
