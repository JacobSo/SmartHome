package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.CheckData;
import com.lsapp.smarthome.data.base.DateData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/16.
 */
public class HistoryData extends BaseData {

    private ArrayList<DateData> list;

    public ArrayList<DateData> getList() {
        return list;
    }

    public void setList(ArrayList<DateData> list) {
        this.list = list;
    }
}
