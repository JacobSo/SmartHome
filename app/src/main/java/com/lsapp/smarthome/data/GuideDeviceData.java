package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.GuideDevice;
import com.lsapp.smarthome.data.base.Scene;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/30.
 */

public class GuideDeviceData extends BaseData{
    private ArrayList<Scene> SpaceList;
    private ArrayList<GuideDevice> list;

    public ArrayList<Scene> getSpaceList() {
        return SpaceList;
    }

    public void setSpaceList(ArrayList<Scene> spaceList) {
        SpaceList = spaceList;
    }

    public ArrayList<GuideDevice> getList() {
        return list;
    }

    public void setList(ArrayList<GuideDevice> list) {
        this.list = list;
    }
}
