package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.RecoveryDevice;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12.
 */

public class RecoveryDeviceData extends BaseData {
    private List<RecoveryDevice> list;

    public List<RecoveryDevice> getList() {
        return list;
    }

    public void setList(List<RecoveryDevice> list) {
        this.list = list;
    }
}
