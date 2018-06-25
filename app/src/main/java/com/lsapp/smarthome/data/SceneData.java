package com.lsapp.smarthome.data;

import com.lsapp.smarthome.data.base.BaseData;
import com.lsapp.smarthome.data.base.Scene;

import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class SceneData extends BaseData {
    private List<Scene> list;

    public List<Scene> getList() {
        return list;
    }

    public void setList(List<Scene> list) {
        this.list = list;
    }
}
