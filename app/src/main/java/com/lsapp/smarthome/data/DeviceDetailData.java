package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.CheckData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/7.
 */
public class DeviceDetailData extends BaseData {
    private ArrayList<CheckData> list;

    public ArrayList<CheckData> getList() {
        return list;
    }

    public void setList(ArrayList<CheckData> list) {
        this.list = list;
    }
}
