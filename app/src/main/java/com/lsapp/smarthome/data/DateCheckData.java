package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.DateCheck;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/15.
 */
public class DateCheckData extends BaseData {
    private ArrayList<DateCheck> list;

    public ArrayList<DateCheck> getList() {
        return list;
    }

    public void setList(ArrayList<DateCheck> list) {
        this.list = list;
    }
}
