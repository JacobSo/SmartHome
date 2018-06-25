package com.lsapp.smarthome.ui.model;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.data.CustomTaskData;
import com.lsapp.smarthome.data.base.CustomTask;
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.ui.CustomizeListActivity;
import com.zuni.library.utils.zToastUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CustomizeModel extends BaseModel<CustomTaskData> {
    private CustomizeListActivity customView;
    private Scene scene;
    private ArrayList<CustomTask> taskList;

    public CustomizeModel(CustomizeListActivity context) {
        super(context);
        customView = context;
    }

    public ArrayList<CustomTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<CustomTask> taskList) {
        this.taskList = taskList;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void get() {
        getCustomList(scene.getActionId(), this::action);
    }

    public void delete(int timeChock) {
        deleteCustomTask(timeChock, scene.getActionId(), baseData -> {
            if (baseData.isSuccess()) {
                zToastUtil.showSnack(customView.getBindingView(),context.getString(R.string.message_delete_success));
                get();
            } else
                zToastUtil.showSnack(customView.getBindingView(),baseData.getFailExecuteMsg());
        });
    }

    public void control(String id, int time, boolean enable) {
        setCustomEnable(id, time, enable, baseData -> {
            if (!baseData.isSuccess()) {
                customView.onFailSwitch();
            }
        });
    }

    @Override
    protected void success(CustomTaskData model) {
        setTaskList(model.getList());
        customView.setView();
    }

    @Override
    protected void failed(CustomTaskData model) {
        customView.setFailedView();
    }

    @Override
    protected void connectFail() {
        customView.setFailedView();
    }

    @Override
    protected void throwError() {
        customView.setFailedView();
    }
}
