package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.CustomTask;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CustomTaskData extends BaseData{
    ArrayList<CustomTask> list;

    public ArrayList<CustomTask> getList() {
        return list;
    }

    public void setList(ArrayList<CustomTask> list) {
        this.list = list;
    }
}
