package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.Control;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/29.
 */
public class ControlData extends BaseData {

    private ArrayList<Control> list;

    public ArrayList<Control> getList() {
        return list;
    }

    public void setList(ArrayList<Control> list) {
        this.list = list;
    }
}
