package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.Device;

import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class DeviceListData extends BaseData{
    private List<Device> list;

    public List<Device> getList() {
        return list;
    }

    public void setList(List<Device> list) {
        this.list = list;
    }
}
