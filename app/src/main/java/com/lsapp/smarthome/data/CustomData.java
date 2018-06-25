package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 */
public class CustomData extends BaseData{
    ArrayList<String> list;

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }
}
